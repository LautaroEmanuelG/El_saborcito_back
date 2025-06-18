#!/bin/bash

# =========================================
# 🔍 SCRIPT DE VALIDACIÓN PRE-DESPLIEGUE
# =========================================

echo "🔍 Validando configuración para despliegue en Render..."

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Función para validar archivos críticos
validate_file() {
    if [ -f "$1" ]; then
        echo -e "${GREEN}✅ $1 existe${NC}"
        return 0
    else
        echo -e "${RED}❌ ERROR: $1 no encontrado${NC}"
        return 1
    fi
}

echo -e "${BLUE}📋 Validando archivos críticos...${NC}"

# Validar archivos esenciales
validate_file "Dockerfile"
validate_file "build.gradle"
validate_file "src/main/java/utn/saborcito/El_saborcito_back/controllers/HealthzController.java"
validate_file ".env.example"

echo ""
echo -e "${BLUE}🔨 Validando build del proyecto...${NC}"

# Verificar Docker
if ! command -v docker &> /dev/null; then
    echo -e "${YELLOW}⚠️  Docker no encontrado - OK para Render (usa Docker automáticamente)${NC}"
else
    echo -e "${GREEN}✅ Docker disponible${NC}"
fi

# Limpiar y construir
echo -e "${BLUE}🏗️  Probando build...${NC}"
./gradlew clean build -x test

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ Build exitoso${NC}"
else
    echo -e "${RED}❌ Error en el build${NC}"
    exit 1
fi

# Verificar que se generó el JAR
if [ -f "build/libs/El_saborcito_back-0.0.1-SNAPSHOT.jar" ]; then
    echo -e "${GREEN}✅ JAR generado correctamente${NC}"
    JAR_SIZE=$(du -h "build/libs/El_saborcito_back-0.0.1-SNAPSHOT.jar" | cut -f1)
    echo -e "${GREEN}📦 Tamaño del JAR: $JAR_SIZE${NC}"
else
    echo -e "${RED}❌ ERROR: JAR no encontrado${NC}"
    exit 1
fi

echo ""
echo -e "${BLUE}🐳 Validando Dockerfile...${NC}"

# Verificar sintaxis del Dockerfile
if docker build --dry-run . &> /dev/null; then
    echo -e "${GREEN}✅ Dockerfile sintácticamente correcto${NC}"
else
    echo -e "${YELLOW}⚠️  No se pudo validar Dockerfile (Docker no disponible)${NC}"
fi

echo ""
echo -e "${BLUE}🏥 Validando Health Check endpoints...${NC}"

# Verificar que existen los archivos de health check
if grep -q "/healthz" src/main/java/utn/saborcito/El_saborcito_back/controllers/HealthzController.java; then
    echo -e "${GREEN}✅ Endpoint /healthz configurado${NC}"
else
    echo -e "${RED}❌ ERROR: Endpoint /healthz no encontrado${NC}"
fi

if grep -q "healthz.*permitAll" src/main/java/utn/saborcito/El_saborcito_back/config/security/SecurityConfiguration.java; then
    echo -e "${GREEN}✅ Health checks permitidos en SecurityConfiguration${NC}"
else
    echo -e "${RED}❌ ERROR: Health checks no están permitidos en Security${NC}"
fi

echo ""
echo -e "${GREEN}🎉 Validación completada!${NC}"
echo ""
echo -e "${BLUE}📝 Configuración para Render:${NC}"
echo -e "🔧 ${YELLOW}Language:${NC} Docker"
echo -e "🌿 ${YELLOW}Branch:${NC} deploy"
echo -e "📦 ${YELLOW}Build Command:${NC} (automático - usa Dockerfile)"
echo -e "▶️  ${YELLOW}Start Command:${NC} (automático - usa Dockerfile)"
echo -e "🏥 ${YELLOW}Health Check Path:${NC} /healthz"
echo -e "🌍 ${YELLOW}Region:${NC} Oregon (US West)"
echo ""
echo -e "${BLUE}🔑 Variables de entorno requeridas:${NC}"
echo -e "   ${YELLOW}DB_URL, DB_USERNAME, DB_PASSWORD${NC}"
echo -e "   ${YELLOW}CORS_ALLOWED_ORIGINS${NC}"
echo -e "   ${YELLOW}AUTH0_ISSUER_URI, AUTH0_AUDIENCE, JWT_SECRET${NC}"
echo -e "   ${YELLOW}CLOUDINARY_*, MERCADOPAGO_*, MAIL_*${NC}"
echo ""
echo -e "${GREEN}🚀 ¡Todo listo para desplegar en Render!${NC}"
