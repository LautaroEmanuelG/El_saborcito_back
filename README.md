# 🍔🍟 El Saborcito - Backend

## Descripción del Proyecto

Este proyecto consiste en el desarrollo de un sistema web para un restaurante que permite gestionar ventas y administrar finanzas de manera eficiente. El backend está construido utilizando Java y Spring Boot, y se conecta a una base de datos H2 para el almacenamiento de datos.  

## Tecnologías Utilizadas

- Java
- Spring Boot
- Gradle
- H2 Database
- Swagger
- (Proximamente) PostgreSQL

## Configuración del Entorno

1. Clonar el repositorio:  

```bash
git clone <URL_DEL_REPOSITORIO>
cd <NOMBRE_DEL_PROYECTO>
```

2. Construir el proyecto:  

```bash
./gradlew build
```

3. Ejecutar la aplicación:  

```bash
./gradlew bootRun
```

4. Acceder a la aplicación: La aplicación estará disponible 
- Base de datos H2: http://localhost:5252/h2-console
- Swagger UI: http://localhost:8080/swagger-ui.html

## Base de Datos

La base de datos utilizada es H2, una base de datos en memoria que se configura automáticamente al iniciar la aplicación. La consola de H2 está disponible en http://localhost:5252/h2-console.  

- JDBC URL: jdbc:h2:mem:testdb
- User: sa
- Password: (dejar en blanco)

## Endpoints de la API

### Productos

- GET /productos: Listar todos los productos.
- GET /productos/{id}: Obtener un producto por ID.
- POST /productos: Crear un nuevo producto.
- PUT /productos/{id}: Actualizar un producto existente.
- DELETE /productos/{id}: Eliminar un producto por ID.

### Tickets

- GET /tickets: Listar todos los tickets.
- GET /tickets/{id}: Obtener un ticket por ID.
- POST /tickets: Crear un nuevo ticket.
- PUT /tickets/{id}: Actualizar un ticket existente.
- DELETE /tickets/{id}: Eliminar un ticket por ID.

### Transacciones

- GET /transacciones: Listar todas las transacciones.
- GET /transacciones/{id}: Obtener una transacción por ID.
- POST /transacciones: Crear una nueva transacción.
- PUT /transacciones/{id}: Actualizar una transacción existente.
- DELETE /transacciones/{id}: Eliminar una transacción por ID.

## Consideraciones Importantes

- Manejo de Enums: Asegúrese de que los enums se almacenen correctamente en la base de datos utilizando la anotación @Enumerated(EnumType.STRING).
- Transacciones: Utilice la anotación @Transactional para asegurar la consistencia de las operaciones de base de datos.
- Validaciones: Asegúrese de validar los datos de entrada para evitar errores y mantener la integridad de los datos.
- Seguridad: Implemente medidas de seguridad adecuadas para proteger los endpoints, especialmente los que son accesibles solo para administradores.