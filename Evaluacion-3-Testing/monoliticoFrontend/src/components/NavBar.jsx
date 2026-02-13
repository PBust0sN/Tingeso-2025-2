import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import IconButton from "@mui/material/IconButton";
import MenuIcon from "@mui/icons-material/Menu";
import React, { useState, useEffect, useRef } from "react";
import { useNavigate } from "react-router-dom";
import { Menu, MenuItem } from "@mui/material";
import LogoutIcon from '@mui/icons-material/Logout';
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import PersonIcon from '@mui/icons-material/Person';
import HandymanIcon from '@mui/icons-material/Handyman';
import NoteAddIcon from '@mui/icons-material/NoteAdd';
import BookIcon from '@mui/icons-material/Book';
import ReceiptIcon from '@mui/icons-material/Receipt';
import logo from "../../public/logo.png";
import { useKeycloak } from "@react-keycloak/web";
import clientService from "../services/client.service";
import CircularProgress from "@mui/material/CircularProgress";
import FeedIcon from '@mui/icons-material/Feed';
import HelpIcon from '@mui/icons-material/Help';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import AssessmentIcon from '@mui/icons-material/Assessment';
import HomeIcon from '@mui/icons-material/Home';

export default function Navbar() {
  const [open, setOpen] = useState(false);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [tokenPayload, setTokenPayload] = useState(null);
  const [loading, setLoading] = useState(true); // Added loading state
  const navigate = useNavigate();
  const [anchorEl, setAnchorEl] = React.useState(null);
  const tokenRef = useRef(localStorage.getItem("authToken"));
  const refreshTokenRef = useRef(localStorage.getItem("refreshToken"));
  const { keycloak, initialized } = useKeycloak();

  useEffect(() => {
    const token = tokenRef.current;
    const refreshToken = refreshTokenRef.current;

    if (token) {
      try {
        const tokenPayload = JSON.parse(atob(token.split(".")[1])); // Decode JWT payload
        const isTokenExpired = tokenPayload.exp * 1000 < Date.now(); // Check if token is expired

        if (isTokenExpired && refreshToken) {
          clientService.refreshToken(refreshToken)
            .then(response => {
              localStorage.setItem("authToken", response.data.access_token);
              localStorage.setItem("refreshToken", response.data.refresh_token);
              tokenRef.current = response.data.access_token; // Update token in memory
              refreshTokenRef.current = response.data.refresh_token; // Update refresh token in memory
              setIsAuthenticated(true);
              setTokenPayload(tokenPayload);
              setLoading(false); // Set loading to false after authentication
            })
            .catch(() => {
              console.error("Failed to refresh token");
              setLoading(false); // Set loading to false even if there is an error
            });
        } else {
          setIsAuthenticated(true);
          setTokenPayload(tokenPayload);
          setLoading(false); // Set loading to false if token is valid
        }
      } catch (error) {
        console.error("Failed to parse token payload:", error);
        setLoading(false); // Set loading to false even if there is an error
      }
    } else {
      setLoading(false); // Set loading to false if no token is found
    }
  }, []);

  if (loading) {
    return (
      <Box sx={{ display: "flex", justifyContent: "center", alignItems: "center", height: "100vh" }}>
        <CircularProgress />
      </Box>
    );
  }

  const handleMenuOpen = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };
  
  const toggleDrawer = (open) => (event) => {
    setOpen(open);
  };

  const clearAllCookies = () => {
    const cookies = document.cookie.split("; ");
    for (const c of cookies) {
      const eqPos = c.indexOf("=");
      const name = eqPos > -1 ? c.substr(0, eqPos) : c;
      document.cookie = `${name}=;expires=Thu, 01 Jan 1970 00:00:00 GMT;path=/`;
      document.cookie = `${name}=;expires=Thu, 01 Jan 1970 00:00:00 GMT;path=/;domain=${window.location.hostname}`;
    }
  };

  const handleLogout = () => {
    try {
      // Clear tokens from localStorage
      localStorage.removeItem("authToken");
      localStorage.removeItem("refreshToken");
      clearAllCookies();
      // Redirect to the login page or home
      window.location.href = "/login";
    } catch (error) {
      console.error("Error during logout:", error);
    }
  };

  return (
    <Box sx={{ 
      flexGrow: 1,
      /* Added margin-bottom to ensure space for SecondaryNavBar */
    }}>
      <AppBar position="fixed" sx={{ backgroundColor: "#1976d2aa", color: "#fff", zIndex: 1300, width: "100%" }}> {/* Ensured full width */}
        <Toolbar>
          <Box sx={{ display: "flex", alignItems: "center", justifyContent: "space-between", width: "100%" }}>
            <Box sx={{ display: "flex", alignItems: "center", position: "relative" }}>
              <Box
                sx={{ position: "absolute", top: 0, left: 0, width: 115, height: 40, cursor: "pointer", zIndex: 1000}}
                onClick={() => navigate("/home")}
              />
              <img src={logo} alt="ToolRent Logo" style={{ width: "30px", height: "30px", marginRight: "8px" }} />
              <Typography variant="h6" component="div">
                ToolRent
              </Typography>
            </Box>
            <Box sx={{ display: "flex", alignItems: "center", gap: 1 }}>
              <Button sx={{ display: "left" }} variant="text" color="inherit" onClick={() => navigate("/help")} endIcon={<HelpIcon />}>
                Ayuda
              </Button>
              {initialized && (
                <>
                  {isAuthenticated ? (
                    <Box sx={{ display: "flex", alignItems: "center" }}>
                      <Button variant="text" color="inherit" onClick={() => navigate("/myreports")} endIcon={<FeedIcon />}>
                        Mis Reportes
                      </Button>
                      <Typography sx={{ ml: 2 }}>
                            Hola!, {tokenPayload?.username || tokenPayload?.email}
                      </Typography>
                      <IconButton color="inherit" onClick={handleLogout}>
                        <LogoutIcon />
                      </IconButton>
                    </Box>
                  ) : (
                    <Button variant="text" color="inherit" onClick={() => navigate("/login")} endIcon={<AccountCircleIcon />}>
                      Iniciar Sesión
                    </Button>
                  )}
                </>
              )}
            </Box>
          </Box>
        </Toolbar>
        <hr style={{ margin: 0, border: "1px solid #fff" }} /> {/* Added horizontal line */}
        <Toolbar sx={{ minHeight: "35px!important", height: 35, display: "flex", justifyContent: "center", backgroundColor: "#1976d2aa", height: 40, gap: "40px" }}> {/* Centered buttons with spacing */}
          <Button variant="text" color="inherit" onClick={() => navigate("/home")} endIcon={
              <HomeIcon/>
            } sx={{ 
    lineHeight: 1, 
    padding: "0 8px", 
    '&:focus': { outline: 'none' }, 
    '&:active': { outline: 'none' } 
  }} 
  disableRipple 
  disableFocusRipple>Inicio</Button>
          <Button
            color="inherit"
            aria-controls="user-menu"
            aria-haspopup="true"
            onClick={handleMenuOpen}
            sx={{ padding: "0 8px", height: "10px", display: "flex", alignItems: "center", lineHeight: 1, 
              padding: "0 8px", 
              '&:focus': { outline: 'none' }, 
              '&:active': { outline: 'none' }  }}
            endIcon={
              <ExpandMoreIcon
                sx={{
                  transform: anchorEl ? "rotate(180deg)" : "rotate(0deg)",
                  transition: "transform 0.3s ease-in-out",
                }}
              />
            }
          >
            Usuarios
          </Button>
          <Menu
            id="user-menu"
            anchorEl={anchorEl}
            open={Boolean(anchorEl)}
            onClose={handleMenuClose}
          >
            <MenuItem onClick={() => { handleMenuClose(); navigate("/admin/list"); }}>Admin</MenuItem>
            <MenuItem onClick={() => { handleMenuClose(); navigate("/employee/list"); }}>Staff</MenuItem>
            <MenuItem onClick={() => { handleMenuClose(); navigate("/client/list"); }}>Clients</MenuItem>
          </Menu>
          <Button variant="text" color="inherit" onClick={() => navigate("/tool/list")} endIcon={
              <HandymanIcon/>
            } sx={{ 
    lineHeight: 1, 
    padding: "0 8px", 
    '&:focus': { outline: 'none' }, 
    '&:active': { outline: 'none' } 
  }} 
  disableRipple 
  disableFocusRipple>Herramientas</Button>
          <Button variant="text" color="inherit" onClick={() => navigate("/loan/list")} endIcon={
              <NoteAddIcon/>
            } sx={{ 
    lineHeight: 1, 
    padding: "0 8px", 
    '&:focus': { outline: 'none' }, 
    '&:active': { outline: 'none' } 
  }} 
  disableRipple 
  disableFocusRipple>Prestamos</Button>
          <Button variant="text" color="inherit" onClick={() => navigate("/record/list")} endIcon={
              <BookIcon/>
            } sx={{ 
    lineHeight: 1, 
    padding: "0 8px", 
    '&:focus': { outline: 'none' }, 
    '&:active': { outline: 'none' } 
  }} 
  disableRipple 
  disableFocusRipple>Historial</Button>
          <Button variant="text" color="inherit" onClick={() => navigate("/fine/list")} endIcon={
              <ReceiptIcon/>
            } sx={{ 
    lineHeight: 1, 
    padding: "0 8px", 
    '&:focus': { outline: 'none' }, 
    '&:active': { outline: 'none' } 
  }} 
  disableRipple 
  disableFocusRipple>Multas</Button>
  <Button variant="text" color="inherit" onClick={() => navigate("/reports/create")} endIcon={
              <AssessmentIcon/>
            } sx={{ 
    lineHeight: 1, 
    padding: "0 8px", 
    '&:focus': { outline: 'none' }, 
    '&:active': { outline: 'none' } 
  }} 
  disableRipple 
  disableFocusRipple>Reportes</Button>
        </Toolbar>
      </AppBar>
    </Box>
  );
}