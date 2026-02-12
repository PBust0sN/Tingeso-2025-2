import { useState, useEffect, useRef } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import Paper from "@mui/material/Paper";
import toolsService from "../services/tools.service";
import loansService from "../services/loans.service";
import imagesService from "../services/images.service";
import Typography from "@mui/material/Typography";
import SaveIcon from "@mui/icons-material/Save";
import AddCircleIcon from "@mui/icons-material/AddCircle";
import TextField from '@mui/material/TextField';
import CircularProgress from "@mui/material/CircularProgress";
import Pagination from "@mui/material/Pagination";

const CARD_WIDTH = 200;
const CARD_HEIGHT = 270;
const TITLE_FONT_SIZE = "1.15rem";


const NewLoan = () => {
  const { client_id } = useParams();
  const [days, setDays] = useState("");
  const [toolOptions, setToolOptions] = useState([]);
  const [selectedTools, setSelectedTools] = useState([]);
  const [imageMap, setImageMap] = useState({});
  const [loading, setLoading] = useState(true);
  const [pageLoading, setPageLoading] = useState(false);
  const [savingLoan, setSavingLoan] = useState(false);
  const [currentPage, setCurrentPage] = useState(1);
  const navigate = useNavigate();
  const ITEMS_PER_PAGE = 5;

  const tokenRef = useRef(localStorage.getItem("authToken"));
  const token = tokenRef.current;
  if (!token) {
    console.error("No auth token found in memory.");
    navigate("/login");
    return null;
  }

  const tokenPayload = JSON.parse(atob(token.split(".")[1]));
  const staff_id = Number(tokenPayload?.id_real);
  const clientNumber = Number(client_id);

  useEffect(() => {
    toolsService
      .getAll()
      .then((response) => {
        setToolOptions(response.data);
        setLoading(false);
      })
      .catch((error) => {
        console.log("Error al obtener herramientas:", error);
        setLoading(false);
      });
  }, []);

  // Cargar imágenes solo para la página actual
  useEffect(() => {
    if (toolOptions.length === 0) return;

    setPageLoading(true);

    const startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
    const endIndex = startIndex + ITEMS_PER_PAGE;
    const currentPageTools = toolOptions.slice(startIndex, endIndex);

    // Cargar imágenes en paralelo para la página actual
    const imagePromises = currentPageTools.map((tool) =>
      imagesService
        .getImage(`${tool.toolId}.png`)
        .then((imageResponse) => ({
          toolId: tool.toolId,
          url: URL.createObjectURL(imageResponse.data),
        }))
        .catch((error) => {
          console.log(`Error al cargar imagen para herramienta ${tool.toolId}:`, error);
          return null;
        })
    );

    Promise.all(imagePromises).then((results) => {
      const newImageMap = {};
      results.forEach((result) => {
        if (result) {
          newImageMap[result.toolId] = result.url;
        }
      });
      // Actualizar acumulativamente (mantener imágenes anteriores)
      setImageMap((prev) => ({
        ...prev,
        ...newImageMap,
      }));
      setPageLoading(false);
    });
  }, [currentPage, toolOptions]);

  if (loading) {
    return (
      <Box sx={{ display: "flex", justifyContent: "center", alignItems: "center", height: "100vh" }}>
        <CircularProgress />
      </Box>
    );
  }

  // select/deselect tool on click
  const handleToolClick = (toolId) => {
    const tool = toolOptions.find(t => t.toolId === toolId);
    if (!tool) return;
    // evitate selecting if out of stock
    if (!(tool.stock > 0)) return;

    setSelectedTools((prev) =>
      prev.includes(toolId)
        ? prev.filter((id) => id !== toolId)
        : [...prev, toolId]
    );
  };

  const saveLoan = (e) => {
    e.preventDefault();
    setSavingLoan(true);
    const loan = {
      staff_id: staff_id,
      client_id: clientNumber,
      days: Number(days),
      tools_id: selectedTools,
    };
    loansService
      .newLoan(loan)
      .then((response) => {
        // suposing that if response.data is an empty array, no errors occurred
        if (Array.isArray(response.data) && response.data.length === 0) {
          window.alert("Préstamo añadido exitosamente");
          navigate("/client/list");
        } else if (Array.isArray(response.data)) {
          // show the list of errors 
          window.alert("Errores:\n" + response.data.join("\n"));
          setSavingLoan(false);
          navigate(-1);
        } else {
          // if the response is not as expected 
          window.alert("Respuesta inesperada del servidor.");
          setSavingLoan(false);
        }
      })
      .catch((error) => {
        window.alert("Ha ocurrido un error al intentar crear nuevo préstamo.");
        console.log("Ha ocurrido un error al intentar crear nuevo préstamo.", error);
        setSavingLoan(false);
      });
  };

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
      {/* main frame of tools */}
      <Box
        sx={{
          position: "relative",
          zIndex: 1,
          minHeight: "100vh",
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          justifyContent: "flex-start",
          pt: 6,
        }}
      >
        <Paper
          elevation={6}
          sx={{
            p: 4,
            width: "90vw",
            maxWidth: "1400px",
            minHeight: "70vh",
            background: "rgba(255,255,255,0.95)",
            color: "#222",
            mb: 3,
          }}
        >
          <Box sx={{ mb: 4, display: "flex", justifyContent: "space-between", alignItems: "center" }}>
            <Typography variant="h5" sx={{ fontWeight: "bold" }}>
              Selecciona Herramientas para el Préstamo
            </Typography>
            <TextField
              id="filled-search"
              label="Días de préstamo"
              type="number"
              variant="filled"
              value={days}
              onChange={e => setDays(e.target.value)}
              sx={{ width: 260 }}
              inputProps={{ min: 1 }}
              required
            />
          </Box>
          <Box
            sx={{
              display: "flex",
              flexWrap: "wrap",
              gap: 3,
              justifyContent: "center",
              alignItems: "flex-start",
              minHeight: "50vh",
            }}
          >
            {pageLoading ? (
              <Box sx={{ display: "flex", justifyContent: "center", alignItems: "center", width: "100%", minHeight: "50vh" }}>
                <CircularProgress />
              </Box>
            ) : (
              toolOptions.slice((currentPage - 1) * ITEMS_PER_PAGE, currentPage * ITEMS_PER_PAGE).map((tool) => {
                const outOfStock = !(tool.stock > 0);
                const isSelected = selectedTools.includes(tool.toolId);
                return (
                  <Paper
                    key={tool.toolId}
                    elevation={isSelected ? 8 : 2}
                    sx={{
                      width: `${CARD_WIDTH}px`,
                      minWidth: `${CARD_WIDTH}px`,
                      maxWidth: `${CARD_WIDTH}px`,
                      height: `${CARD_HEIGHT}px`,
                    minHeight: `${CARD_HEIGHT}px`,
                    maxHeight: `${CARD_HEIGHT}px`,
                    display: "flex",
                    flexDirection: "column",
                    justifyContent: "flex-start",
                    alignItems: "stretch",
                    p: 2,
                    border: isSelected ? "2px solid #1976d2" : "2px solid transparent",
                    background: isSelected ? "rgba(25, 118, 210, 0.08)" : "white",
                    transition: "border 0.2s, background 0.2s",
                    opacity: outOfStock ? 0.6 : 1,
                    cursor: outOfStock ? "not-allowed" : "pointer",
                  }}
                  onClick={() => !outOfStock && handleToolClick(tool.toolId)}
                >
                  <Box
                    sx={{
                      width: "100%",
                      height: 120,
                      mb: 2,
                      background: "#eee",
                      borderRadius: "12px",
                      overflow: "hidden",
                      display: "flex",
                      alignItems: "center",
                      justifyContent: "center",
                    }}
                  >
                    <img
                      src={imageMap[tool.toolId] || "/default-avatar.png"}
                      alt={tool.tool_name}
                      style={{
                        width: "100%",
                        height: "100%",
                        objectFit: "cover",
                      }}
                    />
                  </Box>
                  <Typography
                    variant="h6"
                    sx={{
                      fontWeight: "bold",
                      mb: 1,
                      textAlign: "left",
                      fontSize: TITLE_FONT_SIZE,
                      width: "100%",
                    }}
                  >
                    {tool.tool_name}
                  </Typography>
                  <Box sx={{ display: "flex", alignItems: "center", width: "100%", justifyContent: "space-between", mt: 1 }}>
                    <Box sx={{ textAlign: "left" }}>
                      <Typography variant="body2" color="text.secondary">
                        {tool.category}
                      </Typography>

                      <Typography variant="h6" 
                      sx={{
                      fontWeight: "bold",
                      mb: 1,
                      textAlign: "left",
                      fontSize: TITLE_FONT_SIZE,
                      width: "100%",
                    }}>
                        
                      </Typography>
                      <Typography variant="body2" color="text.secondary">
                        Stock: {tool.stock}
                      </Typography>
                    </Box>
                    <Button
                      color={isSelected ? "primary" : "inherit"}
                      onClick={(e) => {
                        e.stopPropagation();
                        handleToolClick(tool.toolId);
                      }}
                      sx={{ minWidth: 0, ml: 2 }}
                      disabled={outOfStock}
                      title={outOfStock ? "Sin stock" : undefined}
                    >
                      <AddCircleIcon />
                    </Button>
                  </Box>
                </Paper>
              );
              })
            )}
          </Box>
          <Box sx={{ mt: 4, display: "flex", justifyContent: "center" }}>
            <Pagination
              count={Math.ceil(toolOptions.length / ITEMS_PER_PAGE)}
              page={currentPage}
              onChange={(event, newPage) => setCurrentPage(newPage)}
              variant="outlined"
              shape="rounded"
              color="primary"
            />
          </Box>
          <Box sx={{ mt: 4, display: "flex", justifyContent: "center", gap: 2, mb: 2 }}>
            <Button
              variant="contained"
              color="info"
              onClick={saveLoan}
              disabled={!days || selectedTools.length === 0 || savingLoan}
              startIcon={savingLoan ? <CircularProgress size={20} /> : <SaveIcon />}
            >
              {savingLoan ? "Guardando..." : "Grabar Préstamo"}
            </Button>
            <Link to="/client/list" style={{ textDecoration: "none", pointerEvents: savingLoan ? "none" : "auto" }}>
              <Button variant="outlined" color="primary" disabled={savingLoan}>
                Volver al Listado
              </Button>
            </Link>
          </Box>
        </Paper>
      </Box>
    </Box>
  );
};

export default NewLoan;