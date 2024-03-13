import axios from "axios";
import { authConfig, baseUrl, getLogger, withLogs } from '../core';
import { TreeProps } from "./TreeProps";

const treesUrl = `http://${baseUrl}/api/trees`

interface ResponseProps<T> {
    data: T;
}

// function withLogs<T>(promise: Promise<ResponseProps<T>>, fnName: string): Promise<T> {
//     log(`${fnName} - started`);
//     return promise
//         .then(res => {
//             log(`${fnName} - succeeded`);
//             let data = Promise.resolve(res.data);
//             log(data)
//             return data
//         })
//         .catch(err => {
//             log(`${fnName} - failed`);
//             return Promise.reject(err);
//         });
// }

// const config = {
//     headers: {
//         'Content-Type': 'application/json'
//     }
// };

export const getTrees: (token: string) => Promise<TreeProps[]> = token => {
    return withLogs(axios.get(treesUrl, authConfig(token)), "getTrees");
}

export const createTree: (token: string, tree: TreeProps) => Promise<TreeProps[]> = (token, tree) => {
    return withLogs(axios.post(treesUrl, tree, authConfig(token)), "createTree");
}

export const updateTree: (token: string, tree: TreeProps) => Promise<TreeProps[]> = (token, tree) => {
    return withLogs(axios.put(`${treesUrl}/${tree._id}`, tree, authConfig(token)), "updateTree");
}

interface MessageData {
    event: string;
    payload: TreeProps;
}

const log = getLogger('ws')

export const newWebSocket = (token: string, onMessage: (data: MessageData) => void) => {
    const ws = new WebSocket(`ws://${baseUrl}`)
    ws.onopen = () => {
        log('web socket onopen');
        ws.send(JSON.stringify({type: 'authorization', payload: {token}}));
    };
    ws.onclose = () => {
        log('web socket onclose');
    };
    ws.onerror = error => {
        log('web socket onerror', error);
    };
    ws.onmessage = messageEvent => {
        log('web socket onmessage');
        onMessage(JSON.parse(messageEvent.data));
    };
    return () => {
        ws.close();
    }
}