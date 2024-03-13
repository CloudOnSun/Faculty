import React, {useContext, useEffect, useReducer} from "react";
import PropTypes from 'prop-types';
import Professor from "../types/Professor";
import {AuthContext} from "../auth/AuthProvider";
import {getProfessors} from "./professor-requests-api";
import Student from "../types/Student";
import Department from "../types/Department";

export interface ProfessorState {
    professors?: Professor[],
    fetching: boolean,
    fetchingError?: Error | null,
}

interface ActionProps {
    type: string,
    payload?: any,
}

const initialState: ProfessorState = {
    fetching: false,
};

const FETCH_PROFESSORS_STARTED = "FETCH_PROFESSORS_STARTED";
const FETCH_PROFESSORS_SUCCEEDED = "FETCH_PROFESSORS_SUCCEEDED";
const FETCH_PROFESSORS_FAILED = "FETCH_PROFESSORS_FAILED"

const reducer: (state: ProfessorState, action: ActionProps) => ProfessorState =
    (state, { type, payload}) => {
        switch(type) {
            case FETCH_PROFESSORS_STARTED:
                return { ...state, fetching: true, fetchingError: null };
            case FETCH_PROFESSORS_SUCCEEDED:
                return { ...state, professors: payload.professors, fetching: false };
            case FETCH_PROFESSORS_FAILED:
                return { ...state, fetchingError: payload.error, fetching: false};
            default:
                return state;
        }
    };

export const ProfessorContext = React.createContext<ProfessorState>(initialState);

interface ProfessorProviderProps {
    children: PropTypes.ReactNodeLike,
}

export const ProfessorProvider: React.FC<ProfessorProviderProps> = ({children} : ProfessorProviderProps) => {
    const {authenticationToken, user} = useContext(AuthContext);
    const [state, dispatch] = useReducer(reducer, initialState);
    const { professors, fetching, fetchingError } = state;

    useEffect(getProfessorsEffect, [authenticationToken])

    const value = { professors, fetching, fetchingError }
    return (
        <ProfessorContext.Provider value={value}>
            {children}
        </ProfessorContext.Provider>
    )

    function filterProfessors(professors: Professor[]) {
        return professors.filter((p) => {
            let languageOk: boolean = true;
            let departmentOk: boolean = true;
            const studLanguage = (user as Student).studyLanguage;
            const studDepartment = (user as Student).facultyDepartment;

            if (!p.supportedLanguages.includes(studLanguage)) {
                languageOk = false;
            }
            if (studDepartment !== Department.MATHEMATICS_INFORMATICS && studDepartment !== p.department) {
                departmentOk = false;
            }

            return languageOk && departmentOk;
        })
    }

    function getProfessorsEffect() {
        let canceled = false;
        if (authenticationToken) {
            fetchProfessors();
        }
        return () => {
            canceled = true;
        }

        async function fetchProfessors() {
            try {
                dispatch({type: FETCH_PROFESSORS_STARTED})
                let professorsPayload = await getProfessors(authenticationToken);
                professorsPayload.data = professorsPayload.data.filter((p) => {
                    let languageOk: boolean = true;
                    let departmentOk: boolean = true;
                    const studLanguage = (user as Student).studyLanguage;
                    const studDepartment = (user as Student).facultyDepartment;

                    if (!p.supportedLanguages.includes(studLanguage)) {
                        languageOk = false;
                    }
                    if (studDepartment !== Department.MATHEMATICS_INFORMATICS && studDepartment !== p.department) {
                        departmentOk = false;
                    }

                    return languageOk && departmentOk;
                })
                console.log(professorsPayload.data)
                if (!canceled) {
                    dispatch({type: FETCH_PROFESSORS_SUCCEEDED, payload: {professors: professorsPayload.data}})
                }
            }
            catch (error) {
                if (!canceled) {
                    dispatch({type: FETCH_PROFESSORS_FAILED, payload: {error}})
                }
            }
        }
    }
}