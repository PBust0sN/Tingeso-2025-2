#!/bin/bash

# Script para generar certificados SSL autofirmados para Keycloak y Frontend
# Uso: chmod +x create-ssl-certs.sh && ./create-ssl-certs.sh

set -e  # Salir si hay error

# Colores
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# Variables
CERT_DIR_KEYCLOAK="./keycloak/certs"
CERT_DIR_FRONTEND="./monoliticoFrontend/certs"
IP="15.229.27.254"
HOSTNAME_KEYCLOAK="keycloak.local"
HOSTNAME_FRONTEND="frontend.local"
DAYS=365

# Función para generar certificados
create_certificates() {
  local CERT_DIR=$1
  local HOSTNAME=$2

  echo -e "${BLUE}🔐 Generando certificados para $HOSTNAME...${NC}"

  # Crear directorio
  mkdir -p "$CERT_DIR"

  # Archivo de configuración OpenSSL
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
O = Tingeso
OU = Development
CN = $IP

[v3_req]
keyUsage = critical, digitalSignature, keyEncipherment, dataEncipherment
extendedKeyUsage = serverAuth
subjectAltName = DNS:$HOSTNAME,DNS:localhost,IP:$IP,IP:127.0.0.1
EOF

  # Generar clave privada
  openssl genrsa -out "$CERT_DIR/tls.key" 2048 2>/dev/null

  # Crear CSR
  openssl req -new \
    -key "$CERT_DIR/tls.key" \
    -out "$CERT_DIR/tls.csr" \
    -config "$CERT_DIR/ssl.conf" \
    2>/dev/null

  # Generar certificado autofirmado
  openssl x509 -req \
    -in "$CERT_DIR/tls.csr" \
    -signkey "$CERT_DIR/tls.key" \
    -out "$CERT_DIR/tls.crt" \
    -days $DAYS \
    -extensions v3_req \
    -extfile "$CERT_DIR/ssl.conf" \
    2>/dev/null

  # Crear archivo PEM
  cat "$CERT_DIR/tls.crt" "$CERT_DIR/tls.key" > "$CERT_DIR/tls.pem"

  # Limpiar archivos temporales
  rm "$CERT_DIR/tls.csr" "$CERT_DIR/ssl.conf"

  echo -e "${GREEN}✅ Certificados creados para $HOSTNAME en $CERT_DIR${NC}"
}

# Generar certificados para Keycloak
create_certificates "$CERT_DIR_KEYCLOAK" "$HOSTNAME_KEYCLOAK"

# Generar certificados para Frontend
create_certificates "$CERT_DIR_FRONTEND" "$HOSTNAME_FRONTEND"

# Mostrar información
echo -e "${BLUE}📋 Certificados generados exitosamente:${NC}"
echo "   - Keycloak: $CERT_DIR_KEYCLOAK"
echo "   - Frontend: $CERT_DIR_FRONTEND"
