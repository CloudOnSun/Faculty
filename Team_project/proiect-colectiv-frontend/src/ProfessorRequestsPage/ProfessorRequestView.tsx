import ThesisRequestStatus from "../types/ThesisRequestStatus";
import Student from "../types/Student";
import RespondThesisModal from "./RespondThesisModal";
import Professor from "../types/Professor";

type ProfessorRequestViewProps = {
    student: Student;
    message: string;
    index: number;
    fadeIn: boolean;
    requestId: number;
    myMessage: string;
    status: ThesisRequestStatus;
    changeStatus?: (requestId: number, status: ThesisRequestStatus) => void
    updateUser: () => Promise<void>
    professor: Professor
}

const ProfessorRequestView = (props: ProfessorRequestViewProps) => {

    return (
        <div>
            <li key={props.index} className={`professor-item ${props.fadeIn ? 'fade-in-item' : ''}`}>
                <div className="professor-details">
                    <h3>{props.student.fullName}, {props.student.studentGroup}</h3>
                    <p>{props.student.facultyDepartment}, {props.student.studyLanguage}</p>
                </div>
                <div className="professor-occupation">
                    <p>
                        Mesaj student: {props.message}
                    </p>
                    {props.myMessage && props.myMessage != "" && <p>
                        Raspuns: {props.myMessage}
                    </p>}
                </div>
                {props.status == ThesisRequestStatus.PENDING && props.professor.numberOfEnrolledStudents < props.professor.maximumNumberOfStudents &&
                    <div className="requestThesisButtonContainer">
                        <RespondThesisModal requestId={props.requestId} changeStatus={props.changeStatus} updateUser={props.updateUser}/>
                    </div>
                }
                {
                    <div className="requestStatus">
                        {props.status == ThesisRequestStatus.PROFESSOR_ACCEPTED &&
                            <button disabled={true} className="pendingRequestStatus">
                                Asteapta dupa student
                            </button>}
                        {props.status == ThesisRequestStatus.PROFESSOR_DECLINED &&
                            <button disabled={true} className="declinedRequestStatus">
                                RESPINS
                            </button>}
                        {props.status == ThesisRequestStatus.STUDENT_ACCEPTED &&
                            <button disabled={true} className="acceptedRequestStatus">
                                ACCEPTAT
                            </button>}
                        {props.status == ThesisRequestStatus.STUDENT_DECLINED &&
                            <button disabled={true} className="declinedRequestStatus">
                                Respins de student
                            </button>}
                        {props.status == ThesisRequestStatus.OUTDATED &&
                            <button disabled={true} className="declinedRequestStatus">
                                Invechita
                            </button>}
                    </div>
                }
            </li>
        </div>
    )
};

export default ProfessorRequestView;