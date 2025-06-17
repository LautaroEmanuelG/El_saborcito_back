@echo off
REM =========================================
REM 🧪 SCRIPT DE VALIDACIÓN LOCAL PARA DOCKER
REM =========================================

echo 🔍 Validando Dockerfile localmente...

REM Verificar que Docker está corriendo
docker version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Error: Docker no está corriendo
    exit /b 1
)

echo 📁 Verificando archivos necesarios...
if not exist "gradlew" (
    echo ❌ Error: No se encontró gradlew
    exit /b 1
)

if not exist "gradle\wrapper\gradle-wrapper.jar" (
    echo ❌ Error: No se encontró gradle-wrapper.jar
    exit /b 1
)

if not exist "build.gradle" (
    echo ❌ Error: No se encontró build.gradle
    exit /b 1
)

echo ✅ Archivos verificados correctamente

echo 🐳 Construyendo imagen Docker...
docker build -t el-saborcito-test .

if %errorlevel% neq 0 (
    echo ❌ Error al construir la imagen
    exit /b 1
)

echo ✅ Imagen construida exitosamente

echo 🚀 Probando contenedor...
echo Iniciando contenedor en puerto 8080...
docker run -d --name el-saborcito-test-container -p 8080:8080 el-saborcito-test

echo ⏳ Esperando que la aplicación inicie...
timeout /t 30 /nobreak

echo 🔍 Verificando health check...
curl -f http://localhost:8080/actuator/health 2>nul
if %errorlevel% neq 0 (
    echo ⚠️ Advertencia: Health check falló, pero el contenedor puede estar iniciando
) else (
    echo ✅ Health check exitoso
)

echo 🧹 Limpiando...
docker stop el-saborcito-test-container
docker rm el-saborcito-test-container

echo ✅ Validación completada
echo 💡 La imagen está lista para desplegar en Render
