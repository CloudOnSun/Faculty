import {useCallback, useContext, useEffect, useState} from "react";
import './styling-professor-requests-page/professor-requests-page.css';
import {useNavigate} from "react-router-dom";
import {AuthContext} from "../auth/AuthProvider";
import {getStudentRequests} from "./professor-requests-api";
import {Button} from "@mui/material";
import {ArrowBackRounded, LogoutRounded, RefreshRounded} from "@mui/icons-material";
import ProfessorRequestView from "./ProfessorRequestView";
import ThesisRequestStatus from "../types/ThesisRequestStatus";
import ThesisRequest from "../types/ThesisRequest";
import FrontendRoutes from "../types/FrontendRoutes";
import Professor from "../types/Professor";
import {getUser} from "../auth/auth-api";
import UserRole from "../types/UserRole";


const ProfessorRequestsPage = () => {
    // const {requests} = useContext(RequestsContext);
    const {logout, authenticationToken, user} = useContext(AuthContext);
    const [fadeIn, setFadeIn] = useState(false);
    const [requests, setRequests] = useState<ThesisRequest[] | undefined>([])
    const [professor, setProfessor] = useState<Professor>()

    useEffect(() => {
        setProfessor(user as Professor)
    }, [user]);

    useEffect(() => {
        setFadeIn(true);
    }, []);

    const updateUser = useCallback(async () => {
        if (professor) {
            try {
                const userResponse = await getUser(authenticationToken, UserRole.PROFESSOR, professor.professorId)
                console.log("se schimba profesorul")
                console.log(userResponse)
                if (userResponse) {
                    setProfessor(userResponse.data as Professor)
                }
            } catch (err) {

            }
        }
    }, [professor])

    const navigate = useNavigate();
    const handleLogout = useCallback(async () => {
        logout?.();
        navigate(FrontendRoutes.LOGIN);
    }, []);

    const fetchData = async () => {
        const response = await getStudentRequests(authenticationToken);

        const getStatusPriority = (status: ThesisRequestStatus) => {
            switch (status) {
                case ThesisRequestStatus.PENDING:
                    return 0;
                case ThesisRequestStatus.PROFESSOR_ACCEPTED:
                    return 1;
                case ThesisRequestStatus.PROFESSOR_DECLINED:
                    return 3;
                case ThesisRequestStatus.STUDENT_DECLINED:
                    return 3;
                case ThesisRequestStatus.STUDENT_ACCEPTED:
                    return 2;
                default:
                    return 4;
            }
        }

        const sortedRequests = response.data.sort((r1, r2) => {
            return getStatusPriority(r1.status) - getStatusPriority(r2.status);
        });

        setRequests(sortedRequests);
    };

    useEffect(() => {
        fetchData()
    }, []);

    const changeStatus = (requestId: number, status: ThesisRequestStatus) => {
        const newRequests = requests?.map((request) => {
            if (request.thesisRequestId == requestId) {
                request.status = status;
            }
            return request;
        })
        setRequests(newRequests);
    }

    return (
        <div className="background-container-student-page">
            <div className="professor-list-container">
                <h2 className="list-title">
                    Lista cererilor de coordonare a lucrarilor
                </h2>
                {professor && professor.numberOfEnrolledStudents == professor.maximumNumberOfStudents &&
                    <div className="professor-full-div">
                        Nu se mai pot accepta licente
                    </div>
                }
                <ul className={`professor-list ${fadeIn ? 'fade-in' : ''}`}>
                    {requests && professor && requests.map((thesisRequest: ThesisRequest, index: number) => (
                        <ProfessorRequestView
                            key={index}
                            student={thesisRequest.studentInfo}
                            index={index}
                            fadeIn={fadeIn}
                            message={thesisRequest.initialStudentMessage}
                            requestId={thesisRequest.thesisRequestId}
                            myMessage={thesisRequest.professorResponseMessage}
                            status={thesisRequest.status as ThesisRequestStatus}
                            changeStatus={changeStatus}
                            updateUser={updateUser}
                            professor={professor}
                        />
                    ))}
                </ul>
                <div className="butonLogoutContainer">
                    <Button variant="outlined" startIcon={<RefreshRounded/>} onClick={() => {
                        fetchData()
                    }}>Refresh</Button>
                    <Button variant="outlined" startIcon={<ArrowBackRounded/>} onClick={() => {
                        window.location.reload()
                        navigate(FrontendRoutes.PROFESSOR_PAGE)
                    }}>Inapoi</Button>
                    <Button variant="outlined" startIcon={<LogoutRounded/>} onClick={handleLogout}>
                        LogOut
                    </Button>
                </div>
            </div>
        </div>
    )
}

export default ProfessorRequestsPage;