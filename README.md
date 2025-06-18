# 🍔🍟 El Saborcito - Backend

**Integrantes:** Jenifer Contreras, Lautaro Gonzalez, Matias Araya,Eros Mariotti, Pablo Osorio.

---

## ✨ Características Destacadas

- Gestión integral de restaurante: ventas, pedidos, inventario, promociones y estadísticas.
- Seguridad robusta con Auth0 y JWT.
- Generación automática de facturas en PDF y envío por email.
- Gestión de imágenes en la nube con Cloudinary.
- Documentación interactiva y pruebas de API con Swagger UI.
- Arquitectura modular y escalable, lista para producción.

---

## 📜 Descripción del Proyecto

El backend de "El Saborcito" es una aplicación desarrollada en Java 17 con Spring Boot 3.3.4, orientada a la gestión integral de un restaurante. Permite administrar ventas, finanzas, usuarios, clientes, pedidos, facturación, inventario, promociones y estadísticas. Utiliza una base de datos MySQL para la persistencia y variables sensibles gestionadas mediante un archivo `.env`.

El sistema implementa autenticación y autorización con Auth0 y JWT, y cuenta con módulos para registro y gestión de clientes, pedidos (cocina, delivery, take away), facturación, administración de rubros, insumos y productos, compras de ingredientes, estadísticas (ranking de clientes y productos, movimientos monetarios), y manejo de promociones. Además, gestiona imágenes con Cloudinary y genera documentos Excel y PDF usando Apache POI y OpenPDF.

El proyecto utiliza Gradle como sistema de construcción, incluye herramientas de desarrollo y testing como spring-boot-devtools y spring-boot-starter-test, y documenta sus endpoints y DTOs con Swagger (OpenAPI). El código fuente está organizado en controladores, servicios, repositorios, modelos, DTOs y mappers (MapStruct y Lombok). La configuración de la base de datos y el servidor se realiza mediante variables de entorno y archivos de propiedades.

El backend está preparado para ser ejecutado localmente tras clonar el repositorio, configurar la base de datos y las variables de entorno, construir el proyecto y arrancar la aplicación. La documentación de la API es accesible vía Swagger UI. El sistema está diseñado para ser robusto, seguro y escalable, facilitando la gestión integral de un restaurante.

---

## 📂 Estructura del Proyecto (Backend)

```
src/
├── main/
│   ├── java/utn/saborcito/
│   │   ├── controller/         # Controladores REST: exponen los endpoints de la API
│   │   ├── service/            # Servicios: lógica de negocio y orquestación de procesos
│   │   ├── repository/         # Repositorios: acceso a datos, interfaces JPA
│   │   ├── model/              # Modelos: entidades JPA y modelos de dominio
│   │   ├── dto/                # Data Transfer Objects: objetos para transferencia de datos
│   │   ├── mapper/             # Mappers: conversión entre entidades y DTOs (MapStruct)
│   │   ├── security/           # Seguridad: configuración de Auth0, JWT, roles y filtros
│   │   └── exception/          # Manejo centralizado de excepciones y errores
│   └── resources/
│       ├── application.properties  # Configuración principal de Spring Boot
│       ├── static/                # Recursos estáticos (imágenes, etc.)
│       ├── templates/             # Plantillas para emails o reportes (si aplica)
│       └── ...                    # Otros recursos (fuentes, archivos de datos, etc.)
└── test/
    └── java/utn/saborcito/        # Tests unitarios y de integración
```

