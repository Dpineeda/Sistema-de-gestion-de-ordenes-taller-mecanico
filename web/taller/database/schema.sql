CREATE DATABASE IF NOT EXISTS db_taller
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE db_taller;

CREATE TABLE IF NOT EXISTS cliente (
    id_cliente VARCHAR(30) NOT NULL,
    nombre VARCHAR(120) NOT NULL,
    telefono VARCHAR(30),
    correo VARCHAR(254),
    PRIMARY KEY (id_cliente)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS mecanico (
    id_mecanico INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(120) NOT NULL,
    telefono VARCHAR(30),
    PRIMARY KEY (id_mecanico)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS vehiculo (
    id_vehiculo INT NOT NULL AUTO_INCREMENT,
    placa VARCHAR(20) NOT NULL,
    marca VARCHAR(80),
    modelo VARCHAR(80),
    anio SMALLINT NOT NULL,
    color VARCHAR(50),
    id_cliente VARCHAR(30) NOT NULL,
    PRIMARY KEY (id_vehiculo),
    UNIQUE KEY uq_vehiculo_placa (placa),
    CONSTRAINT fk_vehiculo_cliente
        FOREIGN KEY (id_cliente) REFERENCES cliente (id_cliente)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS servicio (
    id_servicio INT NOT NULL AUTO_INCREMENT,
    descripcion VARCHAR(255) NOT NULL,
    costo_manodeobra DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (id_servicio),
    CONSTRAINT chk_servicio_costo CHECK (costo_manodeobra >= 0)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS repuesto (
    id_repuesto INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT NOT NULL,
    precio DECIMAL(10, 2) NOT NULL,
    existencia INT NOT NULL DEFAULT 0,
    minimo_en_stock INT NOT NULL DEFAULT 0,
    PRIMARY KEY (id_repuesto),
    CONSTRAINT chk_repuesto_precio CHECK (precio >= 0),
    CONSTRAINT chk_repuesto_existencia CHECK (existencia >= 0),
    CONSTRAINT chk_repuesto_minimo CHECK (minimo_en_stock >= 0)
) ENGINE = InnoDB;

ALTER TABLE repuesto
    MODIFY COLUMN descripcion TEXT NOT NULL;

CREATE TABLE IF NOT EXISTS orden (
    id_orden INT NOT NULL AUTO_INCREMENT,
    id_vehiculo INT NOT NULL,
    id_mecanico INT NOT NULL,
    descripcion_problema TEXT NOT NULL,
    precio_total DECIMAL(12, 2) NOT NULL DEFAULT 0,
    estado VARCHAR(30) NOT NULL DEFAULT 'PENDIENTE',
    PRIMARY KEY (id_orden),
    KEY idx_orden_vehiculo (id_vehiculo),
    KEY idx_orden_mecanico (id_mecanico),
    KEY idx_orden_estado (estado),
    CONSTRAINT fk_orden_vehiculo
        FOREIGN KEY (id_vehiculo) REFERENCES vehiculo (id_vehiculo),
    CONSTRAINT fk_orden_mecanico
        FOREIGN KEY (id_mecanico) REFERENCES mecanico (id_mecanico),
    CONSTRAINT chk_orden_total CHECK (precio_total >= 0)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS orden_servicio (
    id_orden_servicio INT NOT NULL AUTO_INCREMENT,
    id_orden INT NOT NULL,
    id_servicio INT NOT NULL,
    costo DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (id_orden_servicio),
    KEY idx_orden_servicio_orden (id_orden),
    KEY idx_orden_servicio_servicio (id_servicio),
    CONSTRAINT fk_orden_servicio_orden
        FOREIGN KEY (id_orden) REFERENCES orden (id_orden),
    CONSTRAINT fk_orden_servicio_servicio
        FOREIGN KEY (id_servicio) REFERENCES servicio (id_servicio),
    CONSTRAINT chk_orden_servicio_costo CHECK (costo >= 0)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS orden_repuesto (
    id_orden_repuesto INT NOT NULL AUTO_INCREMENT,
    id_orden INT NOT NULL,
    id_repuesto INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unidad DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (id_orden_repuesto),
    KEY idx_orden_repuesto_orden (id_orden),
    KEY idx_orden_repuesto_repuesto (id_repuesto),
    CONSTRAINT fk_orden_repuesto_orden
        FOREIGN KEY (id_orden) REFERENCES orden (id_orden),
    CONSTRAINT fk_orden_repuesto_repuesto
        FOREIGN KEY (id_repuesto) REFERENCES repuesto (id_repuesto),
    CONSTRAINT chk_orden_repuesto_cantidad CHECK (cantidad > 0),
    CONSTRAINT chk_orden_repuesto_precio CHECK (precio_unidad >= 0)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS comprobante (
    id_comprobante INT NOT NULL AUTO_INCREMENT,
    id_orden INT NOT NULL,
    fecha_emision DATETIME NOT NULL,
    total_repuesto DECIMAL(12, 2) NOT NULL,
    total DECIMAL(12, 2) NOT NULL,
    PRIMARY KEY (id_comprobante),
    UNIQUE KEY uq_comprobante_orden (id_orden),
    CONSTRAINT fk_comprobante_orden
        FOREIGN KEY (id_orden) REFERENCES orden (id_orden),
    CONSTRAINT chk_comprobante_total_repuesto CHECK (total_repuesto >= 0),
    CONSTRAINT chk_comprobante_total CHECK (total >= 0)
) ENGINE = InnoDB;