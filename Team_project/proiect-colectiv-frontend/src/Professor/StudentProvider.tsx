import React, {useCallback, useContext, useEffect, useReducer, useState} from 'react';
import PropTypes from "prop-types";
import {getLogger} from "../core";
import {getStudents, updateMaxNumberOfStudents} from "./StudentApi";
import Student from "../types/Student";
import Professor from "../types/Professor";
import {AuthContext} from "../auth/AuthProvider";
import {type} from "os";

const log = getLogger('StudentProvider');

export type UpdateMaxNumberOfStudents = (numberOfStudents: number) => Promise<any>

export interface ProfessorState {
    professor?:Professor,
    students?: Student[],
    fetching: boolean,
    fetchingError?: Error | null,
    updateMaxNumberOfStudentsFn?: UpdateMaxNumberOfStudents
}

interface ActionProps {
    type: string,
    payload?: any,
}

const initialState: ProfessorState = {
    fetching: false,
};

const FETCH_STUDENTS_STARTED = 'FETCH_STUDENTS_STARTED';
const FETCH_STUDENTS_SUCCEEDED = 'FETCH_STUDENTS_SUCCEEDED';
const FETCH_STUDENTS_FAILED = 'FETCH_STUDENTS_FAILED';
const INITIALIZE_PROFESSOR = 'INITIALIZE_PROFESSOR';

const reducer: (state: ProfessorState, action: ActionProps) => ProfessorState =
    (state, {type, payload}) => {
        switch (type) {
            case FETCH_STUDENTS_STARTED:
                return {...state, fetching: true, fetchingError: null};
            case FETCH_STUDENTS_SUCCEEDED:
                return {...state, students: payload.students, fetching: false};
            case FETCH_STUDENTS_FAILED:
                return {...state, fetchingError: payload.error, fetching: false};
            case INITIALIZE_PROFESSOR:
                return{...state, professor:payload.professor}
            default:
                return state;
        }
    };

export const ProfessorContext = React.createContext<ProfessorState>(initialState);

interface StudentProviderProps {
    children: PropTypes.ReactNodeLike,
}

export const StudentProvider: React.FC<StudentProviderProps> = ({children}) => {
    const [state, dispatch] = useReducer(reducer, initialState);
    const {students, fetching, fetchingError,professor} = state;
    const {user, authenticationToken}=useContext(AuthContext);
    const updateMaxNumberOfStudentsFn = useCallback<UpdateMaxNumberOfStudents>(updateMaxNumberOfStudentsCallback, [authenticationToken]);
    const value = {students, fetching, fetchingError,professor,updateMaxNumberOfStudentsFn};

    useEffect(() => {
        log("effect initialize professor")
        const professor=user as Professor;
        log(professor);
        dispatch({type: INITIALIZE_PROFESSOR, payload: {professor}})
    }, [user]);
    useEffect(getStudentsEffect,[professor]);

    log("returns");
    return (
        <ProfessorContext.Provider value={value}>
            {children}
        </ProfessorContext.Provider>
    );

    function getStudentsEffect() {
        log("get students effect")
        let canceled = false;
        fetchStudents();
        return () => {
            canceled = true;
        }

        async function fetchStudents() {
            try {
                if(professor){
                    log('fetchStudents started');
                    dispatch({type: FETCH_STUDENTS_STARTED});
                    const studentsResponse = await getStudents(authenticationToken,professor.professorId);
                    const students = studentsResponse?.data
                    log('fetchStudents succeeded');
                    log(students);
                    if (!canceled) {
                        dispatch({type: FETCH_STUDENTS_SUCCEEDED, payload: {students}});
                    }
                }else{
                    log("uninitialized professor")
                }
            } catch (error) {
                log('fetchStudents failed');
                dispatch({type: FETCH_STUDENTS_FAILED, payload: {error}});
            }
        }
    }

    async function updateMaxNumberOfStudentsCallback(numberOfStudents: number){
        const professorResponse= await updateMaxNumberOfStudents(authenticationToken, numberOfStudents)
        const professorRefreshed=professorResponse?.data
        console.log(professorRefreshed)
        dispatch({type: INITIALIZE_PROFESSOR, payload:{professor:professorRefreshed}})
    }

}
