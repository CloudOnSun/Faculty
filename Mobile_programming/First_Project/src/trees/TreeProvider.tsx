import { compassSharp } from "ionicons/icons";
import { getLogger } from "../core";
import { TreeProps } from "./TreeProps";
import React, { useCallback, useContext, useEffect, useReducer, useState } from "react";
import PropTypes from 'prop-types';
import { createTree, getTrees, newWebSocket, updateTree } from "./TreeApi";
import { AuthContext } from "../auth/AuthProvider";
import { NetworkContext } from "../network/NetworkProvider";


const log = getLogger("TreeProvider");

type SaveTreeFn = (tree: TreeProps) => Promise<any>;

export interface TreeState {
    trees?: TreeProps[],
    fetching: boolean,
    fetchingError?: Error | null,
    saving: boolean,
    savingError?: Error | null,
    saveTree?: SaveTreeFn
}

interface ActionProps {
    type: string,
    payload?: any,
}

const initialState: TreeState = {
    fetching: false,
    saving: false,
};

const FETCH_TREES_STARTED = "FETCH_TREES_STARTED";
const FETCH_TREES_SUCCEEDED = "FETCH_TREES_SUCCEEDED";
const FETCH_TREES_FAILED = "FETCH_TREES_FAILED"
const SAVE_TREES_STARTED = "SAVE_TREES_STARTED";
const SAVE_TREES_SUCCEEDED = "SAVE_TREES_SUCCEEDED";
const SAVE_TREES_FAILED = "SAVE_TREES_FAILED";

const reducer: (state: TreeState, action: ActionProps) => TreeState = 
    (state, { type, payload}) => {
        switch(type) {
            case FETCH_TREES_STARTED:
                return { ...state, fetching: true, fetchingError: null };
            case FETCH_TREES_SUCCEEDED:
                return { ...state, trees: payload.trees, fetching: false };
            case FETCH_TREES_FAILED:
                return { ...state, fetchingError: payload.error, fetching: false};
            case SAVE_TREES_STARTED:
                return { ...state, savingError: null, saving: true };
            case SAVE_TREES_SUCCEEDED:
                const trees = [...(state.trees || [])];
                const tree = payload.tree;
                const index = trees.findIndex(t => t._id === tree._id);
                if (index === -1) {
                    trees.splice(0, 0, tree);
                } else {
                    trees[index] = tree;
                }
                return { ...state, trees, saving: false};
            case SAVE_TREES_FAILED:
                return { ...state, savingError: payload.error, saving: false};
            default:
                return state;
        }
    };

export const TreeContext = React.createContext<TreeState>(initialState);

export const UnsyncedTreesContext = React.createContext<TreeProps[]>([]);

interface TreeProviderProps {
    children: PropTypes.ReactNodeLike,
}

export const TreeProvider: React.FC<TreeProviderProps> = ({ children }) => {
    const {token} = useContext(AuthContext);
    const [state, dispatch] = useReducer(reducer, initialState);
    const { trees, fetching, fetchingError, saving, savingError } = state;
    const {connected} = useContext(NetworkContext);
    const [unsyncedTrees, setUnsyncedTrees] = useState<TreeProps[]>([]);
    useEffect(getTreesEffect, [token]);
    useEffect(syncUnsyncedTrees, [connected]);
    log(trees)
    useEffect(wsEffect, [token]);
    const saveTree = useCallback<SaveTreeFn>(saveTreeCallback, [token]);
    const value = { trees, fetching, fetchingError, saving, savingError, saveTree };
    log("returns");
    return (
        <TreeContext.Provider value={value}>
            <UnsyncedTreesContext.Provider value={unsyncedTrees}>
                {children}
            </UnsyncedTreesContext.Provider>
        </TreeContext.Provider>
    )

    function getTreesEffect() {
        let canceled = false;
        if (token) {
            fetchTrees();
        }
        return () => {
            canceled = true;
        }

        async function fetchTrees() {
            try {
                log("fetchTrees started");
                dispatch({ type: FETCH_TREES_STARTED});
                const trees = await getTrees(token);
                log("fetchTrees succeeded");
                if(!canceled) {
                    dispatch({ type: FETCH_TREES_SUCCEEDED, payload: { trees }});
                }
            } catch (error) {
                log("fetchTrees failed");
                if(!canceled) {
                    dispatch({ type: FETCH_TREES_FAILED, payload: { error }});

                }
            }
        }
    }

    async function saveTreeCallback(tree: TreeProps) {
        try {
            log("saveTreeStarted");
            dispatch({ type: SAVE_TREES_STARTED });
            const savedTree = await (tree._id ? updateTree(token, tree) : createTree(token, tree));
            log("saveTree succeeded");
            dispatch({ type: SAVE_TREES_SUCCEEDED, payload: { tree: savedTree }});
            
            setUnsyncedTrees((prevUnsyncedTrees) => prevUnsyncedTrees.filter((t) => t._id !== tree._id));
        } catch (error) {
            log("saveTree failed");
            setUnsyncedTrees((prevUnsyncedTrees) => [...prevUnsyncedTrees, tree]);
            log("saved to unsynced tree");
            dispatch({ type: SAVE_TREES_FAILED, payload: { error }});
        }
    }

    function syncUnsyncedTrees() {
        if (unsyncedTrees.length > 0) {
            log("syncing trees");
            dispatch({ type: SAVE_TREES_STARTED });
            unsyncedTrees.forEach(async (tree) => {
                try {
                const savedTree = await (tree._id ? updateTree(token, tree) : createTree(token, tree));
                log("syncing tree" + savedTree);
                dispatch({ type: SAVE_TREES_SUCCEEDED, payload: { tree: savedTree }});
                // Remove the saved tree from the unsyncedTrees list.
                setUnsyncedTrees((prevUnsyncedTrees) => prevUnsyncedTrees.filter((t) => t._id !== tree._id));
                } catch (error) {
                log("Failed to sync unsynced tree:", error);
                }
            });
        }
      }
      

    function wsEffect() {
        let canceled = false;
        log("wsEffect - connecting");
        let closeWebSocket: () => void;
        if (token?.trim()){
            const closeWebSocket = newWebSocket(token, message => {
                if(canceled) {
                    return;
                }
                const { event, payload: tree} = message;
                log(`ws message, tree ${event}`);
                if (event === "created" || event === "updated") {
                    dispatch({ type: SAVE_TREES_SUCCEEDED, payload: { tree }});
                }
            });
        }
        return () => {
            log("wsEffect - disconnecting");
            canceled = true;
            closeWebSocket?.();
        }
    }
}