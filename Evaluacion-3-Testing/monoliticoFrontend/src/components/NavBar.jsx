import * as React from "react";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import IconButton from "@mui/material/IconButton";
import MenuIcon from "@mui/icons-material/Menu";
import { useState } from "react";
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

export default function Navbar() {
  const [open, setOpen] = useState(false);
  const { keycloak, initialized } = useKeycloak();
  const navigate = useNavigate();
  const [anchorEl, setAnchorEl] = React.useState(null);

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
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            ToolRent: Sistema de Renta De Herramientas
          </Typography>
          {initialized && (
            <>
              {keycloak.authenticated ? (
                <>
                  <Typography sx={{ mr: 2 }}>
                    {keycloak.tokenParsed?.username ||
                      keycloak.tokenParsed?.email}
                  </Typography>
                  <IconButton color="inherit" onClick={handleLogout}>
                    <LogoutIcon />
                  </IconButton>
                </>
              ) : (
                <Button variant="outlined" color="inherit" onClick={() => navigate("/login")}>
                  Login
                </Button>
              )}
            </>
          )}
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