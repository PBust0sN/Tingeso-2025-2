import { useState, useEffect } from "react";
import { Link, useParams, useNavigate } from "react-router-dom";
import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import FormControl from "@mui/material/FormControl";
import SaveIcon from "@mui/icons-material/Save";
import toolsService from "../services/tools.service";
import Paper from "@mui/material/Paper";

const EditTool = () => {
  const [category, setCategory] = useState("");
  const [disponibility, setDisponibility] = useState("");
  const [initialState, setInitialState] = useState("");
  const [toolName, setToolName] = useState("");
  const [loanFee, setLoanFee] = useState("");
  const [repositionFee, setRepositionFee] = useState("");
  const [diaryFineFee, setDiaryFineFee] = useState("");
  const { toolId } = useParams();
  const navigate = useNavigate();

  useEffect(() => {
    if (toolId) {
      toolsService
        .get(toolId)
        .then((tool) => {
          setCategory(tool.data.category);
          setDisponibility(tool.data.disponibility);
          setInitialState(tool.data.initialState);
          setToolName(tool.data.toolName);
          setLoanFee(tool.data.loanFee);
          setRepositionFee(tool.data.repositionFee);
          setDiaryFineFee(tool.data.diaryFineFee);
        })
        .catch((error) => {
          console.log("Se ha producido un error.", error);
        });
    }
  }, [toolId]);

  const saveTool = (e) => {
    e.preventDefault();
    const tool = { toolId, toolName, category, diaryFineFee, loanFee, repositionFee, initialState, disponibility };
    toolsService
      .update(tool)
      .then((response) => {
        console.log("Herramienta actualizada.", response.data);
        navigate("/tool/list");
      })
      .catch((error) => {
        console.log("Ha ocurrido un error al intentar actualizar la herramienta.", error);
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
            background: "rgba(255,255,255,0.85)", // Cambia el color y la transparencia aquí
            color: "#222", // Cambia el color del texto aquí
          }}
        >
          <Box
            display="flex"
            flexDirection="column"
            alignItems="center"
            justifyContent="center"
            component="form"
          >
            <h3>Editar Herramienta</h3>
            <hr />
            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                id="toolName"
                label="Tool Name"
                value={toolName}
                variant="standard"
                onChange={(e) => setToolName(e.target.value)}
                helperText="Ej. Martillo"
              />
            </FormControl>

            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                id="category"
                label="Category"
                value={category}
                variant="standard"
                onChange={(e) => setCategory(e.target.value)}
              />
            </FormControl>

            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                id="disponibility"
                label="Disponibility"
                value={disponibility}
                variant="standard"
                onChange={(e) => setDisponibility(e.target.value)}
              />
            </FormControl>

            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                id="initialState"
                label="Initial State"
                value={initialState}
                variant="standard"
                onChange={(e) => setInitialState(e.target.value)}
              />
            </FormControl>

            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                id="loanFee"
                label="Loan Fee"
                type="number"
                value={loanFee}
                variant="standard"
                onChange={(e) => setLoanFee(e.target.value)}
              />
            </FormControl>

            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                id="repositionFee"
                label="Reposition Fee"
                type="number"
                value={repositionFee}
                variant="standard"
                onChange={(e) => setRepositionFee(e.target.value)}
              />
            </FormControl>

            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                id="diaryFineFee"
                label="Diary Fine Fee"
                type="number"
                value={diaryFineFee}
                variant="standard"
                onChange={(e) => setDiaryFineFee(e.target.value)}
              />
            </FormControl>

            <FormControl sx={{ mb: 2 }}>
              <Button
                variant="contained"
                color="info"
                onClick={saveTool}
                startIcon={<SaveIcon />}
              >
                Grabar
              </Button>
            </FormControl>
            <hr />
            <Link to="/tool/list">Back to List</Link>
          </Box>
        </Paper>
      </Box>
    </Box>
  );
};

export default EditTool;
