import { useState, useEffect } from "react";
import { Link, useParams, useNavigate } from "react-router-dom";
import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import FormControl from "@mui/material/FormControl";
import SaveIcon from "@mui/icons-material/Save";
import Paper from "@mui/material/Paper";
import MenuItem from "@mui/material/MenuItem";
import toolsService from "../services/tools.service";
import loansService from "../services/loans.service";
import toolsLoansService from "../services/toolsLoans.service";

const EditLoan = () => {
  const [loanType, setLoanType] = useState("");
  const [amount, setAmount] = useState("");
  const [deliveryDate, setDeliveryDate] = useState("");
  const [returnDate, setReturnDate] = useState("");
  const [date, setDate] = useState("");
  const [staffId, setStaffId] = useState("");
  const [clientId, setClientId] = useState("");
  const [extraCharges, setExtraCharges] = useState("");
  const [toolOptions, setToolOptions] = useState([]);
  const [selectedTools, setSelectedTools] = useState([]);
  const { loanId } = useParams();
  const navigate = useNavigate();

  // Cargar opciones de herramientas
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

  // Cargar datos del préstamo y herramientas asociadas
  useEffect(() => {
    if (loanId) {
      loansService
        .get(loanId)
        .then((loan) => {
          setLoanType(loan.data.loanType);
          setAmount(loan.data.amount);
          setDeliveryDate(loan.data.deliveryDate);
          setReturnDate(loan.data.returnDate);
          setDate(loan.data.date);
          setStaffId(loan.data.staffId);
          setClientId(loan.data.clientId);
          setExtraCharges(loan.data.extraCharges);

          // Obtener los ids de herramientas asociadas
          toolsLoansService
            .getToolsIdByLoanId(loanId)
            .then((idsResponse) => {
              const toolIds = idsResponse.data; // array de ids
              // Rellenar los desplegables con los ids asociados
              setSelectedTools(toolIds.length > 0 ? toolIds : [""]);
            })
            .catch((error) => {
              console.log("Error al obtener ids de herramientas:", error);
              setSelectedTools([""]);
            });
        })
        .catch((error) => {
          console.log("Se ha producido un error.", error);
        });
    }
  }, [loanId]);

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
      loanId,
      loanType,
      amount,
      deliveryDate,
      returnDate,
      date,
      staffId,
      clientId,
      extraCharges,
      tools_id: selectedTools.filter(Boolean),
    };
    loansService
      .update(loan)
      .then((response) => {
        console.log("Préstamo actualizado.", response.data);
        navigate("/loan/list");
      })
      .catch((error) => {
        console.log("Ha ocurrido un error al intentar actualizar el préstamo.", error);
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
            <h3>Editar Préstamo</h3>
            <hr />
            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                id="loanType"
                label="Tipo de Préstamo"
                value={loanType}
                variant="standard"
                onChange={(e) => setLoanType(e.target.value)}
              />
            </FormControl>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                id="amount"
                label="Cantidad"
                type="number"
                value={amount}
                variant="standard"
                onChange={(e) => setAmount(e.target.value)}
              />
            </FormControl>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                id="deliveryDate"
                label="Fecha de Entrega"
                type="date"
                value={deliveryDate}
                variant="standard"
                onChange={(e) => setDeliveryDate(e.target.value)}
                InputLabelProps={{ shrink: true }}
              />
            </FormControl>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                id="returnDate"
                label="Fecha de Retorno"
                type="date"
                value={returnDate}
                variant="standard"
                onChange={(e) => setReturnDate(e.target.value)}
                InputLabelProps={{ shrink: true }}
              />
            </FormControl>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                id="date"
                label="Fecha"
                type="date"
                value={date}
                variant="standard"
                onChange={(e) => setDate(e.target.value)}
                InputLabelProps={{ shrink: true }}
              />
            </FormControl>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                id="staffId"
                label="ID Staff"
                value={staffId}
                variant="standard"
                onChange={(e) => setStaffId(e.target.value)}
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
                id="extraCharges"
                label="Cargos Extra"
                type="number"
                value={extraCharges}
                variant="standard"
                onChange={(e) => setExtraCharges(e.target.value)}
              />
            </FormControl>
            {/* Desplegables dinámicos para herramientas */}
            {selectedTools.map((selectedTool, idx) => (
              <FormControl fullWidth sx={{ mb: 2, textAlign: "left" }} key={idx}>
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

export default EditLoan;