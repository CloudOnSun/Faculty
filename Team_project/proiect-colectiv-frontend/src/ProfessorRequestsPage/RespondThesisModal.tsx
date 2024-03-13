import Button from "@mui/material/Button";
import Modal from "@mui/material/Modal";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import TextField from "@mui/material/TextField";
import {Send} from "@mui/icons-material";
import * as React from "react";
import {useCallback, useContext, useState} from "react";
import {AuthContext} from "../auth/AuthProvider";
import {saveStudentResponse} from "./professor-requests-api";
import ThesisRequestStatus from "../types/ThesisRequestStatus";

type RespondThesisModalProps = {
    requestId: number,
    changeStatus?: (requestId: number, status: ThesisRequestStatus) => void
    updateUser: () => Promise<void>
}

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

const RespondThesisModal = (props: RespondThesisModalProps) => {
    const {authenticationToken} = useContext(AuthContext);

    const [open, setOpen] = React.useState(false);
    const [requestMessage, setRequestMessage] = useState("");
    const [error, setError] = useState<string>("");

    const handleOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);

    const validateRequestMessage = useCallback((requestMessage: string) => {
        return requestMessage.length < 100 && requestMessage.length > 1
    }, [])

    const approve = async () => {
        if (!validateRequestMessage(requestMessage)) {
            setError("Mesaj de cerere invalid!");
            return;
        }

        try {
            saveStudentResponse && await saveStudentResponse(authenticationToken, props.requestId, requestMessage, "PROFESSOR_ACCEPTED");
            props.updateUser()
            console.log("din modal s a facut update")
        }
        catch (eroare) {
            setError("Ceva nu a mers bine!")
        }
        handleClose();
        props.changeStatus && props.changeStatus(props.requestId, ThesisRequestStatus.PROFESSOR_ACCEPTED);
    }

    const deny = () => {
        if (!validateRequestMessage(requestMessage)) {
            setError("Mesaj de cerere invalid!");
            return;
        }

        saveStudentResponse && saveStudentResponse(authenticationToken, props.requestId, requestMessage, "PROFESSOR_DECLINED");
        handleClose();
        props.changeStatus && props.changeStatus(props.requestId, ThesisRequestStatus.PROFESSOR_DECLINED);
    }

    const handleSendRequest = useCallback(async (event: any) => {
        event.preventDefault();
    }, [requestMessage])

    return (
        <div className="requestThesisModalContainer">
            <Button onClick={handleOpen}>Raspunde</Button>
            <Modal
                open={open}
                onClose={handleClose}
                aria-labelledby="modal-modal-title"
                aria-describedby="modal-modal-description"
            >
                <Box sx={style}>
                    <Typography id="request-thesis-modal-title" variant="h6" component="h2">
                        Send a response to the student
                    </Typography>
                    <form onSubmit={handleSendRequest}>
                        <TextField
                            className="requestThesisMessageInput"
                            type="text"
                            label="Mesaj"
                            variant="outlined"
                            value={requestMessage}
                            onChange={(event) => setRequestMessage(event.target.value)}
                            fullWidth
                            margin="normal"
                            InputProps={{classes: {root: 'mui-input-root'}}}
                        />
                        <Button type="submit" onClick={approve} variant="outlined" startIcon={<Send/>}>
                            Approve
                        </Button>
                        <Button type="submit" onClick={deny} variant="outlined" startIcon={<Send/>}>
                            Deny
                        </Button>
                    </form>
                    <span>{error}</span>
                </Box>
            </Modal>
        </div>
    );
}

export default RespondThesisModal;