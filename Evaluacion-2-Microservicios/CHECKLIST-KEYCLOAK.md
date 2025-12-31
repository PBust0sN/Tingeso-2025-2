# ✅ CHECKLIST: Frontend + Keycloak en Minikube

## 🔧 Configuración Completada

- [x] **Keycloak corriendo** en puerto 30443
- [x] **Realm importado** (toolRent)
- [x] **Cliente Frontend registrado** (toolRent-Frontend)
- [x] **CORS habilitado** en Keycloak
- [x] **Redirect URIs configuradas** en realm-export.json
- [x] **keycloak.js actualizado** - URL dinámica
- [x] **App.jsx arreglado** - Sin conflictos PKCE
- [x] **main.jsx actualizado** - Logs mejorados
- [x] **Axios interceptor** ya existe con manejo de tokens
- [x] **LoadingScreen** existe para espera de inicialización
- [x] **OpenID Configuration** verificada y accesible

---

## 🚀 Próximos Pasos (IMPORTANTE)

### OPCIÓN 1: Deploy Automático (RECOMENDADO)

```bash
# Desde el directorio raíz del proyecto
chmod +x deploy-frontend.sh
./deploy-frontend.sh
```

El script realizará automáticamente:
- ✅ Compilar frontend con `npm run build`
- ✅ Configurar Docker para Minikube
- ✅ Construir imagen Docker
- ✅ Redeployar en Kubernetes
- ✅ Mostrar logs de inicio

---

### OPCIÓN 2: Deploy Manual

#### Paso 1: Compilar el Frontend
```bash
cd microservicio-frontend
npm run build
cd ../
```

#### Paso 2: Configurar Docker para Minikube
```bash
eval $(minikube docker-env)
```

#### Paso 3: Construir la Imagen Docker
```bash
cd microservicio-frontend
docker build -t pbust0sn/frontend:latest .
cd ../
```

#### Paso 4: Redeployar en Kubernetes
```bash
# Opción A: Rollout restart (recomendado)
kubectl rollout restart deployment/frontend -n default

# Opción B: Crear deployment si no existe
kubectl apply -f deployments/frontend-deployment.yaml -n default
```

#### Paso 5: Verificar Estado
```bash
# Ver pods
kubectl get pods -l app=frontend -n default

# Ver logs (seguimiento en tiempo real)
kubectl logs -l app=frontend -n default --tail=50 -f
```

---

## 🧪 Verificación Post-Deployment

### 1. Verificar que el Pod esté corriendo
```bash
kubectl get pods -l app=frontend -n default
```
Esperado:
```
NAME                        READY   STATUS    RESTARTS   AGE
frontend-7d8f5c9b8d-k9x2l   1/1     Running   0          1m
```

### 2. Verificar logs iniciales
```bash
kubectl logs -l app=frontend -n default
```

Deberías ver algo como:
```
[Keycloak] Config: {url: "http://192.168.39.157:30443", realm: "toolRent", clientId: "toolRent-Frontend"}
[Keycloak] Instance created
[Main] Renderizando aplicación...
[App] Inicializando Keycloak...
[App] Keycloak inicializado correctamente
```

### 3. Acceder al Frontend
Abre en navegador:
```
http://192.168.39.157:30080
```

O usa:
```bash
minikube service frontend -n default
```

### 4. Abrir DevTools (F12)
Ve a la pestaña **Console** y verifica que NO haya errores rojo.

Deberías ver:
```
✅ [Keycloak] logs
✅ [App] logs
✅ [AppContent] logs
❌ EVITAR: errores de conexión o CORS
```

### 5. Verificar Conectividad con Keycloak
```bash
# Desde tu máquina
curl -s http://192.168.39.157:30443/realms/toolRent | head -5
```

---

## 🔐 Testing de Autenticación

### Paso 1: Intentar Login
1. Abre `http://192.168.39.157:30080`
2. Haz clic en "Login" o botón de autenticación
3. Deberías ser redirigido a: `http://192.168.39.157:30443/realms/toolRent/...`

### Paso 2: Credenciales
- **Usuario**: `admin`
- **Contraseña**: `admin`

### Paso 3: Verificar Redirección
Después de login, deberías volver a:
```
http://192.168.39.157:30080
```

Con el estado: `authenticated: true`

### Paso 4: Verificar Token en LocalStorage
En DevTools → Application → Local Storage → Current domain

Deberías ver:
```
kc_state
kc_access_token (con contenido JWT)
kc_token
```

### Paso 5: Hacer una Petición al Backend
Si el frontend puede hacer una petición a un endpoint protegido:

En DevTools → Network → request to backend

Deberías ver header:
```
Authorization: Bearer eyJhbGc...
```

---

## ⚠️ Problemas Comunes

### ❌ Problema: Frontend mostrando "Loading..." por siempre

**Diagnóstico**:
```bash
# Ver logs completos
kubectl logs -l app=frontend -n default

# Ver eventos del pod
kubectl describe pod -l app=frontend -n default
```

**Soluciones posibles**:
1. Keycloak no es accesible desde el pod
   ```bash
   # Probar conectividad desde el pod
   kubectl exec -it $(kubectl get pod -l app=frontend -o jsonpath='{.items[0].metadata.name}') -- \
     curl http://192.168.39.157:30443/realms/toolRent
   ```

