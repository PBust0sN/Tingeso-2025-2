#!/bin/bash

# Script para generar certificados SSL autofirmados para ToolRent
# Uso: chmod +x create-ssl-certs.sh && ./create-ssl-certs.sh

set -e  # Salir si hay error

# Colores
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# Variables
CERT_DIR="./microservicio-frontend/certs"
IP="192.168.39.157"
HOSTNAME="toolrent.local"
DAYS=365

echo -e "${BLUE}╔════════════════════════════════════════════════════════╗${NC}"
echo -e "${BLUE}║   🔐 Generador de Certificados SSL Autofirmados        ║${NC}"
echo -e "${BLUE}║   ToolRent - Development Environment                  ║${NC}"
echo -e "${BLUE}╚════════════════════════════════════════════════════════╝${NC}"
echo ""

# Verificar OpenSSL
if ! command -v openssl &> /dev/null; then
    echo -e "${RED}❌ Error: OpenSSL no está instalado${NC}"
    echo "   Instalar: sudo apt-get install openssl"
    exit 1
fi

echo -e "${YELLOW}📋 Configuración:${NC}"
echo "   IP: $IP"
echo "   Hostname: $HOSTNAME"
echo "   Directorio: $CERT_DIR"
echo "   Válido por: $DAYS días"
echo ""

# Crear directorio
mkdir -p "$CERT_DIR"
echo -e "${GREEN}✅ Directorio creado: $CERT_DIR${NC}"

# Archivo de configuración OpenSSL
echo -e "${YELLOW}📝 Creando configuración OpenSSL...${NC}"
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
echo -e "${GREEN}✅ Configuración creada${NC}"

# Generar clave privada
echo -e "${YELLOW}🔑 Generando clave privada (2048 bits)...${NC}"
openssl genrsa -out "$CERT_DIR/tls.key" 2048 2>/dev/null
echo -e "${GREEN}✅ Clave privada creada${NC}"

# Crear CSR
echo -e "${YELLOW}📜 Creando Certificate Signing Request (CSR)...${NC}"
openssl req -new \
  -key "$CERT_DIR/tls.key" \
  -out "$CERT_DIR/tls.csr" \
  -config "$CERT_DIR/ssl.conf" \
  2>/dev/null
echo -e "${GREEN}✅ CSR creado${NC}"

# Generar certificado autofirmado
echo -e "${YELLOW}🔐 Generando certificado autofirmado ($DAYS días)...${NC}"
openssl x509 -req \
  -in "$CERT_DIR/tls.csr" \
  -signkey "$CERT_DIR/tls.key" \
  -out "$CERT_DIR/tls.crt" \
  -days $DAYS \
  -extensions v3_req \
  -extfile "$CERT_DIR/ssl.conf" \
  2>/dev/null
echo -e "${GREEN}✅ Certificado generado${NC}"

# Crear archivo PEM
echo -e "${YELLOW}📦 Creando archivo PEM combinado...${NC}"
cat "$CERT_DIR/tls.crt" "$CERT_DIR/tls.key" > "$CERT_DIR/tls.pem"
echo -e "${GREEN}✅ PEM creado${NC}"

# Limpiar archivos temporales
rm "$CERT_DIR/tls.csr" "$CERT_DIR/ssl.conf"

# Mostrar información del certificado
echo ""
echo -e "${BLUE}╔════════════════════════════════════════════════════════╗${NC}"
echo -e "${BLUE}║   📋 INFORMACIÓN DEL CERTIFICADO                       ║${NC}"
echo -e "${BLUE}╚════════════════════════════════════════════════════════╝${NC}"
echo ""

openssl x509 -in "$CERT_DIR/tls.crt" -text -noout 2>/dev/null | grep -E "Subject:|Issuer:|Not Before|Not After|Public-Key:|Subject Alternative Name"

echo ""
echo -e "${GREEN}✅ Certificados creados exitosamente!${NC}"
echo ""

# Listar archivos generados
echo -e "${YELLOW}📁 Archivos generados:${NC}"
ls -lh "$CERT_DIR"/ | grep -E "tls\.(crt|key|pem)" | awk '{print "   " $9 " (" $5 ")"}'

echo ""
echo -e "${YELLOW}⚠️  IMPORTANTE:${NC}"
echo "   - Este certificado es AUTOFIRMADO (válido solo para desarrollo)"
echo "   - El navegador mostrará un aviso de seguridad (es NORMAL)"
echo "   - En Firefox: Aceptar el riesgo y continuar"
echo "   - Válido hasta: $(openssl x509 -in "$CERT_DIR/tls.crt" -noout -enddate | cut -d= -f2)"
echo ""

echo -e "${BLUE}🚀 Próximos pasos:${NC}"
echo "   1. Copiar certificados al Dockerfile:"
echo "      COPY certs/tls.crt /etc/nginx/certs/tls.crt"
echo "      COPY certs/tls.key /etc/nginx/certs/tls.key"
echo ""
echo "   2. Recompilar el frontend:"
echo "      npm --prefix microservicio-frontend run build"
echo ""
echo "   3. Reconstruir imagen Docker:"
echo "      eval \$(minikube docker-env)"
echo "      docker build -t pbust0sn/frontend:latest microservicio-frontend/"
echo ""
echo "   4. Crear Secret en Kubernetes:"
echo "      kubectl create secret tls frontend-tls \\"
echo "        --cert=microservicio-frontend/certs/tls.crt \\"
echo "        --key=microservicio-frontend/certs/tls.key \\"
echo "        -n default --dry-run=client -o yaml | kubectl apply -f -"
echo ""
echo "   5. Restart deployment:"
echo "      kubectl rollout restart deployment/microservicio-frontend-deployment"
echo ""
echo "   6. Acceder a: https://192.168.39.157:30080"
echo ""
