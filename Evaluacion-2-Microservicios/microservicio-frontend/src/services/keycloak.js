import Keycloak from "keycloak-js";

const keycloak = new Keycloak({
  url: process.env.REACT_APP_KEYCLOAK_URL || "http://keycloak:8080",
  realm: process.env.REACT_APP_KEYCLOAK_REALM || "toolRent",
  clientId: process.env.REACT_APP_KEYCLOAK_CLIENT_ID || "toolRent-Frontend",
});

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