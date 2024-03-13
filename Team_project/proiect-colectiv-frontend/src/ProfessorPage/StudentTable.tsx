// StudentTable.tsx
import React, {useContext} from 'react';
import {ProfessorContext} from "../Professor/StudentProvider";
import StudentView from "./StudentView";

function StudentTable(){
    const { students, fetching, fetchingError } = useContext(ProfessorContext);
    return (
        <div className="StudentList">
            <table className="StudentsTable">
                <thead>
                <tr>
                    <th>Nume</th>
                    <th>Grupa</th>
                    <th>Departament</th>
                    <th>Limba de studiu</th>
                    <th>Email</th>
                </tr>
                </thead>
                <tbody>
                {students &&(students.map(student => (
                        <StudentView key={student.studentId} studentId={student.studentId} fullName={student.fullName}
                                     studentGroup={student.studentGroup} studyLanguage={student.studyLanguage}
                                     email={student.email} facultyDepartment={student.facultyDepartment}/>
                )))}
                </tbody>
            </table>
        </div>
    );
}

export default StudentTable;
