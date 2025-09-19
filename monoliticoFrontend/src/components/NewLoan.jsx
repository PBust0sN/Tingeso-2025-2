import { useState, useEffect } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import FormControl from "@mui/material/FormControl";
import SaveIcon from "@mui/icons-material/Save";
import Paper from "@mui/material/Paper";
import MenuItem from "@mui/material/MenuItem";
import toolsService from "../services/tools.service";
import loansService from "../services/loans.service";
import { useKeycloak } from "@react-keycloak/web";

const NewLoan = () => {
  const { client_id } = useParams();
  const [days, setDays] = useState("");
  const [toolOptions, setToolOptions] = useState([]);
  const [selectedTools, setSelectedTools] = useState([""]);
  const navigate = useNavigate();
  const { keycloak } = useKeycloak();
  const staff_id = Number(keycloak.tokenParsed?.acr);
  const clientNumber = Number(client_id); 

  // Cargar herramientas desde el backend
  useEffect(() => {
    toolsService
      .getAll()
      .then((response) => {
        setToolOptions(response.data);
      })
      .catch((error) => {
        console.log("Error al obtener herramientas:", error);
      });
  }, []);

  // Agregar otra caja de selección
  const handleAddToolSelect = () => {
    setSelectedTools([...selectedTools, ""]);
  };

  // Cambiar valor de una caja de selección
  const handleToolChange = (index, value) => {
    const newSelectedTools = [...selectedTools];
    newSelectedTools[index] = value;
    setSelectedTools(newSelectedTools);
  };

  const saveLoan = (e) => {
    e.preventDefault();
    const loan = {
      staff_id,
      client_id: clientNumber,
      days: Number(days),
      tools_id: selectedTools.filter(Boolean),
    };
    console.log(loan);
    loansService
      .newLoan(loan)
      .then((response) => {
        console.log("prestamo añadido.", response.data);
        navigate("/loan/list");
      })
      .catch((error) => {
        console.log("Ha ocurrido un error al intentar crear nuevo prestamo.", error);
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
            background: "rgba(255,255,255,0.85)",
            color: "#222",
          }}
        >
          <Box
            display="flex"
            flexDirection="column"
            alignItems="center"
            justifyContent="center"
            component="form"
          >
            <h3>Nuevo Préstamo</h3>
            <hr />
            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                id="days"
                label="Días"
                type="number"
                value={days}
                variant="standard"
                onChange={(e) => setDays(e.target.value)}
                helperText="Cantidad de días del préstamo"
              />
            </FormControl>
            {/* Desplegables dinámicos para herramientas */}
            {selectedTools.map((selectedTool, idx) => (
              <FormControl fullWidth sx={{ mb: 2, textAlign:"left"}} key={idx}>
                <TextField
                  select
                  label={`Herramienta ${idx + 1}`}
                  value={selectedTool}
                  onChange={e => handleToolChange(idx, e.target.value)}
                  variant="standard"
                  
                >
                  <MenuItem value="">
                    <em>Seleccione una herramienta</em>
                  </MenuItem>
                  {toolOptions.map((tool) => (
                    <MenuItem key={tool.tool_id} value={tool.toolId}>
                      {tool.tool_name}
                    </MenuItem>
                  ))}
                </TextField>
              </FormControl>
            ))}
            <Button
              variant="outlined"
              color="primary"
              onClick={handleAddToolSelect}
              sx={{ mb: 2 }}
            >
              Agregar otra herramienta
            </Button>
            <FormControl sx={{ mb: 2 }}>
              <Button
                variant="contained"
                color="info"
                onClick={saveLoan}
                startIcon={<SaveIcon />}
              >
                Grabar
              </Button>
            </FormControl>
            <hr />
            <Link to="/loan/list">Back to List</Link>
          </Box>
        </Paper>
      </Box>
    </Box>
  );
};

export default NewLoan;