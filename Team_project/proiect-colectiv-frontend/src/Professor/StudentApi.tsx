import axios from 'axios';
import {authConfig, config} from "../core";
import Student from "../types/Student";
import Professor from "../types/Professor";
import User from "../types/User";
//to change below!!!!!!!!!!!!!!!!
const professorUrl = `${process.env.REACT_APP_API_URL}/professors`

export interface StudentsResponse {
    data: User
}

export interface updateMaxNumberOfStudentsResponse{
    data: Professor
}
export const getStudents: (authToken:string, professorId:number) => Promise<StudentsResponse> = (authToken, professorId) => {
    console.log()
    return axios.get(`${professorUrl}/${professorId}/students`, authConfig(authToken));
    //return Promise.resolve(mockStudents);
}

export const updateMaxNumberOfStudents: (authToken:string, increaseMaxNumberStudentsBy:number) => Promise<updateMaxNumberOfStudentsResponse> =(authToken, increaseMaxNumberStudentsBy) =>{
    return axios.patch(`${professorUrl}/me`,{
        increaseMaxNumberStudentsBy:increaseMaxNumberStudentsBy
        }, authConfig(authToken))

}

interface MessageData {
    type: string;
    payload:Student;
}