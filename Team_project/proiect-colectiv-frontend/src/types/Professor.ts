import Department from "./Department";
import Language from "./Language";

type Professor = {
    professorId: number;
    title: string;
    domainOfActivity: string;
    numberOfEnrolledStudents: number;
    maximumNumberOfStudents: number;
    name: string;
    department: Department;
    supportedLanguages: Language[]
}

export default Professor
