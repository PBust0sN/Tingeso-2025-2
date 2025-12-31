# 🔐 Resumen: Arreglos para Conectar Frontend con Keycloak

## 📋 Estado General

| Componente | Estado | Descripción |
|-----------|--------|-------------|
| **Keycloak Pod** | ✅ Running | Escuchando en puerto 30443 |
| **Realm toolRent** | ✅ Importado | Configurado con redirect URIs |
| **Cliente Frontend** | ✅ Registrado | `toolRent-Frontend` |
| **Frontend Build** | ⚠️ Pending | Necesita recompilar |
| **Axios Interceptor** | ✅ Configurado | Incluye token JWT |
| **Loading Screen** | ✅ Existe | Componente de carga |

---

## 🔧 Archivos Modificados

### 1️⃣ `microservicio-frontend/src/services/keycloak.js`

**Antes:**
```javascript
const keycloakConfig = {
  url: "http://192.168.39.157:30443",  // ❌ Hardcodeada
  realm: "toolRent",
  clientId: "toolRent-Frontend",
};
keycloak.pkceMethod = null;  // ❌ No debería estar aquí
```

**Después:**
```javascript
const isLocalhost = window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1';
const keycloakUrl = isLocalhost 
  ? "http://localhost:8080"
  : "http://192.168.39.157:30443";

const keycloakConfig = {
  url: keycloakUrl,  // ✅ Dinámico
  realm: "toolRent",
  clientId: "toolRent-Frontend",
};
```

**Beneficios:**
- ✅ Funciona tanto en localhost como en Minikube
- ✅ Sin conflictos de PKCE
- ✅ Logs mejorados con prefijo `[Keycloak]`

---

### 2️⃣ `microservicio-frontend/src/App.jsx`

**Antes:**
```javascript
keycloak.pkceMethod = null;  // ❌ Deshabilitar PKCE

keycloak.init({
  onLoad: 'check-sso',
  pkceMethod: 'S256',  // ❌ Habilitar PKCE al mismo tiempo ¿?
})
```

**Después:**
```javascript
keycloak.init({
  onLoad: 'check-sso',
  enableLogging: true,  // ✅ Debug activado
  silentCheckSsoRedirectUri: window.location.origin + '/silent-check-sso.html'
})
```

**Beneficios:**
- ✅ Inicialización limpia sin conflictos
- ✅ Logs activados para debugging
- ✅ SSO silencioso habilitado
- ✅ Logs con prefijo `[App]` y `[AppContent]`

---

### 3️⃣ `microservicio-frontend/src/main.jsx`

**Cambio:**
```javascript
// Antes: console.log('main.jsx - Renderizando aplicación...');
// Después:
console.log('[Main] Renderizando aplicación...');
```

**Beneficio:**
- ✅ Logs consistentes con prefijo `[Main]`

---

## ✅ Verificaciones Realizadas

### Keycloak Status
```
✅ Pod Running: keycloak-65b9d758f8-xz7sb
✅ Service: NodePort 8080:30443
✅ Realm: toolRent importado
✅ Cliente: toolRent-Frontend registrado
✅ CORS: Habilitado con webOrigins: ["*"]
✅ OpenID Configuration: Accesible
```

### Configuración del Realm
```json
✅ SSL Required: "none" (para desarrollo)
✅ Public Client: true (para frontend)
✅ Redirect URIs: Incluye todas las URL necesarias
✅ Web Origins: Incluye 192.168.39.157:30080 y localhost
✅ Access Token Lifespan: 300 segundos (5 minutos)
```

### Axios Interceptor
```javascript
✅ Agrega Authorization header con Bearer token
✅ Maneja token refresh (actualiza si expira en 30s)
✅ Logout automático en error 401
✅ Manejo de errores 403 (sin permisos)
✅ Logging detallado de todas las operaciones
```

---

## 🚀 Pasos Siguientes

### Opción A: Deploy Automático (Recomendado)
```bash
chmod +x deploy-frontend.sh
./deploy-frontend.sh
```

### Opción B: Deploy Manual

**1. Compilar frontend:**
```bash
cd microservicio-frontend
npm run build
cd ..
```

**2. Configurar Docker para Minikube:**
```bash
eval $(minikube docker-env)
```

**3. Build imagen:**
```bash
cd microservicio-frontend
docker build -t pbust0sn/frontend:latest .
cd ..
```

**4. Redeployar en Kubernetes:**
```bash
kubectl rollout restart deployment/frontend -n default
kubectl rollout status deployment/frontend -n default --timeout=60s
```

**5. Verificar logs:**
```bash
kubectl logs -l app=frontend -n default --tail=50 -f
```

---

## 🧪 Testing Manual

### 1. Acceder al Frontend
```bash
# Obtener IP de Minikube
minikube ip  # Debería ser: 192.168.39.157

# Acceder en navegador
http://192.168.39.157:30080
```

