import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import FormControl from "@mui/material/FormControl";
import SaveIcon from "@mui/icons-material/Save";
import Paper from "@mui/material/Paper";
import recordsService from "../services/records.service";

const AddRecord = () => {
  const [recordType, setRecordType] = useState("");
  const [recordDate, setRecordDate] = useState("");
  const [toolId, setToolId] = useState("");
  const [clientId, setClientId] = useState("");
  const [loanId, setLoanId] = useState("");
  const [recordAmount, setRecordAmount] = useState("");
  const navigate = useNavigate();

  const saveRecord = (e) => {
    e.preventDefault();
    const record = {
      record_type: recordType,
      record_date: recordDate,
      tool_id: toolId,
      loan_id: loanId,
      client_id: clientId,
      record_amount: recordAmount,
    };
    console.log(record);
    recordsService
      .create(record)
      .then((response) => {
        console.log("Record añadido.", response.data);
        navigate("/record/list");
      })
      .catch((error) => {
        console.log("Ha ocurrido un error al intentar crear un nuevo record.", error);
      });
  };

  return (
    <Box sx={{ position: "relative", minHeight: "100vh" }}>
      {/* Fondo difuminado */}
      <Box
        sx={{
          position: "fixed",
          top: 0,
          left: 0,
          width: "100%",
          height: "100%",
          backgroundImage: `url("/fondo.jpg")`,
          backgroundSize: "cover",
          backgroundPosition: "center",
          backgroundRepeat: "no-repeat",
          filter: "blur(8px)",
          zIndex: 0,
        }}
      />
      {/* Frame del formulario */}
      <Box
        sx={{
          position: "relative",
          zIndex: 1,
          minHeight: "100vh",
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          justifyContent: "center",
        }}
      >
        <Paper
          elevation={6}
          sx={{
            p: 4,
            minWidth: 350,
            maxWidth: 450,
            width: "90%",
            background: "rgba(255,255,255,0.85)", // Cambia aquí el color/transparencia
            color: "#222", // Cambia aquí el color del texto
          }}
        >
          <Box
            display="flex"
            flexDirection="column"
            alignItems="center"
            justifyContent="center"
            component="form"
          >
            <h3>Nuevo Record</h3>
            <hr />
            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                id="recordType"
                label="Tipo de Record"
                value={recordType}
                variant="standard"
                onChange={(e) => setRecordType(e.target.value)}
              />
            </FormControl>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                id="recordDate"
                label="Fecha"
                type="date"
                value={recordDate}
                variant="standard"
                onChange={(e) => setRecordDate(e.target.value)}
                InputLabelProps={{ shrink: true }}
              />
            </FormControl>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                id="toolId"
                label="ID Herramienta"
                value={toolId}
                variant="standard"
                onChange={(e) => setToolId(e.target.value)}
              />
            </FormControl>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                id="clientId"
                label="ID Cliente"
                value={clientId}
                variant="standard"
                onChange={(e) => setClientId(e.target.value)}
              />
            </FormControl>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                id="loanId"
                label="ID Préstamo"
                value={loanId}
                variant="standard"
                onChange={(e) => setLoanId(e.target.value)}
              />
            </FormControl>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                id="recordAmount"
                label="Monto"
                type="number"
                value={recordAmount}
                variant="standard"
                onChange={(e) => setRecordAmount(e.target.value)}
              />
            </FormControl>
            <FormControl sx={{ mb: 2 }}>
              <Button
                variant="contained"
                color="info"
                onClick={saveRecord}
                startIcon={<SaveIcon />}
              >
                Grabar
              </Button>
            </FormControl>
            <hr />
            <Link to="/record/list">Back to List</Link>
          </Box>
        </Paper>
      </Box>
    </Box>
  );
};

export default AddRecord;
