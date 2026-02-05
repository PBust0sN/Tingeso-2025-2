import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useKeycloak } from "@react-keycloak/web";
import Box from "@mui/material/Box";
import Paper from "@mui/material/Paper";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import Container from "@mui/material/Container";

const Login = () => {
  const { keycloak, initialized } = useKeycloak();
  const navigate = useNavigate();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  console.log('Keycloak initialized?', initialized);
  console.log('Keycloak instance:', keycloak);
  console.log('Is authenticated?', keycloak?.authenticated);
  
  useEffect(() => {
    if (initialized && keycloak.authenticated) {
      navigate("/home");
    }
  }, [initialized, keycloak.authenticated, navigate]);

  if (!initialized) return (
    <Box sx={{ position: "relative", minHeight: "100vh" }}>
      {/* background */}
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
      {/* Frame of the login */}
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
            width: "80%",
            maxWidth: 900,
            minHeight: 500,
            background: "rgba(255,255,255,0.95)",
            color: "#222",
            borderRadius: "8px",
            boxShadow: "0 4px 16px rgba(0,0,0,0.15)",
            display: "flex",
            overflow: "hidden",
          }}
        >
          {/* Left side - Image placeholder (40%) */}
          <Box
            sx={{
              width: "40%",
              backgroundImage: `url("/login-image.jpg")`,
              backgroundSize: "cover",
              backgroundPosition: "center",
              backgroundRepeat: "no-repeat",
              backgroundColor: "#f0f0f0",
            }}
          />
          
          {/* Right side - Form (40%) */}
          <Box
            sx={{
              width: "60%",
              p: 5,
              display: "flex",
              flexDirection: "column",
              justifyContent: "center",
              gap: 3,
            }}
          >
            <h2 style={{ margin: 0, marginBottom: 20 }}>Iniciar Sesión</h2>
            
            <TextField
              label="Usuario"
              variant="outlined"
              fullWidth
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="Ingrese su usuario"
            />
            
            <TextField
              label="Contraseña"
              type="password"
              variant="outlined"
              fullWidth
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Ingrese su contraseña"
            />
            
            <Button
              variant="contained"
              size="large"
              sx={{
                mt: 2,
                backgroundColor: "#1976d2",
                color: "white",
                "&:hover": {
                  backgroundColor: "#1565c0",
                },
              }}
            >
              Iniciar Sesión
            </Button>
          </Box>
        </Paper>
      </Box>
    </Box>
  );

  if (!keycloak.authenticated) {
    keycloak.login();
    return (
    <Box sx={{ position: "relative", minHeight: "100vh" }}>
      {/*background */}
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
      {/* frame off the login */}
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
            width: "80%",
            maxWidth: 900,
            minHeight: 500,
            background: "rgba(255,255,255,0.95)",
            color: "#222",
            borderRadius: "8px",
            boxShadow: "0 4px 16px rgba(0,0,0,0.15)",
            display: "flex",
            overflow: "hidden",
          }}
        >
          {/* Left side - Image placeholder (40%) */}
          <Box
            sx={{
              width: "40%",
              backgroundImage: `url("/login-image.jpg")`,
              backgroundSize: "cover",
              backgroundPosition: "center",
              backgroundRepeat: "no-repeat",
              backgroundColor: "#f0f0f0",
            }}
          />
          
          {/* Right side - Form (40%) */}
          <Box
            sx={{
              width: "60%",
              p: 5,
              display: "flex",
              flexDirection: "column",
              justifyContent: "center",
              gap: 3,
            }}
          >
            <h2 style={{ margin: 0, marginBottom: 20 }}>Iniciar Sesión</h2>
            
            <TextField
              label="Usuario"
              variant="outlined"
              fullWidth
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="Ingrese su usuario"
            />
            
            <TextField
              label="Contraseña"
              type="password"
              variant="outlined"
              fullWidth
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Ingrese su contraseña"
            />
            
            <Button
              variant="contained"
              size="large"
              sx={{
                mt: 2,
                backgroundColor: "#1976d2",
                color: "white",
                "&:hover": {
                  backgroundColor: "#1565c0",
                },
              }}
            >
              Iniciar Sesión
            </Button>
          </Box>
        </Paper>
      </Box>
    </Box>
  );
  }

  return (
    <Box sx={{ position: "relative", minHeight: "100vh" }}>
      {/* background */}
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
      {/* frame of the login */}
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
            width: "80%",
            maxWidth: 900,
            minHeight: 500,
            background: "rgba(255,255,255,0.95)",
            color: "#222",
            borderRadius: "8px",
            boxShadow: "0 4px 16px rgba(0,0,0,0.15)",
            display: "flex",
            overflow: "hidden",
          }}
        >
          {/* Left side - Image placeholder (40%) */}
          <Box
            sx={{
              width: "40%",
              backgroundImage: `url("/login-image.jpg")`,
              backgroundSize: "cover",
              backgroundPosition: "center",
              backgroundRepeat: "no-repeat",
              backgroundColor: "#f0f0f0",
            }}
          />
          
          {/* Right side - Form (40%) */}
          <Box
            sx={{
              width: "60%",
              p: 5,
              display: "flex",
              flexDirection: "column",
              justifyContent: "center",
              gap: 3,
            }}
          >
            <h2 style={{ margin: 0, marginBottom: 20 }}>Iniciar Sesión</h2>
            
            <TextField
              label="Usuario"
              variant="outlined"
              fullWidth
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="Ingrese su usuario"
            />
            
            <TextField
              label="Contraseña"
              type="password"
              variant="outlined"
              fullWidth
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Ingrese su contraseña"
            />
            
            <Button
              variant="contained"
              size="large"
              sx={{
                mt: 2,
                backgroundColor: "#1976d2",
                color: "white",
                "&:hover": {
                  backgroundColor: "#1565c0",
                },
              }}
            >
              Iniciar Sesión
            </Button>
          </Box>
        </Paper>
      </Box>
    </Box>
  );
};

export default Login;