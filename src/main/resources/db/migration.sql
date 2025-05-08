-- Script para migración de datos de H2 a MySQL
-- Este script es una guía básica; ajústalo según tu esquema específico

-- Creación de la base de datos si no existe
CREATE DATABASE IF NOT EXISTS saborcito_db;
USE saborcito_db;

-- Creación de tablas

-- Categoría
CREATE TABLE IF NOT EXISTS categoria (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT
);

-- Valor
CREATE TABLE IF NOT EXISTS valor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    valor DECIMAL(10,2) NOT NULL
);

-- Producto
CREATE TABLE IF NOT EXISTS producto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10,2) NOT NULL,
    imagen_url VARCHAR(255),
    stock INT NOT NULL DEFAULT 0,
    categoria_id BIGINT,
    FOREIGN KEY (categoria_id) REFERENCES categoria(id)
);

-- Usuario
CREATE TABLE IF NOT EXISTS usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    apellido VARCHAR(255),
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    telefono VARCHAR(20),
    direccion TEXT,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Admin
CREATE TABLE IF NOT EXISTS admin (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    apellido VARCHAR(255),
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- Ticket
CREATE TABLE IF NOT EXISTS ticket (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10,2) NOT NULL,
    usuario_id BIGINT,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

-- TicketProducto (tabla de relación)
CREATE TABLE IF NOT EXISTS ticket_producto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ticket_id BIGINT,
    producto_id BIGINT,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (ticket_id) REFERENCES ticket(id),
    FOREIGN KEY (producto_id) REFERENCES producto(id)
);

-- Transaccion
CREATE TABLE IF NOT EXISTS transaccion (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    monto DECIMAL(10,2) NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    descripcion TEXT,
    ticket_id BIGINT,
    FOREIGN KEY (ticket_id) REFERENCES ticket(id)
);

-- NOTA: Para migrar datos desde H2, necesitarás exportar los datos
-- de H2 e importarlos a MySQL usando herramientas como SQL Workbench
-- o scripts personalizados.