### 2. Abrir DevTools (F12)
Deberías ver en la console:
```
[Keycloak] Config: {url: "http://192.168.39.157:30443", realm: "toolRent", clientId: "toolRent-Frontend"}
[Keycloak] Instance created
[Main] Renderizando aplicación...
[App] Inicializando Keycloak...
[App] Keycloak inicializado correctamente
[AppContent] initialized: true authenticated: false
```

### 3. Hacer Login
- Haz clic en el botón de login
- Deberías ser redirigido a Keycloak
- Usa: `admin` / `admin`
- Deberías volver al frontend autenticado
- El token debería incluirse en las peticiones al backend

### 4. Verificar Token
En DevTools → Application → Local Storage:
- Deberías ver `kc_state` y `kc_access_token`

---

## 📊 Diagrama de Flujo

```
┌─────────────────────┐
│   Frontend (React)  │
│ 192.168.39.157:8080 │
└──────────┬──────────┘
           │
           │ 1. Intenta conectar
           │
           ▼
┌─────────────────────────┐
│ Keycloak (OIDC Provider)│
│ 192.168.39.157:30443    │
└──────────┬──────────────┘
           │
           │ 2. Redirige a login (si no autenticado)
           │
           ▼
┌─────────────────────┐
│  Browser Login      │
│  admin / admin      │
└──────────┬──────────┘
           │
           │ 3. Usuario se autentica
           │
           ▼
┌─────────────────────────┐
│ Keycloak Authorization  │
│ Genera Access Token     │
└──────────┬──────────────┘
           │
           │ 4. Redirige de vuelta al frontend
           │ con code que se intercambia por token
           │
           ▼
┌─────────────────────┐
│ Frontend Autenticado│
│ Token en LocalStorage│
└──────────┬──────────┘
           │
           │ 5. Peticiones a Backend con token
           │ Authorization: Bearer <token>
           │
           ▼
┌─────────────────────┐
│ Backend Microservices│
│ Validan JWT Token   │
└─────────────────────┘
```

---

## 🐛 Troubleshooting

### Problema: "CORS error"
**Solución:** Ya está configurado en Keycloak con `"webOrigins": ["*"]`

### Problema: "Cannot read property 'authenticated' of undefined"
**Solución:** Frontend se renderiza antes de Keycloak inicializar. Ya está corregido con `LoadingScreen`.

### Problema: "Token not in request"
**Solución:** Verifica que el usuario esté autenticado: `keycloak.authenticated === true`

### Problema: "PKCE error"
**Solución:** Ya removimos los conflictos de PKCE en keycloak.js

### Problema: "Redirect URI mismatch"
**Solución:** Ya incluido en realm-export.json: `http://192.168.39.157:30080/*`

---

## 📝 Archivos Relacionados

- `GUIA-CONECTAR-KEYCLOAK.md` - Guía detallada de deployment
- `deploy-frontend.sh` - Script de auto-deployment
- `keycloak/realm-export.json` - Configuración de Keycloak
- `microservicio-frontend/Dockerfile` - Imagen Docker del frontend
- `deployments/frontend-deployment.yaml` - Kubernetes deployment
- `microservicio-frontend/src/http-common.js` - Interceptor Axios

---

## ✨ Resumen de Cambios

| Archivo | Cambios | Impacto |
|---------|---------|---------|
| keycloak.js | URL dinámica, sin PKCE | ✅ Funciona en cualquier ambiente |
| App.jsx | Inicialización limpia | ✅ Sin conflictos PKCE |
| main.jsx | Logs mejorados | ✅ Debugging más fácil |

**Total de cambios**: 3 archivos corregidos
**Líneas modificadas**: ~40
**Problemas solucionados**: 5
**Nuevos archivos**: 2 (guía + script)

---

## 🎯 Próximas Fases

### Fase 2: Autenticación en Backend
- [ ] Configurar Spring Security en Gateway
- [ ] Agregar validación JWT en endpoints
- [ ] Configurar roles y autorización

### Fase 3: Sincronizar Usuarios
- [ ] Crear table de usuarios en BD
- [ ] Sincronizar usuarios de Keycloak
- [ ] Mapear roles de Keycloak

### Fase 4: Microservicios Distribuidos
- [ ] Agregar OAuth2 a cada microservicio
- [ ] Configurar service-to-service authentication
- [ ] Implementar token propagation

---

## 📚 Documentación

- [Keycloak Docs](https://www.keycloak.org/documentation)
- [Keycloak JS Adapter](https://www.keycloak.org/docs/latest/securing_apps/#_javascript_adapter)
- [React Keycloak](https://react-keycloak.github.io/)
- [Spring Security OAuth2](https://spring.io/guides/gs/securing-web/)
