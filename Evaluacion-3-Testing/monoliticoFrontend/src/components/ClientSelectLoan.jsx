import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import clientService from "../services/client.service";
import imagesService from "../services/images.service";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import Button from "@mui/material/Button";
import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import InputAdornment from "@mui/material/InputAdornment";
import SearchIcon from "@mui/icons-material/Search";
import CircularProgress from "@mui/material/CircularProgress";
import AddCircleIcon from "@mui/icons-material/AddCircle";
import Typography from "@mui/material/Typography";

const ClientSelectLoan  = () => {
  const [clients, setClients] = useState([]);
  const [search, setSearch] = useState("");
  const [loading, setLoading] = useState(false); // Estado de carga
  const [imageMap, setImageMap] = useState({}); // Mapear imágenes por cliente
  const navigate = useNavigate(); // Import useNavigate hook

  useEffect(() => {
    setLoading(true); // Iniciar carga
    clientService
      .getAll()
      .then((response) => {
        setClients(response.data);
        response.data.forEach((client) => {
          loadImage(client.client_id);
        });
      })
      .catch((error) => {
        console.error("Error al obtener la lista de clientes:", error);
      })
      .finally(() => {
        setLoading(false); // Finalizar carga
      });
  }, []);

  const loadImage = (clientId) => {
    const filename = `icon${clientId}.png`;
    imagesService
      .getImage(filename)
      .then((response) => {
        const url = URL.createObjectURL(response.data);
        setImageMap((prev) => ({
          ...prev,
          [clientId]: url,
        }));
      })
      .catch((error) => {
        console.error(`Error al cargar imagen para cliente ${clientId}:`, error);
      });
  };

  const filteredClients = clients
    .filter((client) => {
      const rolesField = client.role;
      if (Array.isArray(rolesField)) {
        return rolesField.map((r) => String(r).toUpperCase()).includes("CLIENT");
      }
      if (typeof rolesField === "string") {
        return rolesField.toUpperCase() === "CLIENT";
      }
      return false;
    })
    .filter((client) => (client.rut || "").toLowerCase().includes(search.toLowerCase()));


  const handleSelect = (clientId) => {
    navigate(`/loan/new/${clientId}`); // Redirect to the desired route
  };

  return (
    <Box sx={{ position: "relative" }}>
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
          overflow: "hidden",
          filter: "blur(8px)",
          zIndex: 0,
        }}
      />
      <Box
        sx={{
          position: "relative",
          zIndex: 1,
          minHeight: "100vh",
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          justifyContent: "flex-start",
          color: "white",
          textAlign: "center",
          paddingTop: 6,
        }}
      >
        {loading && (
          <Box
            sx={{
              position: "fixed",
              top: 0,
              left: 0,
              width: "100%",
              height: "100%",
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              backgroundColor: "rgba(0, 0, 0, 0.5)",
              zIndex: 10,
            }}
          >
            <CircularProgress />
          </Box>
        )}
        <TableContainer component={Paper} sx={{ maxWidth: 1400, background: "rgba(198, 198, 198, 0.85)" }}>
          <Table sx={{ minWidth: 650 }} size="small" aria-label="a dense table">
            <TableHead>
              <TableRow>
                <TableCell colSpan={8} align="center">
                  <Typography variant="h5" sx={{ color: "black", fontWeight: "bold" }}>
                    Seleccione un cliente
                  </Typography>
                </TableCell>
              </TableRow>
              <TableRow>
                <TableCell colSpan={8} align="left">
                  <TextField
                    variant="outlined"
                    placeholder="Buscar cliente por rut..."
                    value={search}
                    onChange={e => setSearch(e.target.value)}
                    sx={{ width: 350, background: "white", borderRadius: 1 }}
                    InputProps={{
                      startAdornment: (
                        <InputAdornment position="start">
                          <SearchIcon />
                        </InputAdornment>
                      ),
                      sx: { height: 43 }
                    }}
                    inputProps={{
                      style: { height: 43, boxSizing: "border-box" }
                    }}
                  />
                </TableCell>
              </TableRow>
              <TableRow>
                <TableCell align="left" sx={{ maxWidth: 100, fontWeight: "bold", color: "black" }}>
                  Id
                </TableCell>

                <TableCell align="center" sx={{ maxWidth: 100, fontWeight: "bold", color: "black" }}>
                  Foto
                </TableCell>

                <TableCell align="left" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  Rut
                </TableCell>
                <TableCell align="left" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  lastName
                </TableCell>
                <TableCell align="left" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  Name
                </TableCell>
                <TableCell align="center" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  email
                </TableCell>
                <TableCell align="center" sx={{ maxWidth: 180, fontWeight: "bold", color: "black" }}>
                  phone Number
                </TableCell>
                <TableCell align="center" sx={{ maxWidth: 80, fontWeight: "bold", color: "black" }}>
                  Acción
                </TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {filteredClients.map((client) => (
                <React.Fragment key={client.client_id}>
                  <TableRow
                    sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
                  >
                    <TableCell align="left" sx={{ maxWidth: 180 }}>{client.client_id}</TableCell>
                    <TableCell align="left" sx={{ maxWidth: 100 }}>
                      <img
                        src={imageMap[client.client_id] || "/vite.svg"}
                        style={{ width: 48, height: 48, objectFit: "cover", borderRadius: 6 }}
                        alt={`Avatar ${client.client_id}`}
                      />
                    </TableCell>
                    <TableCell align="left" sx={{ maxWidth: 180 }}>{client.rut}</TableCell>
                    <TableCell align="left" sx={{ maxWidth: 180 }}>{client.last_name}</TableCell>
                    <TableCell align="center" sx={{ maxWidth: 180 }}>{client.name}</TableCell>
                    <TableCell align="center" sx={{ maxWidth: 180 }}>{client.mail}</TableCell>
                    <TableCell align="center" sx={{ maxWidth: 180 }}>{client.phone_number}</TableCell>
                    <TableCell align="center" sx={{ maxWidth: 180 }}>
                      <Box sx={{ display: "flex", gap: 1, justifyContent: "center" }}>
                      <Button
                            variant="contained"
                            color="info"
                            size="small"
                            onClick={() => handleSelect(client.client_id)}
                            startIcon={<AddCircleIcon />}
                          >
                            Seleccionar
                      </Button>
                      </Box>
                    </TableCell>
                  </TableRow>
                </React.Fragment>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      </Box>
    </Box>
  );
};

export default ClientSelectLoan;