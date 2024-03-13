import * as React from 'react';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Modal from '@mui/material/Modal';
import {Form} from "react-router-dom";
import {useCallback, useContext, useEffect, useState} from "react";
import TextField from "@mui/material/TextField";
import {AddBox, Label, LogoutRounded, Send, Close} from "@mui/icons-material";
import {AuthContext} from "../auth/AuthProvider";
import {IconButton, InputLabel} from "@mui/material";

import {ProfessorContext, UpdateMaxNumberOfStudents} from "../Professor/StudentProvider";
import {updateMaxNumberOfStudents} from "../Professor/StudentApi";

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


export default function EditNumberOfStudentsModal() {
    const {authenticationToken} = useContext(AuthContext);
    const {professor, updateMaxNumberOfStudentsFn} = useContext(ProfessorContext);


    const [open, setOpen] = React.useState(false);
    const [requestMessage, setRequestMessage] = useState("");
    const [error, setError] = useState<string>("");
    const [numberOfIncreasedStudents, setNumberOfIncreasedStudents] = React.useState(1);

    const handleOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);

    const validateNumberOfStudents = useCallback((requestMessage: string) => {
        return numberOfIncreasedStudents > 0
    }, [])

    const handleInputChange = useCallback((value:number)=>{
        setNumberOfIncreasedStudents(value)
    },[])

    const handleSendRequest = useCallback(async (event: any) => {
        event.preventDefault();
        if (!validateNumberOfStudents(requestMessage)) {
            setError("Numar de studenti invalid!");
            return;
        }
        try {
            updateMaxNumberOfStudentsFn && await updateMaxNumberOfStudentsFn(numberOfIncreasedStudents);
        }catch (e){
            setError("Nu am putut trimite");
        }
    }, [requestMessage,numberOfIncreasedStudents])

    return (
        <div className="editNumberOfStudentsModal">
            <div className="editNumberOfStudentsButton">
                <Button onClick={handleOpen} color="inherit">Editeaza Numarul de locuri</Button>
            </div>
            <Modal
                open={open}
                onClose={handleClose}
                aria-labelledby="modal-modal-title"
                aria-describedby="modal-modal-description"
            >
                <Box sx={style}>
                    <div>
                        Numarul curent de studenti: {professor?.numberOfEnrolledStudents}
                        <br/>
                        Numarul maxim de studenti: {professor?.maximumNumberOfStudents}
                    </div>
                    <IconButton onClick={handleClose}
                                aria-label="close"
                                sx={{
                                    position: 'absolute',
                                    top: 10,
                                    right: 10,
                                    backgroundColor: '#f44336',
                                    color: '#fff',
                                    '&:hover': {
                                        backgroundColor: '#d32f2f',
                                    },
                                }}>
                        <Close/>
                    </IconButton>
                    <form onSubmit={handleSendRequest}>
                        <TextField
                            className="editNumberOfStudents"
                            type="number"
                            value={numberOfIncreasedStudents}
                            label="Numarul de locuri de adaugat"
                            variant="outlined"
                            fullWidth
                            size="medium"
                            margin="normal"
                            autoFocus
                            onChange={(event: { target: { value: string | number; }; }) => {
                                handleInputChange(+event.target.value)
                            }}


                            InputProps={{
                                classes: {root: 'mui-input-root'},
                                style: {textAlign: 'center'},
                                inputProps: { min: 1}
                            }}
                        />
                        <Button type="submit" variant="outlined" startIcon={<Send/>}>
                            Trimite
                        </Button>
                    </form>
                    <span>{error}</span>
                </Box>
            </Modal>
        </div>
    );
}
