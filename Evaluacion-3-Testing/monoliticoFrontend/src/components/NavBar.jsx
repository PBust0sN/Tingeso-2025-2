import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import IconButton from "@mui/material/IconButton";
import MenuIcon from "@mui/icons-material/Menu";
import React, { useState, useEffect, useRef } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { Menu, MenuItem, Breadcrumbs } from "@mui/material";
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
import NavigateNextIcon from '@mui/icons-material/NavigateNext';

export default function Navbar() {
  const [open, setOpen] = useState(false);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [tokenPayload, setTokenPayload] = useState(null);
  const [loading, setLoading] = useState(true); // Added loading state
  const [navigationHistory, setNavigationHistory] = useState([]);
  const navigate = useNavigate();
  const location = useLocation();
  const [anchorEl, setAnchorEl] = React.useState(null);
  const tokenRef = useRef(localStorage.getItem("authToken"));
  const refreshTokenRef = useRef(localStorage.getItem("refreshToken"));
  const { keycloak, initialized } = useKeycloak();

  // Función para obtener la ruta de migas de pan con navegación
  const getBreadcrumbs = () => {
    const path = location.pathname;
    const segments = path.split('/').filter(Boolean);
    
    const pageNames = {
      'home': 'Inicio',
      'help': 'Centro de ayuda',
      'admin': 'Administración',
      'tool': 'Herramientas',
      'loan': 'Préstamos',
      'record': 'Registros',
      'fine': 'Multas',
      'reports': 'Reportes',
      'myreports': 'Mis Reportes',
      'login': 'Iniciar Sesión',
      'client': 'Clientes',
      'employee': 'Staff',
      'list': 'Listado',
      'add': 'Agregar',
      'edit': 'Editar',
      'new': 'Nuevo',
      'info': 'Información',
      'return': 'Devolución',
      'select': 'Seleccionar',
      'create': 'Crear',
      'viewLoanreports': 'Ver Reportes',
      'report': 'Reporte',
      'viewrankingreport': 'Ver Ranking',
      'viewclientbehind': 'Clientes Atrasados',
      'fines': 'Multas'
    };

    if (segments.length === 0 || path === '/') {
      return [{ name: 'Inicio', path: '/home' }];
    }

    if (path === '/home') {
      return [{ name: 'Inicio', path: '/home' }];
    }

    if (path === '/help') {
      return [{ name: 'Centro de ayuda', path: '/help' }];
    }

    if (path === '/login') {
      return [{ name: 'Iniciar Sesión', path: '/login' }];
    }

    if (path === '/myreports') {
      return [{ name: 'Mis Reportes', path: '/myreports' }];
    }

    // Construir la ruta de migas de pan
    const breadcrumbs = [];
    let currentPath = '';
    
    segments.forEach((segment, index) => {
      currentPath += '/' + segment;
      const name = pageNames[segment] || segment.charAt(0).toUpperCase() + segment.slice(1);
      
      // Para parámetros dinámicos (IDs), no crear un link clickeable
      if (segment.match(/^\d+$/) || segment.match(/^[a-f0-9\-]{36}$/)) {
        breadcrumbs.push({ name: name, path: null });
      } else {
        breadcrumbs.push({ name: name, path: currentPath });
      }
    });

    return breadcrumbs;
  };

  // Efecto para rastrear el historial de navegación
  useEffect(() => {
    // Recuperar historial guardado al cargar
    const savedHistory = localStorage.getItem('navigationHistory');
    if (savedHistory && navigationHistory.length === 0) {
      try {
        setNavigationHistory(JSON.parse(savedHistory));
      } catch (e) {
        console.error('Error al recuperar historial:', e);
      }
    }
  }, []);

  // Efecto para actualizar el historial cuando cambia la ruta
  useEffect(() => {
    const currentPath = location.pathname;
    const pageNames = {
      'home': 'Inicio',
      'help': 'Centro de ayuda',
      'admin': 'Administración',
      'tool': 'Herramientas',
      'loan': 'Préstamos',
      'record': 'Registros',
      'fine': 'Multas',
      'reports': 'Reportes',
      'myreports': 'Mis Reportes',
      'login': 'Iniciar Sesión',
      'client': 'Clientes',
      'employee': 'Staff',
      'list': 'Listado',
      'add': 'Agregar',
      'edit': 'Editar',
      'new': 'Nuevo',
      'info': 'Información',
      'return': 'Devolución',
      'select': 'Seleccionar',
      'create': 'Crear'
    };

    // Construir el breadcrumb para esta ruta
    const segments = currentPath.split('/').filter(Boolean);
    const newBreadcrumbs = [{ name: 'Inicio', path: '/home' }];
    let currentBuildPath = '';

    segments.forEach((segment, index) => {
      currentBuildPath += '/' + segment;
      const name = pageNames[segment] || segment.charAt(0).toUpperCase() + segment.slice(1);
      
      // Si el segmento es 'list' y es el último, actualizar la ruta del anterior
      if (segment === 'list' && index === segments.length - 1) {
        if (newBreadcrumbs.length > 1) {
          newBreadcrumbs[newBreadcrumbs.length - 1].path = currentBuildPath;
        }
      } else if (!segment.match(/^\d+$/) && !segment.match(/^[a-f0-9\-]{36}$/)) {
        newBreadcrumbs.push({ name: name, path: currentBuildPath });
      }
    });

    // Actualizar historial y guardar en localStorage
    setNavigationHistory(newBreadcrumbs);
    localStorage.setItem('navigationHistory', JSON.stringify(newBreadcrumbs));
  }, [location.pathname]);

  // Función para determinar si un botón debe estar resaltado
  const isActive = (route) => {
    return location.pathname.startsWith(route);
  };

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
          <Box sx={{ display: "flex", alignItems: "center", justifyContent: "space-between", width: "100%", position: "relative" }}>
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
            <Box sx={{ display: "flex", alignItems: "center", flex: 1, justifyContent: "flex-start", marginX: 2 }} key={location.pathname}>
              <Breadcrumbs 
                separator={<NavigateNextIcon sx={{ fontSize: 14, color: '#fff' }} />}
                sx={{ 
                  '& .MuiBreadcrumbs-ol': { display: 'flex', gap: 0.5 }
                }}
              >
                {navigationHistory.map((crumb, index, array) => (
                  crumb.path ? (
                    <Typography
                      key={index}
                      onClick={() => navigate(crumb.path)}
                      sx={{ 
                        color: '#fff', 
                        fontSize: 13,
                        fontWeight: 400,
                        cursor: 'pointer',
                        '&:hover': {
                          textDecoration: 'underline',
                          opacity: 0.8
                        },
                        transition: 'all 0.2s ease'
                      }}
                    >
                      {crumb.name}
                    </Typography>
                  ) : (
                    <Typography
                      key={index}
                      sx={{ 
                        color: '#fff', 
                        fontSize: 13,
                        fontWeight: 400
                      }}
                    >
                      {crumb.name}
                    </Typography>
                  )
                ))}
              </Breadcrumbs>
            </Box>
            <Box sx={{ display: "flex", alignItems: "center", gap: 1 }}>
              <Button sx={{ display: "left" }} variant="text" color="inherit" onClick={() => navigate("/help")} endIcon={<HelpIcon />}>
                Centro de ayuda
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
          <Button 
            variant="text" 
            color="inherit" 
            onClick={() => navigate("/home")} 
            endIcon={<HomeIcon/>} 
            sx={{ 
              lineHeight: 1, 
              padding: "0 8px", 
              '&:focus': { outline: 'none' }, 
              '&:active': { outline: 'none' },
              backgroundColor: isActive('/home') ? 'rgba(255,255,255,0.25)' : 'transparent',
              borderBottom: isActive('/home') ? '3px solid white' : 'none',
              transition: 'all 0.3s ease'
            }} 
            disableRipple 
            disableFocusRipple
          >
            Inicio
          </Button>
          <Button
            color="inherit"
            aria-controls="user-menu"
            aria-haspopup="true"
            onClick={handleMenuOpen}
            sx={{ 
              padding: "0 8px", 
              height: "10px", 
              display: "flex", 
              alignItems: "center", 
              lineHeight: 1, 
              '&:focus': { outline: 'none' }, 
              '&:active': { outline: 'none' },
              backgroundColor: (location.pathname.includes('/admin/list') || location.pathname.includes('/employee/list') || location.pathname.includes('/client/list')) ? 'rgba(255,255,255,0.25)' : 'transparent',
              borderBottom: (location.pathname.includes('/admin/list') || location.pathname.includes('/employee/list') || location.pathname.includes('/client/list')) ? '3px solid white' : 'none',
              transition: 'all 0.3s ease'
            }}
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
          <Button 
            variant="text" 
            color="inherit" 
            onClick={() => navigate("/tool/list")} 
            endIcon={<HandymanIcon/>} 
            sx={{ 
              lineHeight: 1, 
              padding: "0 8px", 
              '&:focus': { outline: 'none' }, 
              '&:active': { outline: 'none' },
              backgroundColor: isActive('/tool') ? 'rgba(255,255,255,0.25)' : 'transparent',
              borderBottom: isActive('/tool') ? '3px solid white' : 'none',
              transition: 'all 0.3s ease'
            }} 
            disableRipple 
            disableFocusRipple
          >
            Herramientas
          </Button>
          <Button 
            variant="text" 
            color="inherit" 
            onClick={() => navigate("/loan/list")} 
            endIcon={<NoteAddIcon/>} 
            sx={{ 
              lineHeight: 1, 
              padding: "0 8px", 
              '&:focus': { outline: 'none' }, 
              '&:active': { outline: 'none' },
              backgroundColor: isActive('/loan') ? 'rgba(255,255,255,0.25)' : 'transparent',
              borderBottom: isActive('/loan') ? '3px solid white' : 'none',
              transition: 'all 0.3s ease'
            }} 
            disableRipple 
            disableFocusRipple
          >
            Prestamos
          </Button>
          <Button 
            variant="text" 
            color="inherit" 
            onClick={() => navigate("/record/list")} 
            endIcon={<BookIcon/>} 
            sx={{ 
              lineHeight: 1, 
              padding: "0 8px", 
              '&:focus': { outline: 'none' }, 
              '&:active': { outline: 'none' },
              backgroundColor: isActive('/record') ? 'rgba(255,255,255,0.25)' : 'transparent',
              borderBottom: isActive('/record') ? '3px solid white' : 'none',
              transition: 'all 0.3s ease'
            }} 
            disableRipple 
            disableFocusRipple
          >
            Registros
          </Button>
          <Button 
            variant="text" 
            color="inherit" 
            onClick={() => navigate("/fine/list")} 
            endIcon={<ReceiptIcon/>} 
            sx={{ 
              lineHeight: 1, 
              padding: "0 8px", 
              '&:focus': { outline: 'none' }, 
              '&:active': { outline: 'none' },
              backgroundColor: isActive('/fine') ? 'rgba(255,255,255,0.25)' : 'transparent',
              borderBottom: isActive('/fine') ? '3px solid white' : 'none',
              transition: 'all 0.3s ease'
            }} 
            disableRipple 
            disableFocusRipple
          >
            Multas
          </Button>
          <Button 
            variant="text" 
            color="inherit" 
            onClick={() => navigate("/reports/create")} 
            endIcon={<AssessmentIcon/>} 
            sx={{ 
              lineHeight: 1, 
              padding: "0 8px", 
              '&:focus': { outline: 'none' }, 
              '&:active': { outline: 'none' },
              backgroundColor: isActive('/reports') ? 'rgba(255,255,255,0.25)' : 'transparent',
              borderBottom: isActive('/reports') ? '3px solid white' : 'none',
              transition: 'all 0.3s ease'
            }} 
            disableRipple 
            disableFocusRipple
          >
            Reportes
          </Button>
        </Toolbar>
      </AppBar>
    </Box>
  );
}