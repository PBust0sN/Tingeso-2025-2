import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useKeycloak } from "@react-keycloak/web";
import Box from "@mui/material/Box";
import Paper from "@mui/material/Paper";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import Container from "@mui/material/Container";
import login1 from "../../public/login1.png";
import login2 from "../../public/login2.png";
import login3 from "../../public/login3.png";
import login4 from "../../public/login4.png";
import clientService from "../services/client.service";

const images = [
  login1,
  login2,
  login3,
  login4
];

const Login = () => {
  const { keycloak, initialized } = useKeycloak();
  const navigate = useNavigate();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [currentImageIndex, setCurrentImageIndex] = useState(0);
  const [errorMessage, setErrorMessage] = useState("");

  console.log('Keycloak initialized?', initialized);
  console.log('Keycloak instance:', keycloak);
  console.log('Is authenticated?', keycloak?.authenticated);

  useEffect(() => {
    if (initialized && keycloak.authenticated) {
      navigate("/home");
    }

    const interval = setInterval(() => {
      setCurrentImageIndex((prevIndex) => (prevIndex + 1) % images.length);
    }, 3000); // Change image every 3 seconds

    return () => clearInterval(interval);
  }, [initialized, keycloak.authenticated, navigate]);

  useEffect(() => {
    if (initialized && keycloak.authenticated) {
      navigate("/home");
    }
  }, [initialized, keycloak.authenticated, navigate]);

  useEffect(() => {
    const token = localStorage.getItem("authToken");
    const refreshToken = localStorage.getItem("refreshToken");

    if (token && refreshToken) {
        const tokenPayload = JSON.parse(atob(token.split(".")[1]));
        const isTokenExpired = tokenPayload.exp * 1000 < Date.now();

        if (isTokenExpired) {
            clientService.refreshToken(refreshToken)
                .then(response => {
                    localStorage.setItem("authToken", response.data.access_token);
                    localStorage.setItem("refreshToken", response.data.refresh_token);
                })
                .catch(() => {
                    console.error("Failed to refresh token");
                    localStorage.removeItem("authToken");
                    localStorage.removeItem("refreshToken");
                });
        }
    }
}, []);

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
          backgroundImage: `url(./fondo.jpg)`,
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
          padding: "20px", // Added padding to ensure space for larger Paper
          boxSizing: "border-box", // Ensure padding is included in the layout
        }}
      >
        <Paper
          elevation={6}
          sx={{
            width: 1000, // Ensure it takes full width of the container
            maxWidth: "1200px", // Limit maximum width
            maxHeight: "700px", // Further increased height
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
              width: "60%",
              backgroundImage: `url(${images[currentImageIndex]})`,
              backgroundSize: "cover",
              backgroundPosition: "center",
              backgroundRepeat: "no-repeat",
              backgroundColor: "#f0f0f0",
            }}
          />
          
          {/* Right side - Form (40%) */}
          <Box
            sx={{
              width: "40%",
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
              onClick={async () => {
                try {
                  const response = await clientService.login(username, password);
                  const { access_token, refresh_token } = response.data;
                  console.log("Login successful, tokens:", response.data);
                  localStorage.setItem("authToken", access_token); // Store access token in localStorage
                  localStorage.setItem("refreshToken", refresh_token); // Store refresh token in localStorage
                  navigate("/home");
                } catch (error) {
                  if (error.response && error.response.status === 401) {
                    setErrorMessage("Contraseña y/o usuario incorrecto");
                  } else {
                    setErrorMessage("Ocurrió un error inesperado");
                  }
                }
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
          backgroundImage: `url("./fondo.jpg")`,
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
          padding: "20px", // Added padding to ensure space for larger Paper
          boxSizing: "border-box", // Ensure padding is included in the layout
        }}
      >
        <Paper
          elevation={6}
          sx={{
            width: 1000, // Ensure it takes full width of the container
            maxWidth: "1200px", // Limit maximum width
            maxHeight: "700px", // Further increased height
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
              width: "60%",
              backgroundImage: `url(${images[currentImageIndex]})`,
              backgroundSize: "cover",
              backgroundPosition: "center",
              backgroundRepeat: "no-repeat",
              backgroundColor: "#f0f0f0",
            }}
          />
          
          {/* Right side - Form (60%) */}
          <Box
            sx={{
              width: "40%",
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
              onClick={async () => {
                try {
                  const response = await clientService.login(username, password);
                  const { access_token, refresh_token } = response.data;
                  console.log("Login successful, tokens:", response.data);
                  localStorage.setItem("authToken", access_token); // Store access token in localStorage
                  localStorage.setItem("refreshToken", refresh_token); // Store refresh token in localStorage
                  navigate("/home");
                } catch (error) {
                  if (error.response && error.response.status === 401) {
                    setErrorMessage("Contraseña y/o usuario incorrecto");
                  } else {
                    setErrorMessage("Ocurrió un error inesperado");
                  }
                }
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
          backgroundImage: `url(${images[currentImageIndex]})`,
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
          padding: "20px", // Added padding to ensure space for larger Paper
          boxSizing: "border-box", // Ensure padding is included in the layout
        }}
      >
        <Paper
          elevation={6}
          sx={{
            width: 1000, // Ensure it takes full width of the container
            maxWidth: "1200px", // Limit maximum width
            maxHeight: "700px", // Further increased height
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
              width: "60%",
              backgroundImage: `url("/login-image.jpg")`,
              backgroundSize: "cover",
              backgroundPosition: "center",
              backgroundRepeat: "no-repeat",
              backgroundColor: "#f0f0f0",
            }}
          />
          
          {/* Right side - Form (60%) */}
          <Box
            sx={{
              width: "40%",
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
              onClick={async () => {
                try {
                  const response = await clientService.login(username, password);
                  const { access_token, refresh_token } = response.data;
                  console.log("Login successful, tokens:", response.data);
                  localStorage.setItem("authToken", access_token); // Store access token in localStorage
                  localStorage.setItem("refreshToken", refresh_token); // Store refresh token in localStorage
                  navigate("/home");
                } catch (error) {
                  if (error.response && error.response.status === 401) {
                    setErrorMessage("Contraseña y/o usuario incorrecto");
                  } else {
                    setErrorMessage("Ocurrió un error inesperado");
                  }
                }
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