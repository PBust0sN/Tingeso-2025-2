# 🔐 Guía: Conectar Frontend con Keycloak en Minikube

## ✅ Cambios Realizados

Se corrigieron **3 problemas críticos** en la configuración del frontend:

### 1. **keycloak.js** - Configuración Dinámica
**Problema**: URL hardcodeada al puerto incorrecto
```javascript
// ❌ ANTES
const keycloakConfig = {
  url: "http://192.168.39.157:30443",  // Puerto incorrecto
  realm: "toolRent",
  clientId: "toolRent-Frontend",
};
keycloak.pkceMethod = null;  // No debería estar aquí
```

**Solución**: Detección automática de ambiente + URL dinámica
```javascript
// ✅ DESPUÉS
const isLocalhost = window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1';
const keycloakUrl = isLocalhost 
  ? "http://localhost:8080"
  : "http://192.168.39.157:30443";
```

### 2. **App.jsx** - Conflictos de PKCE
**Problema**: Deshabilitar y habilitar PKCE al mismo tiempo
```javascript
// ❌ ANTES
keycloak.pkceMethod = null;  // Deshabilitar PKCE
keycloak.init({
  pkceMethod: 'S256',  // Habilitar PKCE ¿?
})
```

**Solución**: Inicialización limpia sin conflictos
```javascript
// ✅ DESPUÉS
keycloak.init({
  onLoad: 'check-sso',
  enableLogging: true,
  silentCheckSsoRedirectUri: window.location.origin + '/silent-check-sso.html'
})
```

### 3. **Logs Mejorados**
- Cambié prefijos de logs para mejor debugging: `[Keycloak]`, `[App]`, `[AppContent]`
- Agregué `enableLogging: true` en Keycloak.init()

---

## 🚀 Próximos Pasos

### Step 1: Compilar el Frontend
```bash
cd microservicio-frontend
npm run build
```

### Step 2: Reconstruir la imagen Docker
```bash
# Opción A: Si usas Docker local
docker build -t pbust0sn/frontend:latest .
docker push pbust0sn/frontend:latest

# Opción B: Si usas Minikube (sin push necesario)
eval $(minikube docker-env)
docker build -t pbust0sn/frontend:latest .
```

### Step 3: Actualizar ConfigMap (si es necesario)
```bash
kubectl create configmap frontend-env-config \
  --from-literal=VITE_KEYCLOAK_URL=http://192.168.39.157:30443 \
  --from-literal=VITE_KEYCLOAK_REALM=toolRent \
  --from-literal=VITE_KEYCLOAK_CLIENT_ID=toolRent-Frontend \
  --dry-run=client -o yaml | kubectl apply -f -
```

### Step 4: Redeployar Frontend
```bash
# Opción A: Con kubectl
kubectl rollout restart deployment/frontend -n default
kubectl get pods -n default | grep frontend

# Opción B: Eliminar y recrear
kubectl delete deployment frontend -n default
kubectl apply -f deployments/frontend-deployment.yaml
```

### Step 5: Verificar Conexión
```bash
# Ver logs del frontend
kubectl logs -l app=frontend -n default --tail=50 -f

# Probar conexión a Keycloak desde el pod
kubectl exec -it $(kubectl get pod -l app=frontend -o jsonpath='{.items[0].metadata.name}') -n default -- \
  curl -s http://192.168.39.157:30443/realms/toolRent/.well-known/openid-configuration | head -5
```

---

## 🧪 Testing Manual

### Acceso al Frontend
```bash
# Puerto del frontend en Minikube
minikube service frontend -n default
# O accede directamente: http://192.168.39.157:30080
```

### Abrir Console del Navegador
1. Abre DevTools (F12)
2. Ve a la pestaña "Console"
3. Deberías ver logs como:
   ```
   [Keycloak] Config: {url: "http://192.168.39.157:30443", realm: "toolRent", clientId: "toolRent-Frontend"}
   [Keycloak] Instance created
   [Main] Renderizando aplicación...
   [App] Inicializando Keycloak...
   [App] Keycloak inicializado correctamente
   [AppContent] initialized: true authenticated: false
   ```

### Intentar Login
1. Haz clic en el botón de login
2. Deberías ser redirigido a `http://192.168.39.157:30443/realms/toolRent/protocol/openid-connect/auth`
3. Usa credenciales: `admin` / `admin`
4. Deberías ser redirigido de vuelta al frontend con token

---

## ⚠️ Problemas Comunes

### Error: "CORS error" o "Access-Control-Allow-Origin"
**Solución**: Ya está configurado en Keycloak con `webOrigins: ["*"]`

### Error: "Unauthorized" al acceder a endpoints
**Solución**: Verifica que el interceptor de Axios esté agregando el token:
```javascript
// En http-common.js debe existir:
config.headers.Authorization = `Bearer ${keycloak.token}`;
```

### Token expirado después de 5 minutos
**Configuración esperada** (en realm-export.json):
```json
"accessTokenLifespan": 300  // 5 minutos
```

### Frontend se queda en "Loading..."
1. Abre la console del navegador (F12)
2. Verifica si hay errores de conexión
3. Asegúrate que Keycloak esté respondiendo:
   ```bash
   curl http://192.168.39.157:30443/realms/toolRent
   ```

---

## 📊 Estado de Keycloak

✅ **Keycloak Status**: Running
- Pod: `keycloak-65b9d758f8-xz7sb`
- Estado: `1/1 Running`
- Puerto: `30443` (NodePort)
- Realm importado: ✅ `toolRent`
- Cliente creado: ✅ `toolRent-Frontend`
- URL: `http://192.168.39.157:30443`

---

## 📝 Verificación de Configuración

### Verificar Realm
```bash
curl -s http://192.168.39.157:30443/realms/toolRent | jq .realm
# Output: "toolRent"
```

### Verificar Cliente
```bash
curl -s http://192.168.39.157:30443/realms/toolRent/clients-registrations/openid-connect
# Busca "toolRent-Frontend"
```

### Verificar Configuración OpenID
```bash
curl -s http://192.168.39.157:30443/realms/toolRent/.well-known/openid-configuration | jq .
```

---

## 🔍 Debug Avanzado

### Ver todos los logs de Keycloak
```bash
kubectl logs -l app=keycloak -n default -f
```

### Acceder a la Consola Administrativa
1. URL: `http://192.168.39.157:30443/admin/`
2. Usuario: `admin`
3. Contraseña: `admin`
4. Ve a: Realm → toolRent → Clients → toolRent-Frontend

### Exportar Configuración Actualizada
```bash
# Si realizas cambios en la consola administrativa
kubectl exec -it $(kubectl get pod -l app=keycloak -o jsonpath='{.items[0].metadata.name}') -n default -- \
  /opt/keycloak/bin/kc.sh export --realm toolRent --users realm_file --file /tmp/realm-export.json

# Luego copiar desde el pod
kubectl cp keycloak-pod:/tmp/realm-export.json keycloak/realm-export.json
```

---

## 📚 Referencias

- **Keycloak Docs**: https://www.keycloak.org/documentation
- **Keycloak JS Adapter**: https://www.keycloak.org/docs/latest/securing_apps/#_javascript_adapter
- **React Keycloak**: https://react-keycloak.github.io/
