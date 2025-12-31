# ✅ CHECKLIST - Implementación de Arreglos Keycloak

## 🔵 FASE 1: CAMBIOS EN CÓDIGO (YA COMPLETADOS)

- [x] Actualizar `main.jsx` con initOptions y LoadingComponent
- [x] Mejorar `keycloak.js` con URL dinámica
- [x] Actualizar `http-common.js` con interceptores robustos
- [x] Mejorar `App.jsx` con validación de inicialización
- [x] Crear `LoadingScreen.jsx`
- [x] Actualizar `realm-export.json` con URLs correctas
- [x] Actualizar `gateway-service.yaml` con OAuth2 config

## 🔴 FASE 2: BUILD Y DEPLOY FRONTEND (HACER AHORA)

- [ ] Verificar que `npm install` esté actualizado
  ```bash
  cd microservicio-frontend
  npm install
  ```

- [ ] Compilar el frontend
  ```bash
  npm run build
  ```

- [ ] Verificar que se compiló sin errores
  ```bash
  ls -la dist/
  ```

- [ ] Reconstruir imagen Docker
  ```bash
  docker build -t pbust0sn/microservicio-frontend:latest .
  docker push pbust0sn/microservicio-frontend:latest
  ```

- [ ] Actualizar Keycloak realm en Kubernetes
  ```bash
  # Eliminar configmap anterior
  kubectl delete configmap keycloak-realm-config --ignore-not-found
  
  # Crear nuevo configmap con JSON actualizado
  kubectl create configmap keycloak-realm-config \
    --from-file=keycloak/realm-export.json
  
  # Reiniciar pod de Keycloak
  kubectl delete pod -l app=keycloak
  kubectl wait --for=condition=Ready pod -l app=keycloak --timeout=300s
  ```

- [ ] Redeployar Frontend
  ```bash
  kubectl rollout restart deployment microservicio-frontend-deployment
  kubectl rollout status deployment microservicio-frontend-deployment
  ```

## 🟠 FASE 3: ACTUALIZAR GATEWAY (HACER DESPUÉS)

- [ ] Actualizar ConfigMap del Gateway con nueva config
  ```bash
  kubectl delete configmap gateway-service --ignore-not-found
  kubectl create configmap gateway-service \
    --from-file=config-data/gateway-service.yaml
  ```

- [ ] Agregar dependencias al pom.xml del gateway-service
  ```xml
  <!-- Spring Security OAuth2 Resource Server -->
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
  </dependency>
  
  <!-- Spring Security -->
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
  </dependency>
  ```

- [ ] Crear `gateway-service/src/main/java/com/example/config/SecurityConfig.java`
  ```bash
  # Ver GUIA-IMPLEMENTACION-JWT.md para el código
  ```

- [ ] Compilar Gateway
  ```bash
  cd gateway-service
  mvn clean package -DskipTests
  ```

- [ ] Reconstruir imagen Docker
  ```bash
  docker build -t pbust0sn/gateway-service:latest .
  docker push pbust0sn/gateway-service:latest
  ```

- [ ] Redeployar Gateway
  ```bash
  kubectl rollout restart deployment gateway-service-deployment
  kubectl rollout status deployment gateway-service-deployment
  ```

## 🟡 FASE 4: ACTUALIZAR MICROSERVICIOS (HACER AL FINAL)

Para cada microservicio (`ms-clients-service`, `ms-inventory-service`, etc.):

### Para cada servicio:

- [ ] **ms-clients-service**
  - [ ] Agregar dependencias OAuth2 a pom.xml
  - [ ] Crear SecurityConfig.java
  - [ ] Compilar y pushear imagen
  - [ ] Redeploy: `kubectl rollout restart deployment ms-clients-service-deployment`

- [ ] **ms-inventory-service**
  - [ ] Agregar dependencias OAuth2 a pom.xml
  - [ ] Crear SecurityConfig.java
  - [ ] Compilar y pushear imagen
  - [ ] Redeploy: `kubectl rollout restart deployment ms-inventory-service-deployment`

- [ ] **ms-loans-service**
  - [ ] Agregar dependencias OAuth2 a pom.xml
  - [ ] Crear SecurityConfig.java
  - [ ] Compilar y pushear imagen
  - [ ] Redeploy: `kubectl rollout restart deployment ms-loans-service-deployment`

- [ ] **ms-rates-service**
  - [ ] Agregar dependencias OAuth2 a pom.xml
  - [ ] Crear SecurityConfig.java
  - [ ] Compilar y pushear imagen
  - [ ] Redeploy: `kubectl rollout restart deployment ms-rates-service-deployment`

