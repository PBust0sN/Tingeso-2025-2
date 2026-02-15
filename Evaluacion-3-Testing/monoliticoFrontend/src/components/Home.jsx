import { Container, Typography, Button, Box, Card, CardContent } from "@mui/material";
import { useNavigate } from "react-router-dom";
import OpenInNewIcon from "@mui/icons-material/OpenInNew";

const Home = () => {
  const navigate = useNavigate();
    return (
      <Box sx = {{position: "relative"}}>
        <Box 
      sx={{
        position: "fixed",                // occupies all the screen
          top: 0,
          left: 0,
          width: "100%",                // total width of the window
          height: "100%",               // total height of the window                    
        backgroundImage: `url("/fondo.jpg")`,
        backgroundSize: "cover",            // cover the entire area
        backgroundPosition: "center",       // centered
        backgroundRepeat: "no-repeat",  
        overflow: "hidden",     // does not repeat
        display: "flex",                    // for centering
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "center",
        color: "white",
        textAlign: "center",
        filter: "blur(8px)"
      }}
    >
    </Box>
    <Box
      sx={{
        position: "relative",
        zIndex: 1,
        height: "100%",
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "center",
        color: "white",
        textAlign: "center",
      }}
      >
      <Typography
        variant="h3"
        gutterBottom
        sx={{
          fontWeight: 700,
          color: "#fff",
          WebkitTextStroke: "1px rgba(0,0,0,0.85)", // webkit outline
          textShadow: [
            "1px 1px 0 rgba(0,0,0,0.85)",
            "-1px 1px 0 rgba(0,0,0,0.85)",
            "1px -1px 0 rgba(0,0,0,0.85)",
            "-1px -1px 0 rgba(0,0,0,0.85)"
          ].join(", "), // fallback for outline in other browsers
        }}
      >
        Bienvenido 
      </Typography>
      <Typography
      variant="h5"
        gutterBottom
        sx={{
          fontWeight: 700,
          color: "#fff",
          WebkitTextStroke: "1px rgba(0,0,0,0.85)", // Wedbkit outline Al Sistema de Renta de Herramientas Tool Rent
          textShadow: [
            "1px 1px 0 rgba(0,0,0,0.85)",
            "-1px 1px 0 rgba(0,0,0,0.85)",
            "1px -1px 0 rgba(0,0,0,0.85)",
            "-1px -1px 0 rgba(0,0,0,0.85)"
          ].join(", "), // fallback for outline in other browsers
        }}
        >
        Necesitas ayuda?, visita el{" "}
        <span 
          onClick={() => navigate("/help")}
          style={{
            color: "#ffffff",
            textDecoration: "underline",
            textDecorationColor: "#ffffff",
            cursor: "pointer",
            fontWeight: 700,
            display: "inline-flex",
            alignItems: "center",
            gap: "4px",
            textShadow: "none"
          }}
        >
          centro de ayuda
          <OpenInNewIcon sx={{ fontSize: "1.2em" }} />
        </span>
        {" "}para conocer los atajos de teclado y más información sobre el sistema.
      </Typography>      
      </Box>
    </Box>
    
    );
};

export default Home;