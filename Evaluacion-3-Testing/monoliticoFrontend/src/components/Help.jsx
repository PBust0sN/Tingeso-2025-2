import React, { useState } from "react";
import {
  Box,
  Container,
  Typography,
  Accordion,
  AccordionSummary,
  AccordionDetails,
  Paper,
  TextField,
  InputAdornment,
} from "@mui/material";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import SearchIcon from "@mui/icons-material/Search";

const Help = () => {
  const [search, setSearch] = useState("");
  const [expandedPanel, setExpandedPanel] = useState(false);

  const faqs = [
    {
      id: 1,
      category: "Préstamos",
      question: "¿Cómo puedo crear un nuevo préstamo?",
      answer:
        "Para crear un nuevo préstamo, dirígete a la sección de Préstamos y selecciona 'Nuevo Préstamo'. Elige el cliente, las herramientas que deseas prestar y especifica la fecha de devolución esperada.",
    },
    {
      id: 2,
      category: "Préstamos",
      question: "¿Qué pasa si un cliente no devuelve una herramienta a tiempo?",
      answer:
        "Si un cliente no devuelve una herramienta a tiempo, se genera automáticamente una multa. Puedes verla en la sección de Multas y hacer seguimiento del estado de pago.",
    },
    {
      id: 3,
      category: "Inicio de Sesión",
      question: "¿Cómo inicio sesión en la aplicación?",
      answer:
        "Para iniciar sesión, dirígete a la página de inicio de sesión e ingresa tu correo electrónico y contraseña. Luego haz clic en 'Iniciar Sesión'. Si eres un nuevo usuario, deberás registrarte primero con tus datos.",
    },
    {
      id: 4,
      category: "Inicio de Sesión",
      question: "¿Qué debo hacer si olvido mi contraseña?",
      answer:
        "Si olvidas tu contraseña, haz clic en el enlace 'Olvidé mi contraseña' en la página de inicio de sesión. Ingresa tu correo electrónico y recibirás instrucciones para restablecer tu contraseña.",
    },
    {
      id: 5,
      category: "Inicio de Sesión",
      question: "¿Mi sesión caduca?",
      answer:
        "Sí, por razones de seguridad, las sesiones cuentan con un tiempo de expiración. Si tu sesión expira, el sistema intentará renovarla automáticamente. Si no puede renovarla, deberás iniciar sesión de nuevo.",
    },
    {
      id: 6,
      category: "Inicio de Sesión",
      question: "¿Qué significan los diferentes roles de usuario?",
      answer:
        "Existen tres roles principales: Admin (acceso completo del sistema), Staff (puede gestionar préstamos, clientes y herramientas) y Client (puede consultar sus propios reportes y préstamos).",
    },
    {
      id: 7,
      category: "Clientes",
      question: "¿Cómo agrego un nuevo cliente al sistema?",
      answer:
        "Ve a la sección de Clientes, haz clic en 'Agregar Cliente' e ingresa los datos requeridos: nombre, RUT, correo electrónico y número de teléfono.",
    },
    {
      id: 8,
      category: "Clientes",
      question: "¿Puedo editar la información de un cliente existente?",
      answer:
        "Sí, en la lista de Clientes encontrarás un botón de edición junto a cada cliente. Haz clic para modificar sus datos.",
    },
    {
      id: 9,
      category: "Herramientas",
      question: "¿Cómo administro el inventario de herramientas?",
      answer:
        "En la sección de Herramientas puedes ver el inventario completo. Puedes agregar nuevas herramientas, editar existentes o eliminarlas si es necesario.",
    },
    {
      id: 10,
      category: "Herramientas",
      question: "¿Qué información debo proporcionar al agregar una herramienta?",
      answer:
        "Debes proporcionar el nombre de la herramienta, descripción, código único, cantidad disponible y el precio diario de alquiler.",
    },
    {
      id: 11,
      category: "Devoluciones",
      question: "¿Cuál es el proceso para devolver una herramienta?",
      answer:
        "Dirígete a Préstamos, selecciona el préstamo que deseas cerrar, haz clic en 'Devolver' y verifica el estado de la herramienta. El sistema calculará automáticamente cualquier multa si está retrasada.",
    },
    {
      id: 12,
      category: "Reportes",
      question: "¿Qué tipos de reportes están disponibles?",
      answer:
        "Puedes generar reportes sobre: Préstamos por cliente, Herramientas más utilizadas, Clientes con préstamos atrasados y Multas aplicadas.",
    },
    {
      id: 13,
      category: "Reportes",
      question: "¿Cómo descargo un reporte?",
      answer:
        "Una vez generado un reporte, encontrarás un botón de descarga. Los reportes se pueden descargar en formato PDF o Excel según la disponibilidad.",
    },
    {
      id: 14,
      category: "Usuarios",
      question: "¿Qué roles de usuario existen en el sistema?",
      answer:
        "Existen tres roles: Admin (acceso completo), Staff (gestión de préstamos y clientes) y Client (consultar propios reportes y préstamos).",
    },
    {
      id: 15,
      category: "Atajos de Teclado",
      question: "¿Cuáles son los atajos de teclado disponibles?",
      answer:
        "El sistema incluye los siguientes atajos de teclado para navegar más rápidamente: Shift+N para ir a nuevo préstamo, Shift+H para volver al inicio, y Shift+B para volver a la página anterior.",
    },
    {
      id: 16,
      category: "Atajos de Teclado",
      question: "¿Cómo creo un nuevo préstamo usando el atajo de teclado?",
      answer:
        "Presiona Shift+N desde cualquier página para ir directamente a la sección de creación de nuevo préstamo. Esta combinación de teclas te llevará a la pantalla de selección de clientes.",
    },
    {
      id: 17,
      category: "Atajos de Teclado",
      question: "¿Cómo navego rápidamente a la página de inicio?",
      answer:
        "Presiona Shift+H desde cualquier página para volver al inicio de la aplicación. Este atajo funciona desde cualquier sección del sistema.",
    },
    {
      id: 18,
      category: "Atajos de Teclado",
      question: "¿Cómo vuelvo a la página anterior?",
      answer:
        "Presiona Shift+B para volver a la página que visitaste anteriormente. Este atajo es equivalente a hacer clic en el botón atrás del navegador.",
    },
  ];

  const filteredFaqs = faqs.filter(
    (faq) =>
      faq.question.toLowerCase().includes(search.toLowerCase()) ||
      faq.answer.toLowerCase().includes(search.toLowerCase()) ||
      faq.category.toLowerCase().includes(search.toLowerCase())
  );

  const handleChange = (panel) => (event, isExpanded) => {
    setExpandedPanel(isExpanded ? panel : false);
  };

  return (
    <Box sx={{ position: "relative" }}>
      {/* Fondo con imagen */}
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

      {/* Contenido principal */}
      <Box
        sx={{
          position: "relative",
          zIndex: 1,
          minHeight: "100vh",
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          py: 4,
        }}
      >
        <Container maxWidth="md">
          {/* Título */}
          <Typography
            variant="h3"
            gutterBottom
            sx={{
              fontWeight: 700,
              color: "#fff",
              textAlign: "center",
              mb: 1,
              WebkitTextStroke: "1px rgba(0,0,0,0.85)",
              textShadow: [
                "1px 1px 0 rgba(0,0,0,0.85)",
                "-1px 1px 0 rgba(0,0,0,0.85)",
                "1px -1px 0 rgba(0,0,0,0.85)",
                "-1px -1px 0 rgba(0,0,0,0.85)",
              ].join(", "),
            }}
          >
            Centro de Ayuda
          </Typography>

          <Typography
            variant="h6"
            gutterBottom
            sx={{
              color: "#fff",
              textAlign: "center",
              mb: 4,
              WebkitTextStroke: "0.5px rgba(0,0,0,0.85)",
              textShadow: [
                "1px 1px 0 rgba(0,0,0,0.85)",
                "-1px 1px 0 rgba(0,0,0,0.85)",
                "1px -1px 0 rgba(0,0,0,0.85)",
                "-1px -1px 0 rgba(0,0,0,0.85)",
              ].join(", "),
            }}
          >
            Preguntas Frecuentes
          </Typography>

          {/* Barra de búsqueda */}
          <Paper
            elevation={3}
            sx={{
              mb: 4,
              p: 2,
              background: "rgba(255,255,255,0.95)",
              borderRadius: "8px",
            }}
          >
            <TextField
              fullWidth
              placeholder="Buscar en preguntas frecuentes..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              InputProps={{
                startAdornment: (
                  <InputAdornment position="start">
                    <SearchIcon sx={{ color: "#999" }} />
                  </InputAdornment>
                ),
              }}
              sx={{
                "& .MuiOutlinedInput-root": {
                  fontSize: "1rem",
                },
              }}
            />
          </Paper>

          {/* Acordeones de preguntas */}
          <Box sx={{ display: "flex", flexDirection: "column", gap: 1 }}>
            {filteredFaqs.length > 0 ? (
              filteredFaqs.map((faq) => (
                <Accordion
                  key={faq.id}
                  expanded={expandedPanel === `panel${faq.id}`}
                  onChange={handleChange(`panel${faq.id}`)}
                  sx={{
                    background: "rgba(255,255,255,0.92)",
                    borderRadius: "8px",
                    "& .MuiAccordionSummary-root": {
                      backgroundColor: "rgba(76, 175, 80, 0.1)",
                      borderRadius: "8px 8px 0 0",
                      "&:hover": {
                        backgroundColor: "rgba(76, 175, 80, 0.2)",
                      },
                    },
                    "&.Mui-expanded": {
                      "& .MuiAccordionSummary-root": {
                        backgroundColor: "rgba(76, 175, 80, 0.15)",
                        borderRadius: "8px 8px 0 0",
                      },
                    },
                  }}
                >
                  <AccordionSummary
                    expandIcon={<ExpandMoreIcon />}
                    aria-controls={`panel${faq.id}-content`}
                    id={`panel${faq.id}-header`}
                  >
                    <Box sx={{ display: "flex", flexDirection: "column", width: "100%" }}>
                      <Typography
                        variant="subtitle2"
                        sx={{
                          color: "#4caf50",
                          fontWeight: 600,
                          fontSize: "0.8rem",
                          mb: 0.5,
                        }}
                      >
                        {faq.category}
                      </Typography>
                      <Typography
                        variant="body1"
                        sx={{
                          fontWeight: 600,
                          color: "#333",
                        }}
                      >
                        {faq.question}
                      </Typography>
                    </Box>
                  </AccordionSummary>
                  <AccordionDetails
                    sx={{
                      backgroundColor: "rgba(255,255,255,1)",
                      borderRadius: "0 0 8px 8px",
                      borderTop: "1px solid rgba(76, 175, 80, 0.2)",
                    }}
                  >
                    <Typography sx={{ color: "#666", lineHeight: 1.6 }}>
                      {faq.answer}
                    </Typography>
                  </AccordionDetails>
                </Accordion>
              ))
            ) : (
              <Paper
                elevation={2}
                sx={{
                  p: 4,
                  textAlign: "center",
                  background: "rgba(255,255,255,0.92)",
                  borderRadius: "8px",
                }}
              >
                <Typography sx={{ color: "#999" }}>
                  No se encontraron resultados para tu búsqueda.
                </Typography>
              </Paper>
            )}
          </Box>

          {/* Resumen de categorías */}
          <Paper
            elevation={2}
            sx={{
              mt: 4,
              p: 3,
              background: "rgba(255,255,255,0.92)",
              borderRadius: "8px",
              textAlign: "center",
            }}
          >
            <Typography variant="body2" sx={{ color: "#666" }}>
              Mostrando {filteredFaqs.length} de {faqs.length} preguntas
            </Typography>
          </Paper>
        </Container>
      </Box>
    </Box>
  );
};

export default Help;
