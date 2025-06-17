# =========================================
# 🐳 DOCKERFILE OPTIMIZADO PARA RENDER
# =========================================

# Etapa 1: Build
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

# Instalar curl para health checks
RUN apk add --no-cache curl

# Copiar archivos de configuración de Gradle
COPY gradle gradle
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .

# Hacer gradlew ejecutable
RUN chmod +x ./gradlew

# Copiar código fuente
COPY src src

# Construir la aplicación
RUN ./gradlew build -x test --no-daemon

# Etapa 2: Runtime
FROM eclipse-temurin:17-jre-alpine

# Información del mantenedor
LABEL maintainer="El Saborcito Team"
LABEL description="Backend API para El Saborcito - Sistema de gestión de restaurante"

# Instalar curl para health checks
RUN apk add --no-cache curl

# Crear directorio de trabajo
WORKDIR /app

# Copiar el JAR construido desde la etapa anterior
COPY --from=builder /app/build/libs/El_saborcito_back-0.0.1-SNAPSHOT.jar app.jar

# Crear usuario no-root para seguridad
RUN addgroup -g 1001 -S saborcito && \
    adduser -u 1001 -S saborcito -G saborcito

# Cambiar permisos
RUN chown -R saborcito:saborcito /app
USER saborcito

# Exponer el puerto 8080 (Render espera este puerto)
EXPOSE 8080

# Variables de entorno por defecto
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseContainerSupport"
ENV SPRING_PROFILES_ACTIVE=production

# Comando para ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar"]

# Healthcheck para Render
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/healthz || exit 1
