#!/bin/bash

# Script para reconstruir y redeployar el frontend en Minikube
# Uso: ./deploy-frontend.sh

set -e  # Salir si hay error

echo "================================"
echo "🚀 Iniciando Deploy del Frontend"
echo "================================"

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuración
MINIKUBE_IP=$(minikube ip)
FRONTEND_DIR="./microservicio-frontend"
DOCKER_IMAGE="pbust0sn/frontend:latest"
NAMESPACE="default"

echo -e "${BLUE}ℹ️  Minikube IP: $MINIKUBE_IP${NC}"

# Step 1: Build Frontend
echo ""
echo -e "${YELLOW}📦 Step 1: Compilando el frontend...${NC}"
cd "$FRONTEND_DIR"
npm run build
cd ..
echo -e "${GREEN}✅ Frontend compilado exitosamente${NC}"

# Step 2: Configurar Docker para Minikube
echo ""
echo -e "${YELLOW}🐳 Step 2: Configurando Docker para Minikube...${NC}"
eval $(minikube docker-env)
echo -e "${GREEN}✅ Docker configurado${NC}"

# Step 3: Build Docker Image
echo ""
echo -e "${YELLOW}🐳 Step 3: Construyendo imagen Docker...${NC}"
cd "$FRONTEND_DIR"
docker build -t "$DOCKER_IMAGE" .
cd ..
echo -e "${GREEN}✅ Imagen Docker construida${NC}"

# Step 4: Verificar que la imagen existe
echo ""
echo -e "${YELLOW}🔍 Step 4: Verificando imagen Docker...${NC}"
docker images | grep frontend
echo -e "${GREEN}✅ Imagen verificada${NC}"

# Step 5: Forzar actualización en Minikube
echo ""
echo -e "${YELLOW}🔄 Step 5: Reiniciando deployment del frontend...${NC}"

# Aumentar imagePullPolicy para forzar re-pull
kubectl set env deployment/frontend -n $NAMESPACE \
  RESTART_TIMESTAMP="$(date +%s)" --overwrite || echo "Deploy no existe aún"

# Hacer rollout restart
kubectl rollout restart deployment/frontend -n $NAMESPACE 2>/dev/null || {
  echo -e "${YELLOW}⚠️  Deployment no existe, creándolo...${NC}"
  kubectl apply -f deployments/frontend-deployment.yaml -n $NAMESPACE
}

echo -e "${GREEN}✅ Deployment reiniciado${NC}"

# Step 6: Esperar a que el pod esté listo
echo ""
echo -e "${YELLOW}⏳ Step 6: Esperando a que el pod esté listo (60 segundos)...${NC}"
kubectl rollout status deployment/frontend -n $NAMESPACE --timeout=60s
echo -e "${GREEN}✅ Pod está listo${NC}"

# Step 7: Verificar pods
echo ""
echo -e "${YELLOW}🔍 Step 7: Estado de los pods${NC}"
kubectl get pods -l app=frontend -n $NAMESPACE

# Step 8: Mostrar logs
echo ""
echo -e "${YELLOW}📋 Step 8: Últimos logs del frontend${NC}"
kubectl logs -l app=frontend -n $NAMESPACE --tail=30 || echo "No hay logs todavía"

# Step 9: URL de acceso
echo ""
echo -e "${GREEN}✅ DEPLOYMENT COMPLETADO${NC}"
echo ""
echo -e "${BLUE}🌐 URLs de acceso:${NC}"
echo -e "  - Local: http://localhost/frontend (si tienes port-forward)"
echo -e "  - Minikube: http://$MINIKUBE_IP:30080"
echo -e "  - Keycloak: http://$MINIKUBE_IP:30443"
echo ""
echo -e "${YELLOW}📝 Para ver logs en tiempo real:${NC}"
echo "  kubectl logs -l app=frontend -n $NAMESPACE -f"
echo ""
echo -e "${YELLOW}🔧 Para hacer port-forward:${NC}"
echo "  kubectl port-forward service/frontend 8080:80 -n $NAMESPACE"
echo ""
