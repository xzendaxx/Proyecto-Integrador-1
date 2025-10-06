CREATE DATABASE IF NOT EXISTS agro_inspecciones CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE agro_inspecciones;

CREATE TABLE IF NOT EXISTS productores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    identificacion VARCHAR(30) NOT NULL UNIQUE,
    nombre VARCHAR(150) NOT NULL,
    telefono VARCHAR(30),
    correo VARCHAR(120)
);

CREATE TABLE IF NOT EXISTS propietarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    identificacion VARCHAR(30) NOT NULL UNIQUE,
    nombre VARCHAR(150) NOT NULL,
    telefono VARCHAR(30),
    correo VARCHAR(120)
);

CREATE TABLE IF NOT EXISTS asistentes_tecnicos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    identificacion VARCHAR(30) NOT NULL UNIQUE,
    nombre VARCHAR(150) NOT NULL,
    telefono VARCHAR(30),
    correo VARCHAR(120),
    tarjeta_profesional VARCHAR(60) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS lugares_produccion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    numero_registro_ica VARCHAR(60) NOT NULL UNIQUE,
    productor_id INT NOT NULL,
    FOREIGN KEY (productor_id) REFERENCES productores(id)
);

CREATE TABLE IF NOT EXISTS predios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    departamento VARCHAR(100) NOT NULL,
    municipio VARCHAR(100) NOT NULL,
    vereda VARCHAR(100) NOT NULL,
    area_hectareas DECIMAL(10,2) NOT NULL,
    propietario_id INT NOT NULL,
    lugar_produccion_id INT NOT NULL,
    FOREIGN KEY (propietario_id) REFERENCES propietarios(id),
    FOREIGN KEY (lugar_produccion_id) REFERENCES lugares_produccion(id)
);

CREATE TABLE IF NOT EXISTS cultivos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    especie VARCHAR(100) NOT NULL,
    variedad VARCHAR(100),
    fecha_siembra DATE,
    predio_id INT NOT NULL,
    FOREIGN KEY (predio_id) REFERENCES predios(id)
);

CREATE TABLE IF NOT EXISTS lotes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numero VARCHAR(50) NOT NULL,
    descripcion TEXT,
    area DECIMAL(10,2),
    cultivo_id INT NOT NULL,
    FOREIGN KEY (cultivo_id) REFERENCES cultivos(id)
);

CREATE TABLE IF NOT EXISTS plagas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_comun VARCHAR(120) NOT NULL,
    nombre_cientifico VARCHAR(120)
);

CREATE TABLE IF NOT EXISTS informes_fitosanitarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    lote_id INT NOT NULL,
    plaga_id INT NOT NULL,
    asistente_id INT NOT NULL,
    fecha DATE NOT NULL,
    plantas_totales INT NOT NULL,
    plantas_afectadas INT NOT NULL,
    porcentaje_incidente DECIMAL(5,2) NOT NULL,
    observaciones TEXT,
    FOREIGN KEY (lote_id) REFERENCES lotes(id),
    FOREIGN KEY (plaga_id) REFERENCES plagas(id),
    FOREIGN KEY (asistente_id) REFERENCES asistentes_tecnicos(id)
);

CREATE TABLE IF NOT EXISTS informes_produccion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    lote_id INT NOT NULL,
    fecha DATE NOT NULL,
    cantidad_cosechada DECIMAL(10,2) NOT NULL,
    observaciones TEXT,
    FOREIGN KEY (lote_id) REFERENCES lotes(id)
);