- [ ] **ms-kardex-service**
  - [ ] Agregar dependencias OAuth2 a pom.xml
  - [ ] Crear SecurityConfig.java
  - [ ] Compilar y pushear imagen
  - [ ] Redeploy: `kubectl rollout restart deployment ms-kardex-service-deployment`

- [ ] **ms-reports-service**
  - [ ] Agregar dependencias OAuth2 a pom.xml
  - [ ] Crear SecurityConfig.java
  - [ ] Compilar y pushear imagen
  - [ ] Redeploy: `kubectl rollout restart deployment ms-reports-service-deployment`

- [ ] **ms-users-service**
  - [ ] Agregar dependencias OAuth2 a pom.xml
  - [ ] Crear SecurityConfig.java
  - [ ] Compilar y pushear imagen
  - [ ] Redeploy: `kubectl rollout restart deployment ms-users-service-deployment`

## 🟢 FASE 5: VERIFICACIÓN Y TESTING

- [ ] **Verificar Keycloak está corriendo**
  ```bash
  kubectl get pod -l app=keycloak
  kubectl logs -f pod/<keycloak-pod>
  ```

- [ ] **Verificar Frontend se compiló correctamente**
  ```bash
  kubectl logs -f deployment/microservicio-frontend-deployment
  ```

- [ ] **Acceder al Frontend**
  - Ir a `http://192.168.39.157:30080`
  - ¿Se redirige a Keycloak? → ✅

- [ ] **Hacer login en Keycloak**
  - Username: `admin`
  - Password: `admin` (o la que tengas configurada)
  - ¿Redirige de vuelta al Frontend? → ✅

- [ ] **Ver datos en el Frontend**
  - ¿Se cargan herramientas, clientes, etc.? → ✅

- [ ] **Verificar tokens en Network**
  - Abrir DevTools → Network
  - Hacer una petición a `/api/tools/`
  - ¿Header `Authorization: Bearer <token>` presente? → ✅

- [ ] **Verificar logs del Gateway**
  ```bash
  kubectl logs -f deployment/gateway-service-deployment | grep -i oauth
  kubectl logs -f deployment/gateway-service-deployment | grep -i security
  ```

- [ ] **Verificar status HTTP correcto**
  - Sin autenticación: 401 ó redirección a login
  - Con autenticación válida: 200
  - Con token inválido: 401

## 📋 COMANDOS ÚTILES

```bash
# Ver todos los pods
kubectl get pods

# Ver logs del frontend
kubectl logs -f deployment/microservicio-frontend-deployment

# Ver logs del gateway
kubectl logs -f deployment/gateway-service-deployment

# Ver logs de keycloak
kubectl logs -f pod/keycloak-<xxxx>

# Reiniciar un deployment
kubectl rollout restart deployment <nombre>

# Ver estado de un rollout
kubectl rollout status deployment <nombre>

# Ejecutar comando dentro de un pod
kubectl exec -it pod/<nombre> -- bash

# Hacer port-forward local
kubectl port-forward svc/keycloak 8080:8080

# Ver configmaps
kubectl get configmap
kubectl describe configmap <nombre>

# Ver eventos recientes
kubectl get events --sort-by='.lastTimestamp'
```

## 🆘 Si algo falla

### Frontend no muestra Keycloak
1. Verificar que Keycloak está corriendo: `kubectl get pod -l app=keycloak`
2. Verificar URL de Keycloak en logs: `kubectl logs -f deployment/microservicio-frontend-deployment`
3. Verificar que realm-export.json se importó: Acceder a `http://192.168.39.157:30443`

### Gateway devuelve 401 a pesar de tener token
1. Verificar que SecurityConfig está en el Gateway
2. Verificar que las dependencias OAuth2 están en pom.xml
3. Ver logs: `kubectl logs -f deployment/gateway-service-deployment | grep -i oauth`

### Token expira rápidamente
1. Verificar que `keycloak.updateToken(30)` está en http-common.js
2. Verificar que el accessTokenLifespan en Keycloak es mayor a 30 segundos

### CORS error
1. Verificar que webOrigins en realm-export.json incluye tu host
2. Verificar que SecurityConfig tiene CorsConfigurationSource

## 📞 SOPORTE

Revisa:
- [GUIA-IMPLEMENTACION-JWT.md](GUIA-IMPLEMENTACION-JWT.md) - Pasos detallados
- [CAMBIOS-REALIZADOS.md](CAMBIOS-REALIZADOS.md) - Qué se cambió
- [ANTES-VS-DESPUES.md](ANTES-VS-DESPUES.md) - Comparativa visual
- [ANALISIS-KEYCLOAK-PROBLEMAS.md](ANALISIS-KEYCLOAK-PROBLEMAS.md) - Análisis profundo

