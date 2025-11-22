import { Container, Typography, Button, Box, Card, CardContent } from "@mui/material";

const Home = () => {
    return (
      <Box sx = {{position: "relative"}}>
        <Box 
      sx={{
        position: "fixed",                // ocupa toda la ventana
          top: 0,
          left: 0,
          width: "100%",
          height: "100%",               // altura total de la ventana                     // ancho total de la ventana
        backgroundImage: `url("/fondo.jpg")`,
        backgroundSize: "cover",            // cubre toda la pantalla
        backgroundPosition: "center",       // centrada
        backgroundRepeat: "no-repeat",  
        overflow: "hidden",     // no se repite
        display: "flex",                    // para centrar contenido
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
          WebkitTextStroke: "1px rgba(0,0,0,0.85)", // contorno en navegadores WebKit
          textShadow: [
            "1px 1px 0 rgba(0,0,0,0.85)",
            "-1px 1px 0 rgba(0,0,0,0.85)",
            "1px -1px 0 rgba(0,0,0,0.85)",
            "-1px -1px 0 rgba(0,0,0,0.85)"
          ].join(", "), // fallback para simular contorno en otros navegadores
        }}
      >
        Bienvenido Al Sistema de Renta de Herramientas Tool Rent
      </Typography>
      <Typography
      variant="h5"
        gutterBottom
        sx={{
          fontWeight: 700,
          color: "#fff",
          WebkitTextStroke: "1px rgba(0,0,0,0.85)", // contorno en navegadores WebKit
          textShadow: [
            "1px 1px 0 rgba(0,0,0,0.85)",
            "-1px 1px 0 rgba(0,0,0,0.85)",
            "1px -1px 0 rgba(0,0,0,0.85)",
            "-1px -1px 0 rgba(0,0,0,0.85)"
          ].join(", "), // fallback para simular contorno en otros navegadores
        }}
        >
        Hace click en el menu lateral para alguna acción
      </Typography>      
      </Box>
    </Box>
    
    );
};

export default Home;