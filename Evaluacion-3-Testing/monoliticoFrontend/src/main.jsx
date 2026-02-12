import ReactDOM from 'react-dom/client'
import App from './App.jsx'
import './index.css'
import { ReactKeycloakProvider } from "@react-keycloak/web";
import keycloak from "./services/keycloak";
import { BrowserRouter } from "react-router-dom";
import Clarity from '@microsoft/clarity';

const projectId = "vfdtj40ou9"

// Clarity.init(projectId);

console.log('Clarity initialized with project ID:', projectId);

console.log('Keycloak instance at index:', keycloak);

ReactDOM.createRoot(document.getElementById('root')).render(
  <ReactKeycloakProvider authClient={keycloak}>
    <App />
  </ReactKeycloakProvider>
)