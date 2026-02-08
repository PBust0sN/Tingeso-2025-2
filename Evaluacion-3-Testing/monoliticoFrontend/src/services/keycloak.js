import Keycloak from "keycloak-js";
const keycloakUrl = import.meta.env.VITE_KEYCLOAK_URL || "http://localhost:8080";

const keycloak = new Keycloak({
  url: keycloakUrl,
  realm: "toolRent",
  clientId: "toolRent-Frontend",
});

console.log('Keycloak instance created:', keycloak);


export function initKeycloak(onAuthenticatedCallback) {
  keycloak.init({ onLoad: 'login-required', checkLoginIframe: false })
    .then(authenticated => {
      if (!authenticated) {
        console.warn('User is not authenticated');
      } else {
        console.log('Authenticated!', keycloak.token);
      }
      onAuthenticatedCallback();
    })
    .catch(err => {
      console.error('Keycloak init failed', err);
    });
}

export default keycloak;