import * as React from "react";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import IconButton from "@mui/material/IconButton";
import MenuIcon from "@mui/icons-material/Menu";
import { useState, useEffect } from "react";
import { useKeycloak } from "@react-keycloak/web";
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

export default function Navbar() {
  const [open, setOpen] = useState(false);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [tokenPayload, setTokenPayload] = useState(null);
  const [AuthUser, setAuthUser] = useState(null);
  const { keycloak, initialized } = useKeycloak();
  const navigate = useNavigate();
  const [anchorEl, setAnchorEl] = React.useState(null);

  useEffect(() => {
    const token = localStorage.getItem("authToken");
    const AuthUser = localStorage.getItem("authenticatedUser");
    setAuthUser(AuthUser ? JSON.parse(AuthUser) : null);
    if (token) {
        try {
            const tokenPayload = JSON.parse(atob(token.split(".")[1])); // Decode JWT payload
            const isTokenExpired = tokenPayload.exp * 1000 < Date.now(); // Verificar si el token ha expirado

            if (isTokenExpired) {
                console.warn("Token expirado");
                setIsAuthenticated(false);
                localStorage.removeItem("authToken");
                localStorage.removeItem("refreshToken");
            } else {
                setIsAuthenticated(true);
                setTokenPayload(tokenPayload); // Assuming you have a state to store user info
            }
        } catch (error) {
            console.error("Failed to parse token payload:", error);
        }
    }
  }, []);

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
      try { localStorage.clear(); } catch (e) {}
      try { sessionStorage.clear(); } catch (e) {}
      clearAllCookies();
    } finally {
      if (keycloak && typeof keycloak.logout === "function") {
        keycloak.logout({ redirectUri: window.location.origin });
      } else {
        window.location.href = "/";
      }
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
            <Box sx={{ display: "flex", alignItems: "center" }}>
              <img src={logo} alt="ToolRent Logo" style={{ width: "30px", height: "30px", marginRight: "8px" }} />
              <Typography variant="h6" component="div">
                ToolRent
              </Typography>
            </Box>
            {initialized && (
              <>
                {isAuthenticated ? (
                  <Box sx={{ display: "flex", alignItems: "center" }}>
                    <Typography sx={{ mr: 2 }}>
                      {AuthUser?.username ||AuthUser?.email}
                    </Typography>
                    <IconButton color="inherit" onClick={handleLogout}>
                      <LogoutIcon />
                    </IconButton>
                  </Box>
                ) : (
                  <Button variant="outlined" color="inherit" onClick={() => navigate("/login")}>
                    Login
                  </Button>
                )}
              </>
            )}
          </Box>
        </Toolbar>
        <hr style={{ margin: 0, border: "1px solid #fff" }} /> {/* Added horizontal line */}
        <Toolbar sx={{ minHeight: "35px!important", height: 35, display: "flex", justifyContent: "center", backgroundColor: "#1976d2aa", height: 40, gap: "40px" }}> {/* Centered buttons with spacing */}
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
  disableFocusRipple>Records</Button>
          <Button variant="text" color="inherit" onClick={() => navigate("/fine/list")} endIcon={
              <ReceiptIcon/>
            } sx={{ 
    lineHeight: 1, 
    padding: "0 8px", 
    '&:focus': { outline: 'none' }, 
    '&:active': { outline: 'none' } 
  }} 
  disableRipple 
  disableFocusRipple>Fines</Button>
        </Toolbar>
      </AppBar>
    </Box>
  );
}