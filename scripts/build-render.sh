#!/bin/bash

# =========================================
# 🚀 SCRIPT DE BUILD PARA RENDER (Linux/Mac)
# =========================================

echo "🔨 Iniciando build del proyecto El Saborcito para PRODUCCIÓN..."

# Verificar que Docker esté corriendo
if ! command -v docker &> /dev/null; then
    echo "❌ ERROR: Docker no está instalado"
    echo "💡 Instala Docker: https://docs.docker.com/get-docker/"
    exit 1
fi

if ! docker info &> /dev/null; then
    echo "❌ ERROR: Docker no está corriendo"
    echo "💡 Inicia Docker daemon"
    exit 1
fi

echo "✅ Docker detectado y funcionando"

# Limpiar builds anteriores
echo "🧹 Limpiando builds anteriores..."
./gradlew clean

# Construir el proyecto
echo "📦 Construyendo el proyecto para producción..."
./gradlew build -x test

# Verificar que el JAR se creó correctamente
if [ -f "build/libs/El_saborcito_back-0.0.1-SNAPSHOT.jar" ]; then
    echo "✅ Build completado exitosamente!"
    echo "📋 JAR generado: build/libs/El_saborcito_back-0.0.1-SNAPSHOT.jar"
    JAR_SIZE=$(du -h "build/libs/El_saborcito_back-0.0.1-SNAPSHOT.jar" | cut -f1)
    echo "📦 Tamaño del JAR: $JAR_SIZE"
else
    echo "❌ Error: No se pudo generar el JAR"
    exit 1
fi

# Construir imagen Docker (opcional para test local)
echo "🐳 ¿Quieres construir la imagen Docker localmente? (y/n)"
read -r buildDocker
if [[ "$buildDocker" == "y" || "$buildDocker" == "Y" ]]; then
    echo "🐳 Construyendo imagen Docker..."
    if docker build -t el-saborcito-backend .; then
        echo "✅ Imagen Docker construida exitosamente"
        echo "🚀 Para probar localmente: docker run -p 8080:8080 el-saborcito-backend"
    else
        echo "❌ Error construyendo imagen Docker"
        exit 1
    fi
fi

echo ""
echo "🎉 Build terminado - Listo para despliegue en Render!"
echo "📋 Configuración para Render:"
echo "   🔧 Language: Docker"
echo "   📦 Build Command: (automático)"
echo "   ▶️  Start Command: (automático)"
echo "   🏥 Health Check: /healthz"
echo ""
