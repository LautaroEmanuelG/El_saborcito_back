#!/bin/bash

# =========================================
# 🚀 BUILD SCRIPT OPTIMIZADO PARA RENDER
# =========================================

set -e  # Salir si hay errores

echo "🔧 Iniciando build optimizado para Render..."

# Verificar que estamos en el directorio correcto
if [[ ! -f "gradlew" ]]; then
    echo "❌ Error: No se encontró gradlew. Ejecuta desde el directorio raíz del proyecto."
    exit 1
fi

# Hacer ejecutable el gradlew
chmod +x gradlew

echo "📦 Descargando dependencias..."
./gradlew dependencies --no-daemon --quiet || true

echo "🏗️ Compilando aplicación..."
./gradlew bootJar -x test --no-daemon --info

echo "✅ Build completado exitosamente!"
echo "📂 JAR generado en: build/libs/"
ls -la build/libs/*.jar

echo "🐳 Para probar con Docker:"
echo "docker build -t el-saborcito-back ."
echo "docker run -p 8080:8080 el-saborcito-back"
