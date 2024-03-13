import {Button, Container, Dialog, DialogActions, DialogContent, DialogTitle, Grid, Paper} from "@mui/material";
import {LogoutRounded} from "@mui/icons-material";
import {useCallback, useContext, useEffect, useState} from "react";
import {AuthContext} from "../auth/AuthProvider";
import {useNavigate} from "react-router-dom";
import FrontendRoutes from "../types/FrontendRoutes";
import Student from "../types/Student";
import Typography from "@mui/material/Typography";
import Professor from "../types/Professor";
import {getUser} from "../auth/auth-api";
import UserRole from "../types/UserRole";
import TextField from "@mui/material/TextField";
import {changeThesisTitleApi, ChangeThesisTitlePayload} from "../StudentPage/professor-requests-api";

type ChangeThesisTile =(thesisTitle: string, thesisId:number) => Promise<any>;


function ThesisPage () {
    const {logout, user, authenticationToken} = useContext(AuthContext);
    const [student, setStudent] = useState<Student>()
    const [openDialog, setOpenDialog] = useState(false);
    const [newThesisTitle, setNewThesisTitle] = useState("");

    useEffect(() => {
        setStudent((user as Student))
    }, [user]);

    const [professor, setProfessor] = useState<Professor>()

    useEffect(  () => {
        if (student && student.graduationThesis) {
            getUser(
                authenticationToken,
                UserRole.PROFESSOR,
                student.graduationThesis.professorCoordinatorId).then((value => {
                    if (value) {
                        setProfessor(value.data as Professor)
                    }
                }))
        }
    }, [student]);

    const navigate = useNavigate();
    const handleLogout = useCallback(async () => {
        logout?.();
        navigate(FrontendRoutes.LOGIN);
    }, []);
    const handleOpenDialog = () => {
        setOpenDialog(true);
    };

    const handleCloseDialog = () => {
        setOpenDialog(false);
        // Clear the input field when the dialog is closed
        setNewThesisTitle("");
    };

    async function changeThesisTitleCallback(thesisTitle: string, thesisId:number) {
            if (thesisId && thesisTitle) {
                const changedTitleThesis = await changeThesisTitleApi(authenticationToken,thesisId,thesisTitle)
                student && setStudent({...student,graduationThesis:changedTitleThesis.data})
            }
    }

    const changeThesisTitle=useCallback<ChangeThesisTile>(changeThesisTitleCallback,[newThesisTitle,authenticationToken])

    const handleChangeThesisTitle = () => {
        // Implement logic to update the thesis title in the backend
        // You may want to call an API endpoint to update the title
        // and then update the local state accordingly
        console.log("New Thesis Title:", newThesisTitle);
        student?.graduationThesis && changeThesisTitle(newThesisTitle,student?.graduationThesis?.id)
        // Close the dialog after handling the change
        handleCloseDialog();
    };
    return (
        <div className="background-container-student-page">
            <Container>
                <Grid container spacing={3}>
                    <Grid item xs={12}>
                        <div className="butonLogoutContainer">
                            <Button
                                variant="outlined"
                                startIcon={<LogoutRounded />}
                                onClick={handleLogout}
                            >
                                Log Out
                            </Button>
                        </div>
                    </Grid>
                    {student && student.graduationThesis && (
                        <Grid item xs={12} md={6}>
                            <Paper
                                elevation={3}
                                style={{
                                    padding: 16,
                                    backgroundColor: "#f0f8ff", // Light Blue
                                    borderRadius: 10,
                                }}
                            >
                                <Typography variant="h6" gutterBottom>
                                    Detalii Lucrare de Licenta
                                </Typography>
                                <Typography variant="subtitle1">
                                    Titlul lucrarii: {student.graduationThesis?.thesisTitle || "N/A"}
                                </Typography>
                                <Typography variant="subtitle1">
                                    Profesor: {professor?.name || "N/A"}
                                </Typography>
                                <Button
                                    variant="contained"
                                    color="primary"
                                    onClick={handleOpenDialog}
                                    style={{ marginTop: 16 }}
                                >
                                    Schimba Titlul Lucrarii
                                </Button>
                            </Paper>
                        </Grid>
                    )}
                    {student && (
                        <Grid item xs={12} md={6}>
                            <Paper
                                elevation={3}
                                style={{
                                    padding: 16,
                                    backgroundColor: "#e6e6fa", // Lavender
                                    borderRadius: 10,
                                }}
                            >
                                <Typography variant="h6" gutterBottom>
                                    Informatii Student
                                </Typography>
                                <Typography variant="subtitle1">Numele: {student.fullName}</Typography>
                                <Typography variant="subtitle1">Grupa: {student.studentGroup}</Typography>
                                <Typography variant="subtitle1">Departamentul: {student.facultyDepartment}</Typography>
                                <Typography variant="subtitle1">Limba de predare: {student.studyLanguage}</Typography>
                                {/* Add more student details as needed */}
                            </Paper>
                        </Grid>
                    )}
                </Grid>
            </Container>

            {/* Dialog for changing thesis title */}
            <Dialog open={openDialog} onClose={handleCloseDialog}>
                <DialogTitle>Schimba Titlul Lucrarii</DialogTitle>
                <DialogContent>
                    <TextField
                        label="Noul Titlu"
                        fullWidth
                        value={newThesisTitle}
                        onChange={(e) => setNewThesisTitle(e.target.value)}
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleCloseDialog} color="secondary">
                        Anuleaza
                    </Button>
                    <Button onClick={handleChangeThesisTitle} color="primary">
                        Salveaza
                    </Button>
                </DialogActions>
            </Dialog>
        </div>
    );
}

export default ThesisPage;