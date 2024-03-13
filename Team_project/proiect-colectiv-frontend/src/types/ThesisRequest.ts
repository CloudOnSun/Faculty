import ThesisRequestStatus from "./ThesisRequestStatus";
import Student from "./Student";

type ThesisRequest = {
    thesisRequestId: number,
    studentId: number,
    professorId: number,
    initialStudentMessage: string,
    professorResponseMessage: string,
    requestSentAt: Date,
    status: ThesisRequestStatus,
    studentInfo: Student
}

export default ThesisRequest