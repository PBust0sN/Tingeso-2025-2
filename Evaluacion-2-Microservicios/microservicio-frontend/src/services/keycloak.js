import Keycloak from "keycloak-js";

const keycloak = new Keycloak({
  url: "http://keycloak:8080",
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