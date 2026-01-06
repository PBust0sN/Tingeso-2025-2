#!/bin/bash

# Script para configurar HTTPS en Gateway, Frontend y Keycloak
# Ejecutar: chmod +x setup-https.sh && ./setup-https.sh

set -e

CERT_DIR="./microservicio-frontend/certs"

# Verificar que existan los certificados
if [ ! -f "$CERT_DIR/tls.crt" ] || [ ! -f "$CERT_DIR/tls.key" ]; then
    echo "❌ Certificados no encontrados en $CERT_DIR"
    echo "Ejecuta primero: chmod +x create-ssl-certs.sh && ./create-ssl-certs.sh"
    exit 1
fi

echo "🔐 Configurando HTTPS en Kubernetes..."

# 1. Crear Secret para Frontend
echo "📋 Creando Secret para Frontend..."
kubectl create secret tls frontend-tls \
  --cert="$CERT_DIR/tls.crt" \
  --key="$CERT_DIR/tls.key" \
  -n default --dry-run=client -o yaml | kubectl apply -f -

# 2. Crear Secret para Gateway
echo "📋 Creando Secret para Gateway (convertir a PKCS12)..."
openssl pkcs12 -export \
  -in "$CERT_DIR/tls.crt" \
  -inkey "$CERT_DIR/tls.key" \
  -out "$CERT_DIR/tls.p12" \
  -name gateway-key \
  -passout pass:changeit

kubectl create secret generic gateway-tls \
  --from-file=tls.p12="$CERT_DIR/tls.p12" \
  -n default --dry-run=client -o yaml | kubectl apply -f -

# 3. Crear Secret para Keycloak
echo "📋 Creando Secret para Keycloak..."
kubectl create secret tls keycloak-tls \
  --cert="$CERT_DIR/tls.crt" \
  --key="$CERT_DIR/tls.key" \
  -n default --dry-run=client -o yaml | kubectl apply -f -

# 4. Crear ConfigMap para Keycloak Realm
echo "📋 Creando ConfigMap para realm de Keycloak..."
kubectl create configmap keycloak-realm-config \
  --from-file=realm-export.json="./keycloak/realm-export.json" \
  -n default --dry-run=client -o yaml | kubectl apply -f -

# 5. Crear ConfigMap para nginx (Frontend)
echo "📋 Creando ConfigMap para nginx.conf..."
kubectl create configmap frontend-nginx-config \
  --from-file=nginx.conf="./microservicio-frontend/nginx.conf" \
  -n default --dry-run=client -o yaml | kubectl apply -f -

echo ""
echo "✅ HTTPS configurado correctamente!"
echo ""
echo "URLs de acceso:"
echo "  Gateway:  https://127.0.0.1:30443"
echo "  Frontend: https://127.0.0.1:30080"
echo "  Keycloak: https://127.0.0.1:30443"
echo ""
echo "⚠️  Nota: Los certificados son autofirmados. El navegador mostrará una advertencia."
echo ""
