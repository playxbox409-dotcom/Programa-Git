CREATE DATABASE IF NOT EXISTS gana_app
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE gana_app;

CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(150) NOT NULL UNIQUE,
    creado_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS animales (
    id_animal BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_propietario BIGINT UNSIGNED NOT NULL,
    codigo VARCHAR(30) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    especie VARCHAR(30) NOT NULL,
    raza VARCHAR(80) NOT NULL,
    fecha_nacimiento DATE NULL,
    peso_kg DECIMAL(10, 2) NOT NULL,
    precio_cop DECIMAL(14, 2) NOT NULL,
    ubicacion VARCHAR(120) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'DISPONIBLE',
    creado_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizado_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_animales_propietario
        FOREIGN KEY (id_propietario) REFERENCES usuarios (id_usuario),
    CONSTRAINT chk_animales_peso CHECK (peso_kg > 0),
    CONSTRAINT chk_animales_precio CHECK (precio_cop >= 0),
    CONSTRAINT chk_animales_estado
        CHECK (estado IN ('DISPONIBLE', 'RESERVADO', 'VENDIDO', 'INACTIVO'))
) ENGINE = InnoDB;

INSERT INTO usuarios (nombre, correo)
VALUES ('Usuario demostración', 'demo@ganaapp.local')
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);
