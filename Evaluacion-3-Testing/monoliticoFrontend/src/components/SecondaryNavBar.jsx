import * as React from "react";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import Button from "@mui/material/Button";
import { useNavigate } from "react-router-dom";
import { Menu, MenuItem } from "@mui/material";

export default function SecondaryNavBar() {
  console.log("SecondaryNavBar se ha montado correctamente"); // Debugging message

  const navigate = useNavigate();
  const [anchorEl, setAnchorEl] = React.useState(null);

  const handleMenuOpen = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  return (
    <Box sx={{ flexGrow: 1, height:"10%"}}> {/* Ensured full width */}
      <AppBar position="fixed" sx={{ backgroundColor: "#1976d2", color: "#fff", zIndex: 1300, width: "100%", mt: 6, height:"10%" }}> {/* Adjusted height */}
        <Toolbar sx={{ height:"10%", display: "flex", alignItems: "center" }}> {/* Centered content */}
          <Button
            color="inherit"
            aria-controls="user-menu"
            aria-haspopup="true"
            onClick={handleMenuOpen}
            sx={{ padding: "0 8px", height: "10px", display: "flex", alignItems: "center" }}
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

          <Button color="inherit" onClick={() => navigate("/tool/list")} sx={{ padding: "0 8px", display: "flex", alignItems: "center" }}>Herramientas</Button>
          <Button color="inherit" onClick={() => navigate("/loan/list")} sx={{ padding: "0 8px", display: "flex", alignItems: "center" }}>Prestamos</Button>
          <Button color="inherit" onClick={() => navigate("/record/list")} sx={{ padding: "0 8px", display: "flex", alignItems: "center" }}>Records</Button>
          <Button color="inherit" onClick={() => navigate("/fine/list")} sx={{ padding: "0 8px", display: "flex", alignItems: "center" }}>Fines</Button>
        </Toolbar>
      </AppBar>
    </Box>
  );
}