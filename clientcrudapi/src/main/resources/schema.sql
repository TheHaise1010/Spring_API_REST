-- src/main/resources/schema.sql
-- NOTA: La base de datos 'banco_db' debe ser creada manualmente en MySQL antes de ejecutar la aplicación.

-- Creamos la tabla cliente si no existe
CREATE TABLE IF NOT EXISTS cliente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dui VARCHAR(10) NOT NULL UNIQUE,
    primer_nombre VARCHAR(12) NOT NULL,
    apellido VARCHAR(12) NOT NULL,
    fecha_nacimiento DATE NOT NULL
    );