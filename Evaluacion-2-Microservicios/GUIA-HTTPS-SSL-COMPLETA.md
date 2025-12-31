# 🔐 GUÍA COMPLETA: HTTPS con SSL Autofirmado + PKCE + Keycloak

## 📋 Índice
1. [Por qué falla Web Crypto API](#por-qué-falla-web-crypto-api)
2. [Arquitectura final](#arquitectura-final)
3. [Paso 1: Crear certificados SSL](#paso-1-crear-certificados-ssl)
4. [Paso 2: Configurar Nginx HTTPS](#paso-2-configurar-nginx-https)
5. [Paso 3: Actualizar Keycloak](#paso-3-actualizar-keycloak)
6. [Paso 4: Configurar Frontend React](#paso-4-configurar-frontend-react)
7. [Paso 5: Actualizar Docker](#paso-5-actualizar-docker)
8. [Paso 6: Deploy en Kubernetes](#paso-6-deploy-en-kubernetes)
9. [Troubleshooting](#troubleshooting)

---

## ❌ Por qué falla Web Crypto API

### El problema:
```
Uncaught (in promise) Error: Web Crypto API is not available
```

### Causas:
1. **HTTP en navegadores modernos** - Web Crypto API solo funciona en contextos seguros (HTTPS)
2. **PKCE requiere Web Crypto** - Para generar code_challenge (S256)
3. **Localhost exception** - Solo `localhost:puerto` puede usar HTTP con Web Crypto, pero `192.168.x.x:puerto` NO

### La solución:
**HTTPS autofirmado** en la IP privada `192.168.39.157`

---

## 🏗️ Arquitectura final

```
┌─────────────────────────────────────────────┐
│ Cliente (Firefox en VM)                      │
│ https://192.168.39.157:30080                │
└──────────────────┬──────────────────────────┘
                   │ HTTPS
                   ▼
┌─────────────────────────────────────────────┐
│ Nginx (Reverse Proxy)                       │
│ Puerto 30080 (HTTPS)                        │
│ - Certificado: /certs/tls.crt               │
│ - Clave: /certs/tls.key                     │
└──────────────────┬──────────────────────────┘
                   │ HTTP interno
                   ▼
┌─────────────────────────────────────────────┐
│ Frontend React (dist/)                      │
│ Puerto 80 interno                           │
│ - PKCE S256 habilitado                      │
└──────────────────┬──────────────────────────┘
                   │ HTTPS
                   ▼
┌─────────────────────────────────────────────┐
│ Keycloak                                    │
│ https://192.168.39.157:30443 (NodePort)     │
│ - Redirect URI: https://192.168.39.157/     │
│ - Web Origins: https://192.168.39.157       │
└─────────────────────────────────────────────┘
```

---

## 🔑 Paso 1: Crear certificados SSL

### Opción A: Script automatizado (RECOMENDADO)

Crea un archivo `create-ssl-certs.sh`:

```bash
#!/bin/bash

# Variables
CERT_DIR="./microservicio-frontend/certs"
IP="192.168.39.157"
HOSTNAME="toolrent.local"
DAYS=365

# Crear directorio
mkdir -p "$CERT_DIR"

echo "🔐 Creando certificado SSL autofirmado..."
echo "   IP: $IP"
echo "   Hostname: $HOSTNAME"
echo "   Válido por: $DAYS días"

# 1. Crear archivo de configuración OpenSSL
cat > "$CERT_DIR/ssl.conf" << EOF
[req]
default_bits = 2048
prompt = no
default_md = sha256
distinguished_name = req_distinguished_name
req_extensions = v3_req

[req_distinguished_name]
C = CL
ST = Santiago
L = Santiago
O = ToolRent
OU = Development
CN = $IP

[v3_req]
keyUsage = keyEncipherment, dataEncipherment
extendedKeyUsage = serverAuth
subjectAltName = DNS:$HOSTNAME,DNS:localhost,IP:$IP,IP:127.0.0.1
EOF

# 2. Generar clave privada
openssl genrsa -out "$CERT_DIR/tls.key" 2048 2>/dev/null

# 3. Crear CSR (Certificate Signing Request)
openssl req -new \
  -key "$CERT_DIR/tls.key" \
  -out "$CERT_DIR/tls.csr" \
  -config "$CERT_DIR/ssl.conf" \
  2>/dev/null

# 4. Generar certificado autofirmado
openssl x509 -req \
  -in "$CERT_DIR/tls.csr" \
  -signkey "$CERT_DIR/tls.key" \
  -out "$CERT_DIR/tls.crt" \
  -days $DAYS \
  -extensions v3_req \
  -extfile "$CERT_DIR/ssl.conf" \
  2>/dev/null

# 5. Crear archivo PEM (para aplicaciones que lo requieran)
cat "$CERT_DIR/tls.crt" "$CERT_DIR/tls.key" > "$CERT_DIR/tls.pem"

# 6. Limpiar archivos temporales
rm "$CERT_DIR/tls.csr" "$CERT_DIR/ssl.conf"

# 7. Mostrar información del certificado
echo ""
echo "✅ Certificados creados exitosamente!"
echo ""
echo "📁 Archivos generados:"
echo "   - $CERT_DIR/tls.crt (Certificado)"
echo "   - $CERT_DIR/tls.key (Clave privada)"
echo "   - $CERT_DIR/tls.pem (PEM combinado)"
echo ""
echo "📋 Información del certificado:"
openssl x509 -in "$CERT_DIR/tls.crt" -text -noout | grep -E "Subject:|Issuer:|Not Before|Not After|Subject Alternative Name"
echo ""
echo "⚠️  IMPORTANTE: Este certificado es autofirmado."
echo "   El navegador mostrará un aviso de seguridad (es normal)."
echo "   En Firefox: Aceptar el riesgo y continuar"
```

### Ejecutar:

```bash
chmod +x create-ssl-certs.sh
./create-ssl-certs.sh
```

### Opción B: Comando manual

```bash
# Variables
IP="192.168.39.157"
CERT_DIR="./microservicio-frontend/certs"

# Crear directorio
mkdir -p "$CERT_DIR"

# Generar en un comando
openssl req -x509 -newkey rsa:2048 -keyout "$CERT_DIR/tls.key" \
  -out "$CERT_DIR/tls.crt" -days 365 -nodes \
  -subj "/C=CL/ST=Santiago/L=Santiago/O=ToolRent/OU=Dev/CN=$IP" \
  -addext "subjectAltName=DNS:toolrent.local,DNS:localhost,IP:$IP,IP:127.0.0.1"
```

---

## 🔧 Paso 2: Configurar Nginx HTTPS

### Archivo: `microservicio-frontend/nginx.conf`

```nginx
# HTTP -> HTTPS redirect
server {
    listen 80;
    server_name _;
    
    location / {
        return 301 https://$host$request_uri;
    }
}

# HTTPS server
server {
    listen 443 ssl http2;
    server_name 192.168.39.157 toolrent.local localhost;
    
    # Certificados SSL
    ssl_certificate /etc/nginx/certs/tls.crt;
    ssl_certificate_key /etc/nginx/certs/tls.key;
    
    # Configuración SSL moderna
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;
    ssl_prefer_server_ciphers on;
    ssl_session_cache shared:SSL:10m;
    ssl_session_timeout 10m;
    
    # Headers de seguridad
    add_header Strict-Transport-Security "max-age=31536000; includeSubDomains" always;
    add_header X-Content-Type-Options "nosniff" always;
    add_header X-Frame-Options "SAMEORIGIN" always;
    add_header X-XSS-Protection "1; mode=block" always;
    
    # Frontend React
    root /usr/share/nginx/html;
    index index.html index.htm;
    
    location / {
        try_files $uri $uri/ /index.html;
        add_header Cache-Control "no-cache, no-store, must-revalidate";
        add_header Pragma "no-cache";
        add_header Expires "0";
        
        # CORS para Keycloak
        add_header Access-Control-Allow-Origin "https://192.168.39.157:30443" always;
        add_header Access-Control-Allow-Methods "GET, POST, PUT, DELETE, OPTIONS" always;
        add_header Access-Control-Allow-Headers "Content-Type, Authorization" always;
        
        if ($request_method = OPTIONS) {
            return 204;
        }
    }
    
    # Cache para assets
    location ~* \.(js|css|png|jpg|jpeg|gif|svg|woff|woff2|ttf|eot)$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
    }
}
```

---

## 🗝️ Paso 3: Actualizar Keycloak

### Actualizar ConfigMap con URLs HTTPS

```bash
kubectl create configmap keycloak-realm-config \
  --from-file=keycloak/realm-export.json \
  --dry-run=client -o yaml | kubectl apply -f -
```

### Modificar `realm-export.json`

Busca el cliente `toolRent-Frontend` y actualiza:

```json
{
  "clientId": "toolRent-Frontend",
  "rootUrl": "https://192.168.39.157:30080",
  "redirectUris": [
    "https://192.168.39.157:30080/",
    "https://192.168.39.157:30080/*",
    "https://toolrent.local/",
    "https://toolrent.local/*",
    "https://localhost/",
    "https://localhost/*"
  ],
  "webOrigins": [
    "https://192.168.39.157:30080",
    "https://toolrent.local",
    "https://localhost",
    "*"
  ],
  "publicClient": true,
  "standardFlowEnabled": true,
  "implicitFlowEnabled": true,
  "directAccessGrantsEnabled": true
}
```

### Reiniciar Keycloak

```bash
kubectl delete pod -l app=keycloak -n default
kubectl rollout status deployment/keycloak -n default
```

---

## ⚛️ Paso 4: Configurar Frontend React

### Archivo: `microservicio-frontend/src/services/keycloak.js`

```javascript
import Keycloak from "keycloak-js";

// Detectar ambiente
const hostname = window.location.hostname;
const protocol = window.location.protocol; // https: o http:

let keycloakUrl;

if (hostname === 'localhost' || hostname === '127.0.0.1') {
  keycloakUrl = "https://localhost:30443";
} else if (hostname === 'toolrent.local') {
  keycloakUrl = "https://toolrent.local:30443";
} else {
  // IP privada
  keycloakUrl = `${protocol}//192.168.39.157:30443`;
}

const keycloakConfig = {
  url: keycloakUrl,
  realm: "toolRent",
  clientId: "toolRent-Frontend",
};

console.log('[Keycloak] Configuración:', {
  url: keycloakUrl,
  protocol: protocol,
  hostname: hostname,
  realm: keycloakConfig.realm,
});

const keycloak = new Keycloak(keycloakConfig);

console.log('[Keycloak] Instancia creada');

export default keycloak;
```

### Archivo: `microservicio-frontend/src/App.jsx`

```javascript
keycloak.init({
  onLoad: null,
  checkLoginIframe: false,
  silentCheckSsoFallback: false,
  enableLogging: true,
  pkceMethod: 'S256'  // ✅ AHORA SÍ FUNCIONA CON HTTPS
})
```

---

## 🐳 Paso 5: Actualizar Docker

### Archivo: `microservicio-frontend/Dockerfile`

```dockerfile
# Stage 1: Build
FROM node:22-alpine AS builder
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Stage 2: Nginx con SSL
FROM nginx:stable-alpine
WORKDIR /usr/share/nginx/html
RUN rm -rf ./*
COPY --from=builder /app/dist .

# Copiar configuración Nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

# ✅ Copiar certificados SSL
COPY certs/tls.crt /etc/nginx/certs/tls.crt
COPY certs/tls.key /etc/nginx/certs/tls.key

# Permisos
RUN chmod 644 /etc/nginx/certs/tls.crt && \
    chmod 600 /etc/nginx/certs/tls.key

EXPOSE 80 443
CMD ["nginx", "-g", "daemon off;"]
```

### Build y Push

```bash
cd microservicio-frontend

# Generar certificados si aún no existen
mkdir -p certs
openssl req -x509 -newkey rsa:2048 -keyout certs/tls.key \
  -out certs/tls.crt -days 365 -nodes \
  -subj "/C=CL/ST=Santiago/L=Santiago/O=ToolRent/OU=Dev/CN=192.168.39.157" \
  -addext "subjectAltName=DNS:toolrent.local,DNS:localhost,IP:192.168.39.157,IP:127.0.0.1"

# Build imagen
eval $(minikube docker-env)
docker build -t pbust0sn/frontend:latest .

# Push
docker push pbust0sn/frontend:latest

cd ..
```

---

## ☸️ Paso 6: Deploy en Kubernetes

### Crear Secret con certificados

```bash
kubectl create secret tls frontend-tls \
  --cert=microservicio-frontend/certs/tls.crt \
  --key=microservicio-frontend/certs/tls.key \
  -n default --dry-run=client -o yaml | kubectl apply -f -
```

### Actualizar Deployment

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: microservicio-frontend-deployment
spec:
  template:
    spec:
      containers:
      - name: frontend
        image: pbust0sn/frontend:latest
        ports:
        - containerPort: 80
        - containerPort: 443
        volumeMounts:
        - name: certs
          mountPath: /etc/nginx/certs
          readOnly: true
      volumes:
      - name: certs
        secret:
          secretName: frontend-tls
```

### Actualizar Service

```yaml
apiVersion: v1
kind: Service
metadata:
  name: microservicio-frontend
spec:
  type: NodePort
  ports:
  - port: 80
    targetPort: 80
    nodePort: 30080
    protocol: TCP
    name: http
  - port: 443
    targetPort: 443
    nodePort: 30443
    protocol: TCP
    name: https
  selector:
    app: microservicio-frontend
```

### Deploy

```bash
kubectl apply -f deployments/frontend-deployment.yaml
kubectl rollout status deployment/microservicio-frontend-deployment -n default
```

---

## 🔍 Verificación

### 1. Verificar certificados

```bash
# Ver dentro del pod
kubectl exec -it $(kubectl get pod -l app=microservicio-frontend -o jsonpath='{.items[0].metadata.name}') \
  -- ls -la /etc/nginx/certs/

# Ver información del certificado
openssl x509 -in microservicio-frontend/certs/tls.crt -text -noout
```

### 2. Prueba HTTPS desde VM

```bash
# Desde la VM donde está Minikube
curl -k https://192.168.39.157:30080  # -k ignora certificado autofirmado
```

### 3. Acceso desde navegador

```
https://192.168.39.157:30080
```

- El navegador mostrará ⚠️ "Advertencia de seguridad"
- Es NORMAL para certificados autofirmados
- Hacer clic en "Aceptar el riesgo y continuar" (Firefox)

---

## ⚠️ Troubleshooting

### Error: "SSL_ERROR_RX_RECORD_TOO_LONG"
**Causa:** Nginx está sirviendo HTTP donde espera HTTPS
**Solución:** Verificar que nginx.conf tiene `ssl_certificate` y `ssl_certificate_key`

### Error: "PKCE Code verifier too long"
**Causa:** Todavía no hay HTTPS
**Solución:** Acceder vía `https://` no `http://`

### Error: "subject alt name does not match"
**Causa:** El CN del certificado no coincide
**Solución:** Regenerar certificado con el IP/hostname correcto

### El navegador no acepta el certificado
**Causa:** Es autofirmado (esperado en desarrollo)
**Solución:** Agregar excepción permanente o usar `-k` en curl

### Firefox muestra "SEC_ERROR_INADEQUATE_KEY_USAGE"
**Causa:** El certificado no tiene las extensiones correctas
**Solución:** Usar script con `subjectAltName` en `v3_req`

---

## 📝 Comando todo-en-uno

```bash
#!/bin/bash

cd /media/patricio/EXTERNO/NIVEL\ 6/Tecnicas\ de\ ingenieria\ de\ software/Material/Tingeso-2025-2/Evaluacion-2-Microservicios

# 1. Crear certificados
mkdir -p microservicio-frontend/certs
openssl req -x509 -newkey rsa:2048 -keyout microservicio-frontend/certs/tls.key \
  -out microservicio-frontend/certs/tls.crt -days 365 -nodes \
  -subj "/C=CL/ST=Santiago/L=Santiago/O=ToolRent/OU=Dev/CN=192.168.39.157" \
  -addext "subjectAltName=DNS:toolrent.local,DNS:localhost,IP:192.168.39.157,IP:127.0.0.1"

# 2. Build frontend
npm --prefix microservicio-frontend run build

# 3. Build Docker imagen
eval $(minikube docker-env)
docker build -t pbust0sn/frontend:latest microservicio-frontend/
docker push pbust0sn/frontend:latest

# 4. Crear secret
kubectl create secret tls frontend-tls \
  --cert=microservicio-frontend/certs/tls.crt \
  --key=microservicio-frontend/certs/tls.key \
  -n default --dry-run=client -o yaml | kubectl apply -f -

# 5. Restart deployment
kubectl rollout restart deployment/microservicio-frontend-deployment -n default
kubectl rollout status deployment/microservicio-frontend-deployment -n default

# 6. Verificar
kubectl get pods -l app=microservicio-frontend -n default
echo "✅ Acceder a: https://192.168.39.157:30080"
```

---

## 🎯 Checklist Final

- [ ] Certificados generados en `microservicio-frontend/certs/`
- [ ] `nginx.conf` configurado con SSL
- [ ] `keycloak.js` usa `https://` y URL dinámica
- [ ] `App.jsx` usa `pkceMethod: 'S256'`
- [ ] Dockerfile copia certificados
- [ ] Secret de Kubernetes creado
- [ ] Frontend deployado con nueva imagen
- [ ] Acceso a `https://192.168.39.157:30080` funcionando
- [ ] Botón de Login redirige a Keycloak
- [ ] Login exitoso ✅

---

**Resultado esperado:** 

```
✅ Frontend en HTTPS
✅ PKCE funcionando
✅ Login con Keycloak exitoso
✅ Tokens JWT validados
```
