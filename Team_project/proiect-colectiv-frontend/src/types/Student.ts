import Language from "./Language";
import Department from "./Department";
import GraduationThesis from "./GraduationThesis";

type Student  = {
    studentId: number;
    fullName: string
    studentGroup: number;
    email: string;
    facultyDepartment: Department;
    studyLanguage: Language;
    graduationThesis? : GraduationThesis;
}

export default Student
