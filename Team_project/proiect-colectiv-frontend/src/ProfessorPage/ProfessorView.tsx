import React, {useContext, useEffect, useState} from "react";
import {AuthContext} from "../auth/AuthProvider";
import professor from "../types/Professor";
import Professor from "../types/Professor";
import {getLogger} from "../core";
import {ProfessorContext} from "../Professor/StudentProvider";
import {Link} from "react-router-dom";

const log = getLogger("ProfessorView")

function ProfessorView() {
    const {professor} = useContext(ProfessorContext);
    log(professor);
    return (
        <div className="professor">
            {professor &&
                <div>
                    <h2>Bun venit, {professor.name}!</h2>
                    <h2>Locuri ocupate: {professor.numberOfEnrolledStudents} din {professor.maximumNumberOfStudents}</h2>
                </div>

            }
            <h3>Lista studentilor coordonati: </h3>
            <Link to={"/professor/requests"}>Cererile de alocare</Link>
        </div>
    );
};

export default ProfessorView;
