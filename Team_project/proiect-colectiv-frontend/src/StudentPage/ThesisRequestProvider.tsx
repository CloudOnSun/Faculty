import React, {useCallback, useContext, useEffect, useReducer, useState} from "react";
import PropTypes from 'prop-types';
import ThesisRequest from "../types/ThesisRequest";
import {AuthContext} from "../auth/AuthProvider";
import {ProfessorContext} from "./ProfessorProvider";
import {
    changeThesisTitleApi,
    getThesisRequests,
    saveStudentResponse,
    sendThesisRequest
} from "./professor-requests-api";
import ThesisRequestStatus from "../types/ThesisRequestStatus";


type SaveThesisRequest = (thesisRequestPayload: ThesisRequestPayload) => Promise<any>;

export interface ThesisRequestState {
    thesisRequests?: ThesisRequest[],
    fetching: boolean,
    fetchingError?: Error | null,
    saving: boolean,
    savingError?: Error | null,
    saveThesisRequest?: SaveThesisRequest
}

interface ActionProps {
    type: string,
    payload?: any,
}

const initialState: ThesisRequestState = {
    fetching: false,
    saving: false,
};

const FETCH_THESIS_REQUESTS_STARTED = "FETCH_THESIS_REQUESTS_STARTED";
const FETCH_THESIS_REQUESTS_SUCCEEDED = "FETCH_THESIS_REQUESTS_SUCCEEDED";
const FETCH_THESIS_REQUESTS_FAILED = "FETCH_THESIS_REQUESTS_FAILED"
const SAVE_THESIS_REQUESTS_STARTED = "SAVE_THESIS_REQUESTS_STARTED";
const SAVE_THESIS_REQUESTS_SUCCEEDED = "SAVE_THESIS_REQUESTS_SUCCEEDED";
const SAVE_THESIS_REQUESTS_FAILED = "SAVE_THESIS_REQUESTS_FAILED";


const reducer: (state: ThesisRequestState, action: ActionProps) => ThesisRequestState =
    (state, {type, payload}) => {
        switch (type) {
            case FETCH_THESIS_REQUESTS_STARTED:
                return {...state, fetching: true, fetchingError: null};
            case FETCH_THESIS_REQUESTS_SUCCEEDED:
                return {...state, thesisRequests: payload.thesisRequests, fetching: false};
            case FETCH_THESIS_REQUESTS_FAILED:
                return {...state, fetchingError: payload.error, fetching: false};
            case SAVE_THESIS_REQUESTS_STARTED:
                return {...state, savingError: null, saving: true};
            case SAVE_THESIS_REQUESTS_SUCCEEDED:
                const thesisRequests = [...(state.thesisRequests || [])];
                const thesisRequest = payload.thesisRequest;
                const index = thesisRequests.findIndex(t => t.thesisRequestId === thesisRequest.thesisRequests);
                if (index === -1) {
                    thesisRequests.splice(0, 0, thesisRequest);
                } else {
                    thesisRequests[index] = thesisRequest;
                }
                return {...state, thesisRequests, saving: false};
            case SAVE_THESIS_REQUESTS_FAILED:
                return {...state, savingError: payload.error, saving: false};
            default:
                return state;
        }
    };

interface ThesisRequestProviderProps {
    children: PropTypes.ReactNodeLike,
}

export const ThesisRequestContext = React.createContext<ThesisRequestState>(initialState);

type ThesisRequestPayload = {
    thesisRequestId?: number,
    nextStatus?: ThesisRequestStatus,
    professorId?: number,
    studentInitialMessage?: string
}

export const ThesisRequestProvider: React.FC<ThesisRequestProviderProps> = ({children}: ThesisRequestProviderProps) => {
    const {authenticationToken} = useContext(AuthContext);
    const [state, dispatch] = useReducer(reducer, initialState);
    const {thesisRequests, fetching, fetchingError, saving, savingError} = state;
    useEffect(getThesisRequestEffect, [authenticationToken]);
    console.log(thesisRequests)
    const saveThesisRequest = useCallback<SaveThesisRequest>(saveThesisRequestCallback, [authenticationToken]);
    const value = {thesisRequests, fetching, fetchingError, saving, savingError, saveThesisRequest: saveThesisRequest};
    return (
        <ThesisRequestContext.Provider value={value}>
            {children}
        </ThesisRequestContext.Provider>
    )

    function getThesisRequestEffect() {
        let canceled = false;
        if (authenticationToken) {
            fetchThesisRequests();
        }
        return () => {
            canceled = true;
        }

        async function fetchThesisRequests() {
            try {
                dispatch({type: FETCH_THESIS_REQUESTS_STARTED});
                const thesisRequests = await getThesisRequests(authenticationToken);
                console.log(thesisRequests)
                if (!canceled) {
                    dispatch({type: FETCH_THESIS_REQUESTS_SUCCEEDED, payload: {thesisRequests: thesisRequests.data}});
                }
            } catch (error) {
                if (!canceled) {
                    dispatch({type: FETCH_THESIS_REQUESTS_FAILED, payload: {error}});
                }
            }
        }
    }

    async function saveThesisRequestCallback(thesisRequestPayload: ThesisRequestPayload) {
        try {
            dispatch({type: SAVE_THESIS_REQUESTS_STARTED});
            if (thesisRequestPayload.thesisRequestId && thesisRequestPayload.nextStatus) {
                const savedThesisRequest = await saveStudentResponse(
                    authenticationToken,
                    thesisRequestPayload.thesisRequestId,
                    thesisRequestPayload.nextStatus
                )
                dispatch({type: SAVE_THESIS_REQUESTS_SUCCEEDED, payload: {thesisRequest: savedThesisRequest.data}});
            }
            else if (thesisRequestPayload.professorId && thesisRequestPayload.studentInitialMessage) {
                const savedThesisRequest = await sendThesisRequest(
                    authenticationToken,
                    thesisRequestPayload.professorId,
                    thesisRequestPayload.studentInitialMessage);
                dispatch({type: SAVE_THESIS_REQUESTS_SUCCEEDED, payload: {thesisRequest: savedThesisRequest.data}})
            }
        } catch (error) {
            dispatch({type: SAVE_THESIS_REQUESTS_FAILED, payload: {error}});
        }
    }


}