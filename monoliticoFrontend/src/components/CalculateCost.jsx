import { useLocation, useNavigate } from "react-router-dom";
import Box from "@mui/material/Box";
import Paper from "@mui/material/Paper";
import Typography from "@mui/material/Typography";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Button from "@mui/material/Button";

const estadosHerramienta = [
  "Bueno",
  "Regular",
  "Malo",
  "Reparación",
  "Extraviada"
];

const CalculateCost = () => {
  const { state } = useLocation();
  const navigate = useNavigate();

  if (!state || !state.loan || !state.tools || !state.toolStates) {
    return (
      <Box sx={{ minHeight: "100vh", display: "flex", alignItems: "center", justifyContent: "center" }}>
        <Typography variant="h6" color="text.secondary">
          No hay información para calcular costos.
        </Typography>
      </Box>
    );
  }

  const { loan, tools, toolStates } = state;

  // Ejemplo de cálculo de costos (puedes adaptar la lógica)
  const calcularCostoHerramienta = (estado) => {
    switch (estado) {
      case "Bueno": return 0;
      case "Regular": return 10;
      case "Malo": return 30;
      case "Reparación": return 50;
      case "Extraviada": return 100;
      default: return 0;
    }
  };

  const totalCost = tools.reduce(
    (acc, tool) => acc + calcularCostoHerramienta(toolStates[tool.toolId]),
    0
  );

  const formatDate = (dateStr) => {
    if (!dateStr) return "";
    const date = new Date(dateStr);
    if (isNaN(date)) return "";
    const day = String(date.getDate()).padStart(2, "0");
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const year = date.getFullYear();
    return `${day}/${month}/${year}`;
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
      {/* Frame principal */}
      <Box
        sx={{
          position: "relative",
          zIndex: 1,
          minHeight: "100vh",
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          justifyContent: "flex-start",
          paddingTop: 6,
        }}
      >
        {/* Datos del préstamo */}
        <Paper sx={{ minWidth: 700, maxWidth: 700, width: "100%", mb: 2, background: "rgba(255,255,255,0.85)", p: 2 }}>
          <Typography variant="h6" align="center" sx={{ fontWeight: "bold", mb: 1 }}>
            Préstamo #{loan.loanId}
          </Typography>
          <Table size="small">
            <TableBody>
              <TableRow>
                <TableCell sx={{ fontWeight: "bold" }}>Tipo</TableCell>
                <TableCell>{loan.loanType}</TableCell>
                <TableCell sx={{ fontWeight: "bold" }}>Cantidad</TableCell>
                <TableCell>{loan.amount}</TableCell>
                <TableCell sx={{ fontWeight: "bold" }}>Entrega</TableCell>
                <TableCell>{formatDate(loan.deliveryDate)}</TableCell>
                <TableCell sx={{ fontWeight: "bold" }}>Retorno</TableCell>
                <TableCell>{formatDate(loan.returnDate)}</TableCell>
              </TableRow>
              <TableRow>
                <TableCell sx={{ fontWeight: "bold" }}>Staff</TableCell>
                <TableCell>{loan.staffId}</TableCell>
                <TableCell sx={{ fontWeight: "bold" }}>Cliente</TableCell>
                <TableCell>{loan.clientId}</TableCell>
                <TableCell sx={{ fontWeight: "bold" }}>Cargos Extra</TableCell>
                <TableCell>{loan.extraCharges}</TableCell>
                <TableCell sx={{ fontWeight: "bold" }}>Fecha</TableCell>
                <TableCell>{formatDate(loan.date)}</TableCell>
              </TableRow>
            </TableBody>
          </Table>
        </Paper>

        {/* Herramientas y estados */}
        <Paper sx={{ width: "100%", maxWidth: 700, background: "rgba(255,255,255,0.85)", p: 2, mb: 2 }}>
          <Typography variant="h6" align="center" sx={{ fontWeight: "bold", mb: 1 }}>
            Herramientas y estados
          </Typography>
          <TableContainer>
            <Table size="small">
              <TableHead>
                <TableRow>
                  <TableCell sx={{ fontWeight: "bold" }}>ID</TableCell>
                  <TableCell sx={{ fontWeight: "bold" }}>Nombre</TableCell>
                  <TableCell sx={{ fontWeight: "bold" }}>Estado Seleccionado</TableCell>
                  <TableCell sx={{ fontWeight: "bold" }}>Costo</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {tools.map((tool) => (
                  <TableRow key={tool.toolId}>
                    <TableCell>{tool.toolId}</TableCell>
                    <TableCell>{tool.tool_name}</TableCell>
                    <TableCell>{toolStates[tool.toolId]}</TableCell>
                    <TableCell>${calcularCostoHerramienta(toolStates[tool.toolId])}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        </Paper>

        {/* Total */}
        <Paper sx={{ maxWidth: 700, width: "100%", background: "rgba(255,255,255,0.85)", p: 2, textAlign: "center" }}>
          <Typography variant="h6" sx={{ fontWeight: "bold" }}>
            Costo total: ${totalCost}
          </Typography>
        </Paper>

        <Button variant="contained" sx={{ mt: 3 }} onClick={() => navigate(-1)}>
          Volver
        </Button>
      </Box>
    </Box>
  );
};

export default CalculateCost;