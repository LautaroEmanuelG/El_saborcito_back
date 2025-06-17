@echo off
REM =========================================
REM 🧪 SCRIPT PARA PROBAR DOCKERFILES
REM =========================================

echo 🧪 Probando ambas versiones de Dockerfile...

REM Probar Dockerfile principal
echo 📦 Probando Dockerfile principal...
docker build -t el-saborcito-test-1 .
if errorlevel 1 (
    echo ❌ Dockerfile principal falló
    echo 🔄 Probando Dockerfile alternativo...
    
    REM Usar Dockerfile alternativo
    docker build -t el-saborcito-test-2 -f Dockerfile.alternative .
    if errorlevel 1 (
        echo ❌ Ambos Dockerfiles fallaron
        echo 💡 Revisando configuración...
        
        REM Mostrar estructura de archivos
        echo 📁 Estructura de archivos Gradle:
        dir gradle\wrapper
        echo.
        echo 📄 Contenido de gradlew:
        type gradlew | head -5
        
        pause
        exit /b 1
    ) else (
        echo ✅ Dockerfile alternativo funcionó!
        echo 🔄 Reemplazando Dockerfile principal...
        copy Dockerfile.alternative Dockerfile
        echo ✅ Dockerfile actualizado
    )
) else (
    echo ✅ Dockerfile principal funcionó correctamente!
)

echo 🎉 Build Docker exitoso!
echo 🚀 Para probar: docker run -p 8080:8080 el-saborcito-test-1
pause
