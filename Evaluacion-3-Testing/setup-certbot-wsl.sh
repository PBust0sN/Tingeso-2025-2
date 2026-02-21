#!/bin/bash

# Script para generar certificados Let's Encrypt con Certbot
# Ejecutar en WSL (Windows Subsystem for Linux)
# 
# Uso: bash setup-certbot-wsl.sh

set -e

# Colores
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

DOMAIN1="toolrent-tingeso.duckdns.org"
DOMAIN2="auth.toolrent-tingeso.duckdns.org"
EMAIL="patricio0440bustos@gmail.com"  # Cambiar por tu email

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}  Certbot Setup - Let's Encrypt${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""
echo -e "${YELLOW}Dominios a proteger:${NC}"
echo "  1. $DOMAIN1"
echo "  2. $DOMAIN2"
echo ""

# Verificar si está en WSL
if ! grep -qi microsoft /proc/version 2>/dev/null; then
    echo -e "${YELLOW}⚠️  Este script está diseñado para WSL (Windows Subsystem for Linux)${NC}"
    echo -e "${YELLOW}Si estás en otra plataforma, adapta los comandos según sea necesario${NC}"
    echo ""
fi

# Paso 1: Instalar Certbot
echo -e "${BLUE}📦 Paso 1: Instalando Certbot...${NC}"
if command -v certbot &> /dev/null; then
    echo -e "${GREEN}✅ Certbot ya está instalado${NC}"
    certbot --version
else
    echo -e "${YELLOW}📥 Instalando Certbot...${NC}"
    sudo apt-get update
    sudo apt-get install -y certbot python3-certbot-nginx
    echo -e "${GREEN}✅ Certbot instalado${NC}"
fi

echo ""

# Paso 2: Generar certificados
echo -e "${BLUE}🔐 Paso 2: Generando certificados Let's Encrypt...${NC}"
echo -e "${YELLOW}Dominios:${NC}"
echo "  - $DOMAIN1"
echo "  - $DOMAIN2"
echo ""

# Crear certificado con Certbot (modo standalone)
echo -e "${BLUE}📝 Ejecutando Certbot...${NC}"
echo ""

# Opción 1: Usar standalone (sin servidor web) con ambos dominios
sudo certbot certonly --standalone \
    -d "$DOMAIN1" \
    -d "$DOMAIN2" \
    --agree-tos \
    -m "$EMAIL" \
    --non-interactive \
    --preferred-challenges=http

if [ $? -eq 0 ]; then
    echo ""
    echo -e "${GREEN}✅ Certificados generados exitosamente${NC}"
    echo ""
    echo -e "${BLUE}📂 Ubicación de los certificados:${NC}"
    echo "   /etc/letsencrypt/live/$DOMAIN1/"
    echo ""
    echo -e "${BLUE}📄 Archivos:${NC}"
    ls -lah "/etc/letsencrypt/live/$DOMAIN1/" | grep -E "fullchain|privkey"
    echo ""
    echo -e "${BLUE}✅ Ambos dominios están en el mismo certificado:${NC}"
    echo "   - $DOMAIN1"
    echo "   - $DOMAIN2"
    echo ""
    echo -e "${YELLOW}⚠️  IMPORTANTE: Los certificados caducan en 90 días${NC}"
    echo ""
    echo -e "${BLUE}🚀 Próximos pasos:${NC}"
    echo "   1. Levanta los contenedores:"
    echo "      docker-compose up -d"
    echo ""
    echo "   2. Verifica que nginx esté usando los certificados:"
    echo "      docker logs nginx"
    echo ""
    echo "   3. Para renovación automática, configura cron:"
    echo "      sudo certbot renew --quiet"
    echo ""
else
    echo -e "${RED}❌ Error al generar certificados${NC}"
    echo -e "${YELLOW}Posibles causas:${NC}"
    echo "  - El dominio no está apuntando a tu IP"
    echo "  - El puerto 80 está bloqueado o en uso"
    echo "  - Email inválido"
    echo ""
    echo -e "${BLUE}💡 Alternativa: Usar modo DNS${NC}"
    echo "   Si el puerto 80 no está disponible, usa:"
    echo "   sudo certbot certonly --manual --preferred-challenges=dns -d $DOMAIN1 -d $DOMAIN2"
    exit 1
fi

echo ""
echo -e "${BLUE}========================================${NC}"
echo -e "${GREEN}✅ Instalación completada${NC}"
echo -e "${BLUE}========================================${NC}"
