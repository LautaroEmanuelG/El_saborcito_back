@echo off
REM =========================================
REM 🚀 SCRIPT DE BUILD PARA RENDER (Windows)
REM =========================================

echo 🔨 Iniciando build del proyecto El Saborcito para PRODUCCIÓN...

REM Verificar que Docker esté corriendo
docker --version >nul 2>&1
if errorlevel 1 (
    echo ❌ ERROR: Docker no está instalado o corriendo
    echo 💡 Instala Docker Desktop y asegúrate que esté corriendo
    pause
    exit /b 1
)

echo ✅ Docker detectado

REM Limpiar builds anteriores
echo 🧹 Limpiando builds anteriores...
gradlew.bat clean

REM Construir el proyecto
echo 📦 Construyendo el proyecto para producción...
gradlew.bat build -x test

REM Verificar que el JAR se creó correctamente
if exist "build\libs\El_saborcito_back-0.0.1-SNAPSHOT.jar" (
    echo ✅ Build completado exitosamente!
    echo 📋 JAR generado: build\libs\El_saborcito_back-0.0.1-SNAPSHOT.jar
    
    REM Mostrar el tamaño del JAR
    for %%I in ("build\libs\El_saborcito_back-0.0.1-SNAPSHOT.jar") do echo 📦 Tamaño del JAR: %%~zI bytes
) else (
    echo ❌ Error: No se pudo generar el JAR
    pause
    exit /b 1
)

REM Construir imagen Docker (opcional para test local)
echo 🐳 ¿Quieres construir la imagen Docker localmente? (y/n)
set /p buildDocker=
if /i "%buildDocker%"=="y" (
    echo 🐳 Construyendo imagen Docker...
    docker build -t el-saborcito-backend .
    if errorlevel 1 (
        echo ❌ Error construyendo imagen Docker
    ) else (
        echo ✅ Imagen Docker construida exitosamente
        echo 🚀 Para probar localmente: docker run -p 8080:8080 el-saborcito-backend
    )
)

echo.
echo 🎉 Build terminado - Listo para despliegue en Render!
echo 📋 Configuración para Render:
echo    🔧 Language: Docker
echo    📦 Build Command: (automático)
echo    ▶️  Start Command: (automático)
echo    🏥 Health Check: /healthz
echo.
pause
