import * as React from "react";
import {useCallback, useContext, useEffect, useState} from "react";
import {AuthContext, GetUserFn} from "../auth/AuthProvider";
import {ThesisRequestContext} from "./ThesisRequestProvider";
import Button from "@mui/material/Button";
import Modal from "@mui/material/Modal";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import TextField from "@mui/material/TextField";
import {Send} from "@mui/icons-material";
import ThesisRequestStatus from "../types/ThesisRequestStatus";
import {useNavigate} from "react-router-dom";
import FrontendRoutes from "../types/FrontendRoutes";
import {createThesis} from "./professor-requests-api";

const style = {
    position: 'absolute' as 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: 400,
    bgcolor: 'background.paper',
    border: '2px solid #000',
    boxShadow: 24,
    p: 4,
};

interface AcceptThesisModalProps {
    getUser: GetUserFn | undefined,
    professorId: number,
    thesisRequestId?: number,
}

export default function AcceptThesisModal({getUser, professorId, thesisRequestId}: AcceptThesisModalProps) {
    const {authenticationToken} = useContext(AuthContext);
    const {thesisRequests, saveThesisRequest, savingError} = useContext(ThesisRequestContext);

    const [open, setOpen] = React.useState(false);
    const [error, setError] = useState<string>("");


    const handleOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);

    const navigate = useNavigate();

    useEffect(() => {
        if (savingError)
            setError("Something went wrong!")
        else
            setError("")
    }, [savingError]);

    useEffect(() => {
        handleClose()
    }, [thesisRequests]);

    const handleAccept = useCallback(async (event: any) => {
        event.preventDefault();
        saveThesisRequest && await saveThesisRequest({
            nextStatus: ThesisRequestStatus.STUDENT_ACCEPTED,
            thesisRequestId: thesisRequestId
        })
        await createThesis(authenticationToken, professorId, "NECOMPLETAT")
        window.location.reload();
        navigate(FrontendRoutes.THESIS_PAGE);
    }, [thesisRequestId])

    //TODO: change css for span from auth -- it's global

    return (
        <div className="requestThesisModalContainer">
            <Button onClick={handleOpen}>Confirma cerere</Button>
            <Modal
                open={open}
                onClose={handleClose}
                aria-labelledby="modal-modal-title"
                aria-describedby="modal-modal-description"
            >
                <Box sx={style}>
                    <Typography id="request-thesis-modal-title" variant="h6" component="h2">
                        Profesorul a acceptat cererea de licenta. Confirma cererea.
                    </Typography>
                    <Button onClick={handleAccept} variant="outlined" startIcon={<Send/>}>
                        CONFIRMA
                    </Button>
                    <span>{error}</span>
                </Box>
            </Modal>
        </div>
    );
}