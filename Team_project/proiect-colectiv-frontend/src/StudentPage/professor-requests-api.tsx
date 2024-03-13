import axios from "axios";
import {authConfig} from "../core";
import Professor from "../types/Professor";
import ThesisRequest from "../types/ThesisRequest";
import ThesisRequestStatus from "../types/ThesisRequestStatus";
import {SaveResponsePayload} from "../ProfessorRequestsPage/professor-requests-api";

const professorUrl = `${process.env.REACT_APP_API_URL}/professors`
const thesisRequestUrl = `${process.env.REACT_APP_API_URL}/graduation-thesis-requests`
const graduationThesisUrl = `${process.env.REACT_APP_API_URL}/graduation-thesis`

export type ProfessorPayload = {
    data: Professor[]
}

export type GraduationThesisPayload = {
    data: {
        id: number,
        professorCoordinatorId: number,
        thesisTitle: string
    }
}

export type SaveThesisRequestPayload = {
    data: {
        thesisRequestId: number,
        studentId: number,
        professorId: number,
        initialStudentMessage: string,
        professorResponseMessage: string,
        requestSentAt: Date,
        status: ThesisRequestStatus
    }
}

export type GetThesisRequestsPayload = {
    data: [{
        thesisRequestId: number,
        studentId: number,
        professorId: number,
        initialStudentMessage: string,
        professorResponseMessage: string,
        requestSentAt: Date,
        status: ThesisRequestStatus
    }]
}

export type ChangeThesisTitlePayload = {
    data: {
        id: number,
        professorCoordinatorId: number,
        thesisTitle: string
    }
}

export const getProfessors: (authToken: string) => Promise<ProfessorPayload> = (authToken) => {
    return axios.get(professorUrl, authConfig(authToken));
}

export const sendThesisRequest: (authToken: string, professorId: number, studentInitialMessage: string) => Promise<SaveThesisRequestPayload> =
    (authToken, professorId, studentInitialMessage) => {

        return axios.post(thesisRequestUrl, {professorId, studentInitialMessage}, authConfig(authToken));
    }

export const getThesisRequests: (authToken: string) => Promise<GetThesisRequestsPayload> = (authToken) => {
    return axios.get(thesisRequestUrl, authConfig(authToken));
}

export const saveStudentResponse: (authToken: string, requestId: number, nextStatus: ThesisRequestStatus) => Promise<SaveThesisRequestPayload> = (authToken, requestId, nextStatus) => {
    return axios.patch(`${thesisRequestUrl}/${requestId}`, {
        nextStatus: nextStatus
    }, authConfig(authToken))
}

export const createThesis: (authToken: string, professorId: number, thesisTitle: string) => Promise<GraduationThesisPayload> =
    (authToken, professorId, thesisTitle) => {
        return axios.post(graduationThesisUrl,
            {professorCoordinatorId: professorId, thesisTitle: thesisTitle},
            authConfig(authToken))
    }


export const changeThesisTitleApi: (authToken: string, thesisId: number, title: String) => Promise<ChangeThesisTitlePayload> =
    (authToken, thesisId, title) => {
        return axios.patch(`${graduationThesisUrl}/${thesisId}`, {title: title}, authConfig(authToken))
    }