import React, { useEffect, useState, useRef } from "react";
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
import CircularProgress from "@mui/material/CircularProgress";

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
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");
  const [loading, setLoading] = useState(true); // Added loading state

  const tokenRef = useRef(localStorage.getItem("authToken"));
  const refreshTokenRef = useRef(localStorage.getItem("refreshToken"));

  useEffect(() => {
    if (initialized && isAuthenticated) {
      navigate("/home");
    }

    const interval = setInterval(() => {
      setCurrentImageIndex((prevIndex) => (prevIndex + 1) % images.length);
    }, 3000); // Change image every 3 seconds

    setLoading(false); // Set loading to false after initialization

    return () => clearInterval(interval);
  }, [initialized, isAuthenticated, navigate]);

  useEffect(() => {
    if (initialized && isAuthenticated) {
      navigate("/home");
    }
  }, [initialized, isAuthenticated, navigate]);

  useEffect(() => {
    const token = tokenRef.current;
    const refreshToken = refreshTokenRef.current;

    if (token && refreshToken) {
      try {
        const tokenPayload = JSON.parse(atob(token.split(".")[1])); // Decode JWT payload
        const isTokenExpired = tokenPayload.exp * 1000 < Date.now(); // Check if token is expired

        if (isTokenExpired) {
          clientService.refreshToken(refreshToken)
            .then(response => {
              localStorage.setItem("authToken", response.data.access_token);
              localStorage.setItem("refreshToken", response.data.refresh_token);
              tokenRef.current = response.data.access_token; // Update token in memory
              refreshTokenRef.current = response.data.refresh_token; // Update refresh token in memory
              setIsAuthenticated(true);
            })
            .catch(() => {
              console.error("Failed to refresh token");
              localStorage.removeItem("authToken");
              localStorage.removeItem("refreshToken");
              tokenRef.current = null;
              refreshTokenRef.current = null;
              setIsAuthenticated(false);
            });
        } else {
          setIsAuthenticated(true);
        }
      } catch (error) {
        console.error("Failed to parse token payload:", error);
      }
    } else {
      console.warn("Token or refresh token is missing.");
    }
  }, []);

  const handleLogin = async () => {
    setLoading(true); // Mostrar el ícono de carga
    try {
        const response = await clientService.login(username, password);
        console.log("Login successful, response:", response);

        const { access_token, refresh_token } = response;
        localStorage.setItem("authToken", access_token);
        localStorage.setItem("refreshToken", refresh_token);
        tokenRef.current = access_token; // Update token in memory
        refreshTokenRef.current = refresh_token; // Update refresh token in memory
        setIsAuthenticated(true);

        navigate("/home");
        window.location.reload();
    } catch (error) {
        if (error.response && error.response.status === 401) {
            setErrorMessage("Contraseña y/o usuario incorrecto");
        } else {
            setErrorMessage("Ocurrió un error inesperado");
        }
    } finally {
        setLoading(false); // Ocultar el ícono de carga
    }
  };

  if (loading) {
    return (
      <Box
        sx={{
          position: "fixed",
          top: 0,
          left: 0,
          width: "100%",
          height: "100%",
        }}
      >
        {/* Fondo con desenfoque */}
        <Box
          sx={{
            position: "absolute",
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

        {/* CircularProgress sin desenfoque */}
        <Box
          sx={{
            position: "relative",
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            width: "100%",
            height: "100%",
            zIndex: 1,
          }}
        >
          <CircularProgress />
        </Box>
      </Box>
    );
  }
  
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
              onClick={handleLogin}
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
              onClick={handleLogin}
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
              onClick={handleLogin}
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