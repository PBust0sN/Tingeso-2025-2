import Keycloak from "keycloak-js";

// Detectar ambiente por hostname
const isLocalhost = window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1';
const keycloakUrl = isLocalhost 
  ? "http://localhost:8080"
  : "https://192.168.39.192:30443";

const keycloakConfig = {
  url: keycloakUrl,
  realm: "toolRent",
  clientId: "toolRent-Frontend",
};

console.log('[Keycloak] URL:', keycloakUrl);
console.log('[Keycloak] Config:', keycloakConfig);

const keycloak = new Keycloak(keycloakConfig);

console.log('[Keycloak] Instance creada');

export default keycloak;