- **controller/**: Define los endpoints REST y gestiona las solicitudes HTTP.
- **service/**: Implementa la lógica de negocio, validaciones y reglas del sistema.
- **repository/**: Interfaces para el acceso a la base de datos usando Spring Data JPA.
- **model/**: Contiene las entidades que representan las tablas de la base de datos y otros modelos de dominio.
- **dto/**: Objetos para transferir datos entre capas, evitando exponer entidades directamente.
- **mapper/**: Utiliza MapStruct para mapear entre entidades y DTOs de forma eficiente.
- **security/**: Configuración de seguridad, autenticación y autorización (Auth0, JWT, roles, filtros).
- **exception/**: Manejo global de errores y excepciones personalizadas.
- **resources/**: Archivos de configuración, recursos estáticos y plantillas.
- **test/**: Pruebas unitarias y de integración para garantizar la calidad del código.

---

## 🛠️ Stack Tecnológico

- **Java 17**
- **Spring Boot 3.3.4** (Web, Data JPA, Security, OAuth2, Validation, Mail)
- **MySQL 8+**
- **Gradle** (build y toolchain)
- **Auth0 & JWT** (seguridad y autenticación)
- **Swagger/OpenAPI** (documentación interactiva)
- **Lombok & MapStruct** (boilerplate y mapeo DTO)
- **Cloudinary** (gestión de imágenes)
- **Apache POI & OpenPDF** (Excel y PDF)
- **JUnit, Spring Test** (testing)
- **spring-dotenv** (variables de entorno)

---

## 📦 Módulos y Funcionalidades

- **Gestión de Usuarios y Autenticación:** Auth0, JWT, roles y permisos.
- **Registro y Gestión de Clientes y Empleados:** ABM completo, perfiles y seguridad.
- **Pedidos y Flujos de Cocina/Delivery/TakeAway:** Gestión de estados, cocina, delivery y retiro.
- **Facturación:** Generación automática, PDF, envío por mail.
- **Rubros, Insumos y Productos:** CRUD de categorías, insumos y manufacturados.
- **Registro de Compras de Ingredientes:** Control de stock y compras.
- **Promociones y Detalle de Pedidos:** Aplicación y gestión de promociones.
- **Estadísticas e Informes:** Ranking de clientes y productos, movimientos monetarios (ingresos, costos, ganancias).
- **Gestión de Imágenes:** Subida y administración con Cloudinary.
- **Documentación interactiva:** Swagger UI para probar y consultar la API.

---

## 🗄️ Base de Datos

- **MySQL 8+**  
- Estructura relacional robusta, relaciones entre entidades, constraints y migraciones automáticas.
- Variables de conexión configurables por `.env`.

![Modelo de Base de Datos](https://github.com/user-attachments/assets/d5c2878e-af61-4a68-886c-fce5cf9b9555)

---

## 🔐 Variables de Entorno

El proyecto utiliza un archivo `.env` para gestionar variables sensibles.  
Ejemplo:

```properties
DB_URL=jdbc:mysql://localhost:3306/saborcito_db?createDatabaseIfNotExist=true&serverTimezone=UTC
DB_USERNAME=tu_usuario
DB_PASSWORD=tu_contraseña
DB_DRIVER=com.mysql.cj.jdbc.Driver
JPA_DIALECT=org.hibernate.dialect.MySQLDialect
SERVER_PORT=5252
# ...otros valores (Auth0, Cloudinary, etc.)
```

---

## 🚀 Instalación y Ejecución

### 1️⃣ Clonar el repositorio

```bash
git clone https://github.com/LautaroEmanuelG/El_saborcito_back
cd El_saborcito_back
```

### 2️⃣ Configurar MySQL y variables de entorno

- Crear la base de datos en MySQL.
- Copiar `.env.example` a `.env` y completar los valores.

### 3️⃣ Construir el proyecto

```bash
./gradlew build
```

### 4️⃣ Ejecutar la aplicación

```bash
./gradlew bootRun
```

### 5️⃣ Acceder a la documentación interactiva

- Swagger UI: [http://localhost:5252/swagger-ui.html](http://localhost:5252/swagger-ui.html)

---

## 🛠️ Requisitos

- Java 17+
- Gradle
- MySQL 8+

---

## 🧑‍💻 Convenciones de Código y Buenas Prácticas

- Uso de Java 17 y convenciones estándar de nomenclatura.
- Arquitectura en capas (controller, service, repository, model, dto, mapper, security, exception).
- Uso de DTOs para evitar exponer entidades directamente.
- Manejo centralizado de excepciones.
- Validaciones con anotaciones de Spring y Bean Validation.
- Seguridad y roles gestionados con JWT y Auth0.
- Documentación automática y pruebas de endpoints con Swagger.
- Pruebas unitarias y de integración con JUnit y Spring Test.
- Uso de variables de entorno para datos sensibles.

---

## ☁️ Despliegue en Producción

El sistema está preparado para despliegue en entornos productivos. Se recomienda:

- Configurar variables de entorno y credenciales seguras.
- Utilizar servicios gestionados para la base de datos y almacenamiento de imágenes.
- Habilitar HTTPS y configurar CORS según el frontend.
- Monitorear logs y métricas del sistema.
- Realizar backups periódicos de la base de datos.

---

## 📖 Documentación

- **Swagger UI:** [http://localhost:5252/swagger-ui.html](http://localhost:5252/swagger-ui.html)
- **Documentación técnica:** En endpoints y DTOs vía Swagger.

---

## 👥 Equipo de Desarrollo

- **González**  
  Líder técnico y referente del equipo. Mantuvo el orden, la calidad y la visión global del sistema, resolviendo bloqueos y asegurando la integración de todos los módulos.

- **Mariotti**  
  Fundamental en la organización y avance del equipo. Desarrolló reportes, estadísticas y colaboró en la coordinación y soporte diario.

- **Araya**  
  Responsable de los ABM (CRUD), lógica de manufacturados, control de stock e inventario, asegurando la correcta actualización de insumos y productos.

- **Contreras**  
  Encargada de la seguridad, diseño y armado de la base de datos y relaciones entre entidades. Desarrolló toda la gestión de usuarios, clientes y empleados (registro, login, perfiles y administración).

- **Osorio**  
  Estructuración del backend, desarrollo de cocina, reportes, generación de facturas e integración de Mercado Pago. Colaboró en la arquitectura y organización general.

> **Todos los integrantes participaron activamente en backend, frontend y base de datos, demostrando un compromiso y una colaboración excepcionales.**

---

### 💬 Mensaje del equipo (por Osorio)

> “Gracias a cada uno por el esfuerzo, la dedicación y la pasión en este proyecto. No solo construimos un sistema robusto y profesional, sino que también formamos un equipo unido, donde cada aporte fue fundamental. ¡Orgulloso de lo que logramos juntos!”

---

## 📚 Recursos y enlaces útiles

- **Repositorio Backend:** [El_saborcito_back](https://github.com/LautaroEmanuelG/El_saborcito_back)
- **Repositorio Frontend:** [El_saborcito_front](https://github.com/LautaroEmanuelG/El_saborcito_front)
- **JIRA (Gestión de tareas):** [Tablero de tareas](https://erosmariotti401-1742469568399.atlassian.net/jira/core/projects/ES/board)
- **Google Drive:** [Carpeta Drive](https://drive.google.com/drive/folders/1AidjC484Kk5kz4Sw-7hI5u736dp-7iz7)
- **Figma:** [Proyecto Figma](https://www.figma.com/design/h92Em0W6NbAVjUaHnliwJ5/Laboratorio-3-El-saborcito?node-id=0-1&p=f)

**Usuarios de GitHub del equipo:**
- [JenJen007](https://github.com/JenJen007)
- [ErosMariotti](https://github.com/ErosMariotti)
- [matias-araya-02](https://github.com/matias-araya-02)
- [matiasman1](https://github.com/matiasman1)
- [pabloosoor](https://github.com/pabloosoor)

**Contacto:** elsaborcito2024@gmail.com

---

<div align="center">
  
**🚀 ¡Gracias por usar El Saborcito! 🚀**

Si te gusta el proyecto, no olvides darle una ⭐

</div>
