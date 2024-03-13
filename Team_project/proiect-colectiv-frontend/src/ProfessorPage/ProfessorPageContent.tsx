import React from 'react';
import StudentTable from "./StudentTable";
import ProfessorView from "./ProfessorView";
import {Route} from "react-router";
import {StudentProvider} from "../Professor/StudentProvider";
import "./styling/ProfessorPage.css"
import {Logout} from "./Logout";
import {Link} from "react-router-dom";
import EditNumberOfStudentsModal from "./EditNumberOfStudentsModal";

function ProfessorPageContent(){
    return (
        <div className="ProfessorPageContent">
            <StudentProvider>
                <ProfessorView/>
                <StudentTable/>
                <Logout/>
                <EditNumberOfStudentsModal />
            </StudentProvider>
        </div>
    );
}

export default ProfessorPageContent