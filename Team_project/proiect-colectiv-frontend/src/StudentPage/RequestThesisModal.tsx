import * as React from 'react';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import Modal from '@mui/material/Modal';
import {Form} from "react-router-dom";
import {useCallback, useContext, useEffect, useState} from "react";
import TextField from "@mui/material/TextField";
import {LogoutRounded, Send} from "@mui/icons-material";
import {sendThesisRequest} from "./professor-requests-api";
import {AuthContext} from "../auth/AuthProvider";
import ThesisRequest from "../types/ThesisRequest";
import {ThesisRequestContext} from "./ThesisRequestProvider";

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

interface RequestThesisModalProps {
    professorId: number
}

export default function RequestThesisModal({professorId}: RequestThesisModalProps) {
    const {authenticationToken} = useContext(AuthContext);
    const {thesisRequests, saveThesisRequest, savingError} = useContext(ThesisRequestContext);

    const [open, setOpen] = React.useState(false);
    const [requestMessage, setRequestMessage] = useState("");
    const [error, setError] = useState<string>("");


    const handleOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);

    useEffect(() => {
        if (savingError)
            setError("Something went wrong!")
        else
            setError("")
    }, [savingError]);

    useEffect(() => {
        handleClose()
    }, [thesisRequests]);

    const validateRequestMessage = useCallback((requestMessage: string) => {
        return requestMessage.length < 100 && requestMessage.length > 1
    }, [])

    const handleSendRequest = useCallback(async (event: any) => {
        event.preventDefault();
        if (!validateRequestMessage(requestMessage)) {
            setError("Mesaj de cerere invalid!");
            return;
        }

        saveThesisRequest && saveThesisRequest({professorId, studentInitialMessage: requestMessage})
    }, [professorId, requestMessage])

    //TODO: change css for span from auth -- it's global

    return (
        <div className="requestThesisModalContainer">
            <Button onClick={handleOpen}>Trimite cerere</Button>
            <Modal
                open={open}
                onClose={handleClose}
                aria-labelledby="modal-modal-title"
                aria-describedby="modal-modal-description"
            >
                <Box sx={style}>
                    <Typography id="request-thesis-modal-title" variant="h6" component="h2">
                        Trimite o cerere de licenta!
                    </Typography>
                    <form onSubmit={handleSendRequest}>
                        <TextField
                            className="requestThesisMessageInput"
                            type="text"
                            label="Mesaj"
                            variant="outlined"
                            value={requestMessage}
                            onChange={(event: { target: { value: React.SetStateAction<string>; }; }) => setRequestMessage(event.target.value)}
                            fullWidth
                            margin="normal"
                            InputProps={{classes: {root: 'mui-input-root'}}}
                        />
                        <Button type="submit" variant="outlined" startIcon={<Send/>}>
                            TRIMITE CERERE
                        </Button>
                    </form>
                    <span>{error}</span>
                </Box>
            </Modal>
        </div>
    );
}
