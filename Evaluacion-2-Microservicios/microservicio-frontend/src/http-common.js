import axios from "axios";
import keycloak from "./services/keycloak";

// Usar / como baseURL - nginx lo proxy a gateway-service:8433
// El navegador no necesita conocer la URL absoluta, nginx maneja el proxy
const baseURL = '/';

const api = axios.create({
    baseURL: baseURL,
    headers: {
        'Content-Type': 'application/json'
    }
});

console.log('API Gateway URL configurada:', baseURL);

/**
 * Interceptor de peticiones para agregar el token JWT de Keycloak
 */
api.interceptors.request.use(async (config) => {
  try {
    // Si es FormData, no forzar Content-Type para que axios maneje el boundary
    if (config.data instanceof FormData) {
      delete config.headers['Content-Type'];
    }

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