2. URL de Keycloak incorrecta
   - Verificar que `keycloak.js` use la URL correcta
   - Debería ser: `http://192.168.39.157:30443`

3. ConfigMap de Keycloak no existe
   ```bash
   kubectl get configmap keycloak-realm-config -n default
   ```

---

### ❌ Problema: "CORS error" o "Access-Control-Allow-Origin"

**Solución**:
Ya está configurado en Keycloak con:
```json
"webOrigins": ["*"]
```

Pero si persiste:
```bash
# Verificar CORS en Keycloak
kubectl exec -it $(kubectl get pod -l app=keycloak -o jsonpath='{.items[0].metadata.name}') -- \
  grep -i cors /opt/keycloak/data/import/realm-export.json
```

---

### ❌ Problema: "Unauthorized" después de login

**Verificar**:
1. ¿El token se está enviando?
   ```javascript
   // En DevTools console
   localStorage.kc_access_token  // Debería existir
   ```

2. ¿El backend valida correctamente?
   - Verificar que Gateway esté configurado con OAuth2
   - Ver logs del Gateway

3. Token expirado (solo 5 minutos)
   - Hacer logout y login nuevamente

---

### ❌ Problema: "Redirect URI mismatch"

**Verificar URL de Keycloak**:
```bash
# Ver configuración del cliente
curl -s http://192.168.39.157:30443/realms/toolRent/clients-registrations/openid-connect | grep -A5 "toolRent-Frontend"
```

Debería incluir:
- `http://192.168.39.157:30080/`
- `http://localhost:5173/`

---

### ❌ Problema: "Network error" al conectar a Keycloak

**Soluciones**:
```bash
# 1. Verificar que Keycloak esté corriendo
kubectl get pods -l app=keycloak -n default

# 2. Verificar servicio
kubectl get svc keycloak -n default

# 3. Probar conectividad
curl http://192.168.39.157:30443/health

# 4. Ver logs de Keycloak
kubectl logs -l app=keycloak -n default --tail=30
```

---

## 📊 Verificación de Estado

### Chequeo Rápido
```bash
#!/bin/bash
echo "=== Keycloak ==="
kubectl get pods -l app=keycloak -n default

echo -e "\n=== Frontend ==="
kubectl get pods -l app=frontend -n default

echo -e "\n=== Servicios ==="
kubectl get svc keycloak frontend -n default

echo -e "\n=== Conectividad Keycloak ==="
curl -s http://192.168.39.157:30443/realms/toolRent/.well-known/openid-configuration | jq .issuer

echo -e "\n=== Últimos logs Frontend ==="
kubectl logs -l app=frontend -n default --tail=5
```

---

## 📝 Comandos Útiles

### Ver estado general
```bash
kubectl get all -n default
```

### Seguir logs en tiempo real
```bash
kubectl logs -l app=frontend -n default -f
```

### Acceder a shell del pod
```bash
kubectl exec -it $(kubectl get pod -l app=frontend -o jsonpath='{.items[0].metadata.name}') -- /bin/sh
```

### Describe del pod (eventos)
```bash
kubectl describe pod -l app=frontend -n default
```

### Port-forward
```bash
kubectl port-forward svc/frontend 8080:80 -n default
```

### Restart deployment
```bash
kubectl rollout restart deployment/frontend -n default
```

### Ver rollout history
```bash
kubectl rollout history deployment/frontend -n default
```

---

## 🎯 Estado Actual

| Componente | Estado | Nota |
|-----------|--------|------|
| Keycloak | ✅ Running | Escuchando 192.168.39.157:30443 |
| Frontend Source | ✅ Actualizado | keycloak.js, App.jsx, main.jsx arreglados |
| Frontend Build | ⚠️ Pendiente | Necesita `npm run build` |
| Frontend Pod | ⚠️ Pendiente | Necesita deployment después del build |
| Testing | ⏳ Próximo | Después del deployment |

---

## ✨ Resumen de Arreglos

1. **keycloak.js**: URL dinámica según ambiente
2. **App.jsx**: Inicialización limpia sin conflictos PKCE  
3. **main.jsx**: Logs mejorados
4. **http-common.js**: ✅ Ya tiene interceptor correcto
5. **LoadingScreen**: ✅ Ya existe
6. **realm-export.json**: ✅ Ya configurado

**Resultado**: Frontend debería conectar correctamente con Keycloak después del deployment.

---

## 📚 Documentación Generada

Revisa los siguientes archivos para más información:
- `GUIA-CONECTAR-KEYCLOAK.md` - Guía completa
- `RESUMEN-ARREGLOS-KEYCLOAK.md` - Resumen técnico
- `deploy-frontend.sh` - Script de auto-deployment

---

## 🔔 IMPORTANTE

⚠️ **Después de hacer cambios en el código frontend, SIEMPRE**:

1. Recompilar: `npm run build`
2. Reconstruir imagen Docker
3. Redeployar en Minikube

Puedes usar:
```bash
./deploy-frontend.sh
```

Para automatizar todo el proceso.

---

**Última actualización**: 30 de diciembre de 2025
