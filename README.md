# 🍔🍟 El Saborcito - Backend

**Integrantes:** Pablo Osorio, Jenifer Contreras, Lautaro Gonzalez, Matias Araya y Eros Mariotti.

## 📜 Descripción del Proyecto

Este proyecto consiste en el desarrollo de un sistema web para un restaurante que permite gestionar ventas y administrar finanzas de manera eficiente. El backend está construido utilizando Java y Spring Boot, y se conecta a una base de datos MySQL para el almacenamiento de datos.

Este proyecto implementa los siguientes módulos (Historias de Usuario):

- **Gestión de Usuarios y Autenticación** (Auth0)

- **Registro y Gestión de Clientes**

- **Pedidos y Flujo de Cocina/Delivery/TakeAway**

- **Facturación** 

- **Rubros e Ingredientes** (CRUD de categorías, insumos y productos)

- **Registro de Compras de Ingredientes**

- **Estadísticas e Informes:**

   - Ranking de Clientes

   - Ranking de Productos

   - Movimientos Monetarios (Ingresos, Costos, Ganancias)

- **Promociones y Detalle de Pedidos con Promociones**

## 🗄️ Base de Datos MySQL

La aplicación utiliza MySQL como sistema de gestión de base de datos para el almacenamiento persistente de datos.

## 🔐 Variables de Entorno

El proyecto utiliza un archivo `.env` para gestionar variables de entorno sensibles. Para configurar tu entorno:

1. Crea un archivo `.env` en la raíz del proyecto basándote en el archivo `.env.example`
2. Completa las variables con tus propios valores

```properties
# Ejemplo de configuración en .env
DB_URL=jdbc:mysql://localhost:3306/saborcito_db?createDatabaseIfNotExist=true&serverTimezone=UTC
DB_USERNAME=tu_usuario
DB_PASSWORD=tu_contraseña
DB_DRIVER=com.mysql.cj.jdbc.Driver
JPA_DIALECT=org.hibernate.dialect.MySQLDialect

# Configuración del servidor
SERVER_PORT=5252
```

## 📦 Tecnologías y Dependencias

- **Java 17**  
- **Spring Boot 3.3.4**  
  - spring-boot-starter-web  
  - spring-boot-starter-data-jpa  
  - spring-boot-starter-security  
  - spring-boot-starter-oauth2-resource-server  
  - spring-boot-starter-mail  
  - spring-boot-starter-validation  
- **Seguridad & JWT**  
  - com.auth0:java-jwt (4.4.0)  
  - io.jsonwebtoken:jjwt-api / jjwt-impl / jjwt-jackson (0.11.5)  
- **Bases de datos**  
  - MySQL Connector/J (8.0.33)  
  - PostgreSQL Driver  
- **Mapeo y generación de código**  
  - Lombok  
  - MapStruct (+ lombok-mapstruct-binding)  
- **Documentación OpenAPI**  
  - springdoc-openapi-starter-webmvc-ui (Swagger UI)  
- **Procesamiento de documentos**  
  - Apache POI (poi & poi-ooxml) → Excel  
  - OpenPDF → PDF  
- **Cloudinary**  
  - cloudinary-http5 (2.0.0) → gestión de imágenes  
- **Configuración / Env Vars**  
  - spring-dotenv (me.paulschwarz:spring-dotenv)  
- **Desarrollo & Tests**  
  - spring-boot-devtools  
  - spring-boot-starter-test + JUnit Platform  
- **Build & Tooling**  
  - Gradle  
  - Java toolchain (Java 17)
 

## 🚀 Configuración del Entorno

1. Clonar el repositorio:

```bash
git clone https://github.com/LautaroEmanuelG/El_saborcito_back
```

2. Configurar MySQL y crear el archivo `.env`, detro del proyecto guiarse de `.env.example` (ver sección de [🔐 Variables de Entorno](##-🔐-Variables-de-Entorno)
))

3. Construir el proyecto:

```bash
./gradlew build
```

4. Ejecutar la aplicación:

```bash
./gradlew bootRun
```

5. Acceder a la aplicación:

   Swagger UI: [http://localhost:5252/swagger-ui.html](http://localhost:5252/swagger-ui.html)


## 🛠️ Requisitos

- Java 17+
- Gradle
- MySQL 8+

## 📖 Documentación

Todos los endpoints y DTOs están documentados en Swagger. Accede a:[http://localhost:5252/swagger-ui.html](http://localhost:5252/swagger-ui.html)

