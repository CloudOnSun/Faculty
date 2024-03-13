// StudentView.tsx
import React from 'react';
import Student from "../types/Student";


function StudentView({ studentId,fullName,studentGroup, facultyDepartment, studyLanguage, email }: Student) {
    return (
        <tr className="Student" key={studentId}>
            <td>{fullName}</td>
            <td>{studentGroup}</td>
            <td>{facultyDepartment}</td>
            <td>{studyLanguage}</td>
            <td>{email}</td>
        </tr>
    );
}

export default StudentView;
