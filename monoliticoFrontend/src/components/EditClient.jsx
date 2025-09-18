import { useState, useEffect } from "react";
import { Link, useParams, useNavigate } from "react-router-dom";
import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import FormControl from "@mui/material/FormControl";
import SaveIcon from "@mui/icons-material/Save";
import Paper from "@mui/material/Paper";
import clientService from "../services/client.service";
import MenuItem from "@mui/material/MenuItem";

const EditClient = () => {
  const [avaliable, setAvaliable] = useState("");
  const [last_name, setLastName] = useState("");
  const [mail, setMail] = useState("");
  const [name, setName] = useState("");
  const [phone_number, setPhoneNumber] = useState("");
  const [rut, setRut] = useState("");
  const [state, setState] = useState("");
  const { client_id } = useParams();
  const navigate = useNavigate();

  useEffect(() => {
    if (client_id) {
      clientService
        .get(client_id)
        .then((client) => {
          {/*AQUI VAN LOS SET */}
          setRut(client.data.rut);
          setName(client.data.name);
          setLastName(client.data.last_name);
          setMail(client.data.mail);
          setAvaliable(client.data.avaliable);
          setState(client.data.state);
          setPhoneNumber(client.data.phone_number);
        })
        .catch((error) => {
          console.log("Se ha producido un error.", error);
        });
    }
  }, [client_id]);

  const saveTool = (e) => {
    e.preventDefault();
    const client = { client_id, rut, name, last_name, mail, avaliable, state, phone_number};
    clientService
      .update(client)
      .then((response) => {
        console.log("Cliente actualizado.", response.data);
        navigate("/client/list");
      })
      .catch((error) => {
        console.log("Ha ocurrido un error al intentar actualizar el cliente.", error);
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
            <h3>Editar Cliente</h3>
            <hr />
            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                id="rut"
                label="Rut"
                value={rut}
                variant="standard"
                onChange={(e) => setrut(e.target.value)}
                helperText="99999999-9"
              />
            </FormControl>

            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                id="name"
                label="Name"
                value={name}
                variant="standard"
                onChange={(e) => setName(e.target.value)}
              />
            </FormControl>

            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                id="last_name"
                label="Last Name"
                value={last_name}
                variant="standard"
                onChange={(e) => setLastName(e.target.value)}
              />
            </FormControl>

            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                id="mail"
                label="E-Mail"
                value={mail}
                variant="standard"
                onChange={(e) => setMail(e.target.value)}
              />
            </FormControl>

            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                    id="avaliable"
                    label="Avaliable"
                    value={avaliable}
                    select
                    variant="standard"
                    defaultValue="true"
                    onChange={(e) => setAvaliable(e.target.value)}
                    style={{ width: "25%" }}
                >
                    <MenuItem value={"true"}>TRUE</MenuItem>
                    <MenuItem value={"false"}>FALSE</MenuItem>
              </TextField>
            </FormControl>

            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                id="state"
                label="State"
                value={state}
                variant="standard"
                onChange={(e) => setState(e.target.value)}
              />
            </FormControl>

            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                id="phone_number"
                label="Phone Number"
                value={phone_number}
                variant="standard"
                onChange={(e) => setPhoneNumber(e.target.value)}
                helperText="Ej. +56911112222"
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
            <Link to="/client/list">Back to List</Link>
          </Box>
        </Paper>
      </Box>
    </Box>
  );
};

export default EditClient;
