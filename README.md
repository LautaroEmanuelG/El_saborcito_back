# 🍔🍟 El Saborcito - Backend

## 📜 Descripción del Proyecto

Este proyecto consiste en el desarrollo de un sistema web para un restaurante que permite gestionar ventas y administrar finanzas de manera eficiente. El backend está construido utilizando Java y Spring Boot, y se conecta a una base de datos MySQL para el almacenamiento de datos.

## 🗄️ Base de Datos MySQL

La aplicación utiliza MySQL como sistema de gestión de base de datos para el almacenamiento persistente de datos.

![Base de Datos El Saborcito](./data/El%20saborcito.png)

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
SERVER_PORT=5252
```

## 🛠️ Tecnologías Utilizadas

- Java
- Spring Boot
- Gradle
- MySQL
- Swagger

## 🚀 Configuración del Entorno

1. Clonar el repositorio:

```bash
git clone https://github.com/LautaroEmanuelG/El_saborcito_back.git
```

2. Configurar MySQL y crear el archivo `.env` (ver sección de Variables de Entorno)

3. Construir el proyecto:

```bash
./gradlew build
```

4. Ejecutar la aplicación:

```bash
./gradlew bootRun
```

5. Acceder a la aplicación:
   - Swagger UI: http://localhost:5252/swagger-ui.html

## 📄 Endpoints de la API Swagger

La documentación de la API se encuentra disponible en Swagger UI en http://localhost:5252/swagger-ui.html

### 🍽️ Productos

- GET /productos: Listar todos los productos.
- GET /productos/{id}: Obtener un producto por ID.
- POST /productos: Crear un nuevo producto.
- PUT /productos/{id}: Actualizar un producto existente.
- DELETE /productos/{id}: Eliminar un producto por ID.

### 🎟️ Tickets

- GET /tickets: Listar todos los tickets.
- GET /tickets/{id}: Obtener un ticket por ID.
- POST /tickets: Crear un nuevo ticket.
- PUT /tickets/{id}: Actualizar un ticket existente.
- DELETE /tickets/{id}: Eliminar un ticket por ID.

### 💳 Transacciones

- GET /transacciones: Listar todas las transacciones.
- GET /transacciones/{id}: Obtener una transacción por ID.
- POST /transacciones: Crear una nueva transacción.
- PUT /transacciones/{id}: Actualizar una transacción existente.
- DELETE /transacciones/{id}: Eliminar una transacción por ID.

## 🔗 Uso de una Tabla Intermedia Explícita

En este proyecto, se utiliza una tabla intermedia explícita para gestionar la relación muchos a muchos entre Ticket y Producto. En lugar de usar @ManyToMany con @JoinTable, se ha creado una entidad llamada TicketProducto que representa esta relación. Esta entidad no solo contiene las claves foráneas, sino que también incluye atributos adicionales relevantes para la relación, como la cantidad de productos en un ticket.

- Ejemplo de la Entidad TicketProducto:

```java
@Entity
public class TicketProducto {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    private Integer cantidad;

    @ManyToOne
    @JoinColumn(name = "ticketId")
    @JsonIgnore
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "productoId")
    private Producto producto;
}
```

### ✅ Ventajas:

- Mayor flexibilidad: Permite añadir atributos adicionales a la tabla intermedia, lo que es útil si necesitas almacenar información adicional sobre la relación. Por ejemplo, podrías querer guardar la cantidad de un producto vendido en un ticket.
- Facilidad de escalabilidad: Si en el futuro surge la necesidad de modificar o ampliar la tabla intermedia, este enfoque permite hacerlo sin romper la estructura existente.
- Acceso directo a la tabla intermedia: Tener una entidad para la tabla intermedia facilita consultas y operaciones específicas sobre esa relación, como acceder a todos los productos de un ticket en función de atributos adicionales.

### ❌ Desventajas:

- Mayor complejidad: Este enfoque añade complejidad y más código, ya que tienes que definir una entidad adicional para la tabla intermedia y gestionar su persistencia.
- Overhead inicial: Para relaciones simples, este método puede parecer más complejo de lo necesario si solo estás almacenando las claves foráneas.
