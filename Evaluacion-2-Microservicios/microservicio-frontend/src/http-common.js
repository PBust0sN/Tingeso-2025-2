import axios from "axios";
import keycloak from "./services/keycloak";

const monoliticoBackendServer = import.meta.env.VITE_MONOLITICO_BACKEND_SERVER;
const monoliticoBackendport = import.meta.env.VITE_MONOLITICO_BACKEND_PORT;

console.log('Backend Server:', monoliticoBackendServer);
console.log('Backend Port:', monoliticoBackendport);

const api = axios.create({
    baseURL: `http://${monoliticoBackendServer}:${monoliticoBackendport}`,
    headers: {
        'Content-Type': 'application/json'
    }
});

/**
 * Interceptor de peticiones para agregar el token JWT de Keycloak
 */
api.interceptors.request.use(async (config) => {
  try {
    if (keycloak && keycloak.authenticated) {
      // Actualizar token si está por expirar en 30 segundos
      const refreshed = await keycloak.updateToken(30);
      if (refreshed) {
        console.log('Token refreshed successfully');
      }
      
      // Agregar el token en el header Authorization
      if (keycloak.token) {
        config.headers.Authorization = `Bearer ${keycloak.token}`;
        console.log('Token added to request:', {
          endpoint: config.url,
          tokenLength: keycloak.token.length,
          tokenExpiry: new Date(keycloak.tokenParsed.exp * 1000)
        });
      } else {
        console.warn('Keycloak authenticated but no token available');
      }
    } else {
      console.warn('Keycloak not initialized or user not authenticated');
    }
  } catch (error) {
    console.error('Error updating token:', error);
    // Si hay error refrescando el token, hacer logout
    if (keycloak && typeof keycloak.logout === 'function') {
      keycloak.logout({ redirectUri: window.location.origin });
    }
    return Promise.reject(error);
  }
  return config;
}, (error) => {
  console.error('Request interceptor error:', error);
  return Promise.reject(error);
});

/**
 * Interceptor de respuestas para manejar errores de autenticación
 */
api.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    // Si recibimos 401, el token no es válido
    if (error.response?.status === 401) {
      console.error('Unauthorized (401) - Token may be invalid');
      if (keycloak && typeof keycloak.logout === 'function') {
        keycloak.logout({ redirectUri: window.location.origin });
      }
    }
    // Si recibimos 403, el usuario no tiene permisos
    if (error.response?.status === 403) {
      console.error('Forbidden (403) - User does not have permission');
    }
    return Promise.reject(error);
  }
);

export default api;