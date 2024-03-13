import axios from "axios";
import {authConfig} from "../core";
import ThesisRequest from "../types/ThesisRequest";

const requestsURL = `${process.env.REACT_APP_API_URL}/graduation-thesis-requests`;

export type RequestsPayload = {
    data: ThesisRequest[]
}

export type SaveResponsePayload = {
    nextStatus: string,
    professorResponseMessage: string
}

export const getStudentRequests: (authToken: string) => Promise<RequestsPayload> = (authToken) => {
    return axios.get(requestsURL, authConfig(authToken))
}

export const saveStudentResponse: (authToken: string, requestId: number, response: string, nextStatus: string) => Promise<SaveResponsePayload> = (authToken, requestId, response, nextStatus) => {
    return axios.patch(`${requestsURL}/${requestId}`, {
        professorResponseMessage: response,
        nextStatus: nextStatus
    }, authConfig(authToken))
}