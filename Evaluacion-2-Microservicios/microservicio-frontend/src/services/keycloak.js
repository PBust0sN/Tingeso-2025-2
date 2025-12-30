import Keycloak from "keycloak-js";

// Obtiene la configuración de Keycloak desde la ventana global o usa valores por defecto
const getKeycloakConfig = () => {
  if (typeof window !== 'undefined' && window.KEYCLOAK_CONFIG) {
    return window.KEYCLOAK_CONFIG;
  }
  
  return {
    url: "http://keycloak:8080",
    realm: "toolRent",
    clientId: "toolRent-Frontend"
  };
};

const config = getKeycloakConfig();

const keycloak = new Keycloak({
  url: config.url,
  realm: config.realm,
  clientId: config.clientId,
});

console.log('Keycloak config:', config);
console.log('Keycloak instance created:', keycloak);
console.log('Keycloak URL:', keycloak.authServerUrl);
console.log('Keycloak Realm:', keycloak.realm);
console.log('Keycloak Client ID:', keycloak.clientId);


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