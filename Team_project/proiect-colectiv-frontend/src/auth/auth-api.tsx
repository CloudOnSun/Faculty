import axios from "axios";
import {authConfig, config} from "../core";
import User from "../types/User";
import UserRole from "../types/UserRole";

const studentUrl = `${process.env.REACT_APP_API_URL}/students`
const professorUrl = `${process.env.REACT_APP_API_URL}/professors`

const authUrl = `${process.env.REACT_APP_API_URL}/users/sign-in`

export interface AuthProps {
    data: {
        authenticationToken: string,
        refreshToken: string,
        userId: number,
        role: UserRole
    }
}

export interface UserResponse {
    data: User
}

export const loginApi: (userEmail?: string, password?: string) => Promise<AuthProps> = (userEmail, password) => {
    console.log(authUrl);
    console.log(userEmail + " " + password);
    return axios.post(authUrl, {userEmail, password}, config)
}

export const getUser: (authToken: string, userRole: UserRole, userId: number) => Promise<UserResponse | undefined> =
    (authToken, userRole, userId) => {
    if (userRole === UserRole.PROFESSOR) {
        return axios.get(`${professorUrl}/${userId}`, authConfig(authToken))
    }
    else if (userRole === UserRole.STUDENT) {
        return axios.get(`${studentUrl}/${userId}`, authConfig(authToken))
    }
    else {
        return Promise.resolve(undefined)
    }
}