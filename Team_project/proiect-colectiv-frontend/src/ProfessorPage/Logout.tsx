import React, {useCallback, useContext} from "react";
import {AuthContext} from "../auth/AuthProvider";
import {useNavigate} from "react-router-dom";
import FrontendRoutes from "../types/FrontendRoutes";
export function Logout(){
    const {logout}=useContext(AuthContext);
    const navigate=useNavigate();
    const handleLogout = useCallback(async() =>{
      logout?.();
      navigate(FrontendRoutes.LOGIN);
    },[]);

    return(
        <div className="buttonLogoutContainer">
            <button className="buttonLogOutProfessors" onClick={handleLogout}>
                Logout
            </button>
        </div>
    )
}