import Student from "./Student";

type StudentRequest = {
    thesisRequestId: number,
    studentId: number,
    studentInfo: Student
    professorId: number,
    initialStudentMessage: string,
    professorResponseMessage: string,
    requestSentAt: any,
    status: string,
}

export default StudentRequest