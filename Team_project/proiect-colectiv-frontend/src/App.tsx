import React from 'react';
import './App.css';
import ProfessorList from "./StudentPage/StudentPage";
import {ProfessorProvider} from "./StudentPage/ProfessorProvider";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import Login from "./auth/Login";
import Home from "./containers/Home";
import NotFound from "./containers/NotFound";
import {AuthProvider} from "./auth/AuthProvider";
import PrivateRoute from "./auth/PrivateRoute";
import ProfessorPageContent from "./ProfessorPage/ProfessorPageContent";
import {ThesisRequestProvider} from "./StudentPage/ThesisRequestProvider";
import ProfessorRequestsPage from "./ProfessorRequestsPage/ProfessorRequestsPage";
import ThesisPage from "./ThesisPage/ThesisPage";
import FrontendRoutes from "./types/FrontendRoutes";

function App() {
    return (
        <BrowserRouter>
            <AuthProvider>
                <ProfessorProvider>
                    <ThesisRequestProvider>
                        <Routes>
                            <Route path={FrontendRoutes.HOME} element={<PrivateRoute children={<Home/>}/>}/>
                            <Route path={FrontendRoutes.LOGIN} element={<Login/>}/>
                            <Route path="*" element={<NotFound/>}/>
                            <Route path={FrontendRoutes.STUDENT_PAGE} element={<PrivateRoute children={<ProfessorList/>}/>}/>
                            <Route path={FrontendRoutes.PROFESSOR_REQUESTS_PAGE} element={
                                <PrivateRoute children={<ProfessorRequestsPage/>}/>}
                            />
                            <Route path={FrontendRoutes.PROFESSOR_PAGE} element={
                                <PrivateRoute children={<ProfessorPageContent/>}/>}
                            />
                            <Route path={FrontendRoutes.THESIS_PAGE} element={
                                <PrivateRoute children={<ThesisPage/>}/>}
                            />
                        </Routes>
                    </ThesisRequestProvider>
                </ProfessorProvider>
            </AuthProvider>
        </BrowserRouter>
    )
}

export default App;
