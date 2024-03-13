import {useCallback, useContext, useEffect, useState} from "react";
import {ProfessorContext} from "./ProfessorProvider";
import "./styling-student-page/student-page.css"
import Professor from "../types/Professor";
import TextField from "@mui/material/TextField";
import {AuthContext} from "../auth/AuthProvider";
import {useNavigate} from "react-router-dom";
import {Button} from "@mui/material";
import {LogoutRounded} from '@mui/icons-material';
import RequestThesisModal from "./RequestThesisModal";
import {ThesisRequestContext} from "./ThesisRequestProvider";
import ThesisRequestStatus from "../types/ThesisRequestStatus";
import Student from "../types/Student";
import AcceptThesisModal from "./AcceptThesisModal";
import FrontendRoutes from "../types/FrontendRoutes";

function ProfessorList() {
    const {logout, getUserFn, user} = useContext(AuthContext);
    const {professors} = useContext(ProfessorContext);
    const [fadeIn, setFadeIn] = useState(false);
    const [showAll, setShowAll] = useState(false);
    const [showingProfessors, setShowingProfessors] =
        useState<Professor[] | undefined>([])
    const [searchName, setSearchName] = useState<string>('');

    const {
        thesisRequests,
        fetching,
        fetchingError,
        saving,
        savingError,
        saveThesisRequest
    } = useContext(ThesisRequestContext);

    useEffect(() => {
        setFadeIn(true);
    }, []);

    const navigate = useNavigate();
    const handleLogout = useCallback(async () => {
        logout?.();
        navigate(FrontendRoutes.LOGIN);
    }, []);

    const fetchData = () => {
        let auxData: Professor[] | undefined = [];
        console.log(professors)
        if (showAll) {
            auxData = professors
        } else {
            auxData = professors?.filter(
                (prof) => thesisRequests?.find(t => t.professorId == prof.professorId)
                        ?.status == ThesisRequestStatus.PROFESSOR_ACCEPTED  ||
                    prof.numberOfEnrolledStudents < prof.maximumNumberOfStudents
            );
        }
        auxData = auxData?.sort((p1, p2) => {
            const thesisP1 = thesisRequests?.find(t => t.professorId == p1.professorId);
            const thesisP2 = thesisRequests?.find(t => t.professorId == p2.professorId);
            if (!thesisP1 && !thesisP2) {
                return p1.name.localeCompare(p2.name);
            }
            else if (thesisP1 && !thesisP2) {
                return -1;
            }
            else if (!thesisP1 && thesisP2) {
                return 1;
            }
            else if (thesisP1 && thesisP2) {
                return thesisP1.status.localeCompare(thesisP2.status)
            }
            else return 1;
        })
        if (searchName !== '') {
            auxData = auxData?.filter((prof) =>
                prof.name.toLowerCase().includes(searchName) || prof.domainOfActivity.toLowerCase().includes(searchName));
        }
        console.log(auxData)
        setShowingProfessors(auxData)

    };

    useEffect(() => {
        if ((user as Student).graduationThesis) {
            navigate(FrontendRoutes.THESIS_PAGE)
        }
    }, [user]);

    useEffect(() => {
        fetchData()
    }, [professors]);

    useEffect(() => {
        fetchData()
    }, [thesisRequests]);

    useEffect(() => {
        fetchData()
    }, [showAll]);

    useEffect(() => {
        fetchData()
    }, [searchName]);

    return (
        <div className="background-container-student-page">
            <div className="professor-list-container">
                <h2 className="list-title">
                    Lista posibililor coordonatori de la Facultatea de Matematica si Informatica
                </h2>
                <h3 className="list-professor-props">
                    Departamentul: {(user as Student).facultyDepartment} -- Limba lucrari de licenta: {(user as Student).studyLanguage}
                </h3>
                <div className="searchBarProfessorsContainer">
                    <TextField
                        id="outlined"
                        onChange={(e) => {
                            setSearchName(e.target.value.toLowerCase());
                        }}
                        variant="outlined"
                        fullWidth
                        label="Cauta dupa nume sau domeniu de activitate"
                        autoComplete='off'
                    />
                </div>
                <div className="showAllButtonContainer">
                    <button className="showAllButton" onClick={() => setShowAll(!showAll)}>
                        {showAll ? 'Arata doar profesorii liberi' : 'Arata toti profesorii'}
                    </button>
                </div>
                <ul className={`professor-list ${fadeIn ? 'fade-in' : ''}`}>
                    {showingProfessors && thesisRequests && showingProfessors.map((professor, index) => {

                        const classNameItem = professor.numberOfEnrolledStudents < professor.maximumNumberOfStudents ||
                        thesisRequests.find((t) => t.professorId == professor.professorId)?.status == ThesisRequestStatus.PROFESSOR_ACCEPTED
                            ? "professor-item"
                            : "professor-item-full";

                        return (
                            <li key={index} className={`${classNameItem} ${fadeIn ? 'fade-in-item' : ''}`}>
                                <div className="professor-details">
                                    <h3>{professor.name}, {professor.title}</h3>
                                    <p>{professor.domainOfActivity}</p>
                                </div>
                                <div className="professor-occupation">
                                    <p>
                                        Locuri ocupate: {professor.numberOfEnrolledStudents} /{' '}
                                        {professor.maximumNumberOfStudents}
                                    </p>
                                </div>
                                {thesisRequests &&
                                    thesisRequests.find((t) => t.professorId == professor.professorId) == undefined &&
                                    professor.numberOfEnrolledStudents < professor.maximumNumberOfStudents &&
                                    <div className="requestThesisButtonContainer">
                                        <RequestThesisModal professorId={professor.professorId}/>
                                    </div>
                                }
                                {thesisRequests && thesisRequests.find((t) => t.professorId == professor.professorId) &&
                                    <div className="requestStatus">
                                        {thesisRequests.find((t) => t.professorId == professor.professorId)
                                                ?.status == ThesisRequestStatus.PENDING &&
                                            <button disabled={true} className="pendingRequestStatus">
                                                IN ASTEPTARE
                                            </button>}
                                        {thesisRequests.find((t) => t.professorId == professor.professorId)
                                                ?.status == ThesisRequestStatus.PROFESSOR_DECLINED &&
                                            <button disabled={true} className="declinedRequestStatus">
                                                RESPINS
                                            </button>}
                                        {thesisRequests.find((t) => t.professorId == professor.professorId)
                                                ?.status == ThesisRequestStatus.PROFESSOR_ACCEPTED &&
                                            <div className="acceptedRequestStatus">
                                                <AcceptThesisModal getUser={getUserFn}
                                                    professorId={professor.professorId}
                                                                   thesisRequestId={thesisRequests.find((t) => t.professorId == professor.professorId)
                                                                       ?.thesisRequestId}/>
                                            </div>}
                                    </div>
                                }
                            </li>)
                    })}
                </ul>
                <div className="butonLogoutContainer">
                    <Button variant="outlined" startIcon={<LogoutRounded/>} onClick={handleLogout}>
                        LogOut
                    </Button>
                </div>
            </div>
        </div>
    );
}

export default ProfessorList;