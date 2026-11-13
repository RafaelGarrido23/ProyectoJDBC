-- ============================================================
-- PROYECTO UT2 / RA2 - GESTOR DE RESERVAS DE ESPACIOS
-- MariaDB - Base de datos de ejemplo para Java / JDBC
-- ============================================================
-- ATENCION: este script ELIMINA la base de datos si ya existe.
-- Ejecutar en un entorno de practicas, no de produccion.
-- Contiene 10 registros iniciales en CADA una de las 4 tablas.
-- ============================================================

DROP DATABASE IF EXISTS gestion_reservas_centro;
CREATE DATABASE gestion_reservas_centro
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
USE gestion_reservas_centro;

-- 1. DEPARTAMENTOS
CREATE TABLE departamentos (
    id_departamento INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    CONSTRAINT pk_departamentos PRIMARY KEY (id_departamento),
    CONSTRAINT uq_departamentos_nombre UNIQUE (nombre)
) ENGINE=InnoDB;

-- 2. PROFESORES
CREATE TABLE profesores (
    id_profesor INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(60) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    id_departamento INT NOT NULL,
    CONSTRAINT pk_profesores PRIMARY KEY (id_profesor),
    CONSTRAINT uq_profesores_email UNIQUE (email),
    CONSTRAINT fk_profesores_departamento FOREIGN KEY (id_departamento)
        REFERENCES departamentos(id_departamento)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

-- 3. ESPACIOS
CREATE TABLE espacios (
    id_espacio INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    ubicacion VARCHAR(120) NOT NULL,
    capacidad INT NOT NULL,
    equipamiento TEXT,
    disponible BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT pk_espacios PRIMARY KEY (id_espacio),
    CONSTRAINT uq_espacios_nombre UNIQUE (nombre),
    CONSTRAINT ck_espacios_capacidad CHECK (capacidad > 0)
) ENGINE=InnoDB;

-- 4. RESERVAS
-- Estados de ejemplo: PENDIENTE, CONFIRMADA y CANCELADA.
-- En una cancelacion se recomienda UPDATE estado='CANCELADA'
-- para conservar el historial, no necesariamente DELETE.
CREATE TABLE reservas (
    id_reserva INT NOT NULL AUTO_INCREMENT,
    fecha DATE NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    id_profesor INT NOT NULL,
    id_espacio INT NOT NULL,
    numero_asistentes INT NOT NULL,
    estado ENUM('Confirmado', 'Cancelado', 'Finalizado') NOT NULL DEFAULT 'PENDIENTE',
    motivo VARCHAR(255) NOT NULL,
    CONSTRAINT pk_reservas PRIMARY KEY (id_reserva),
    CONSTRAINT fk_reservas_profesor FOREIGN KEY (id_profesor)
        REFERENCES profesores(id_profesor)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_reservas_espacio FOREIGN KEY (id_espacio)
        REFERENCES espacios(id_espacio)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT ck_reservas_horario CHECK (hora_fin > hora_inicio),
    CONSTRAINT ck_reservas_asistentes CHECK (numero_asistentes > 0),
    INDEX idx_reservas_espacio_fecha (id_espacio, fecha, hora_inicio, hora_fin),
    INDEX idx_reservas_profesor_fecha (id_profesor, fecha, hora_inicio, hora_fin)
) ENGINE=InnoDB;

-- ============================================================
-- DATOS INICIALES: 10 REGISTROS POR TABLA
-- ============================================================

INSERT INTO departamentos (id_departamento, nombre) VALUES
(1, 'Informática'),
(2, 'Matemáticas'),
(3, 'Lengua Castellana'),
(4, 'Inglés'),
(5, 'Geografía e Historia'),
(6, 'Física y Química'),
(7, 'Biología y Geología'),
(8, 'Educación Física'),
(9, 'Tecnología'),
(10, 'Orientación');

INSERT INTO profesores (id_profesor, nombre, apellidos, email, id_departamento) VALUES
(1, 'Ana', 'Martínez López', 'ana.martinez@centro.test', 1),
(2, 'Luis', 'García Ruiz', 'luis.garcia@centro.test', 1),
(3, 'Marta', 'Sánchez Pérez', 'marta.sanchez@centro.test', 2),
(4, 'Pablo', 'Navarro Gil', 'pablo.navarro@centro.test', 3),
(5, 'Elena', 'Romero Díaz', 'elena.romero@centro.test', 4),
(6, 'Carlos', 'Ortega Molina', 'carlos.ortega@centro.test', 5),
(7, 'Lucía', 'Torres Vidal', 'lucia.torres@centro.test', 6),
(8, 'Javier', 'Moreno Campos', 'javier.moreno@centro.test', 7),
(9, 'Sara', 'López Serrano', 'sara.lopez@centro.test', 8),
(10, 'Diego', 'Fernández Ríos', 'diego.fernandez@centro.test', 9);

INSERT INTO espacios
(id_espacio, nombre, tipo, ubicacion, capacidad, equipamiento, disponible) VALUES
(1, 'Aula Informática 1', 'AULA_INFORMATICA', 'Edificio A - planta 1', 30, '30 ordenadores, proyector y pizarra digital', TRUE),
(2, 'Aula Informática 2', 'AULA_INFORMATICA', 'Edificio A - planta 1', 25, '25 ordenadores y proyector', TRUE),
(3, 'Aula ATECA', 'AULA_TECNOLOGICA', 'Edificio A - planta 2', 24, 'Equipos VR, impresora 3D y ordenadores', TRUE),
(4, 'Biblioteca', 'BIBLIOTECA', 'Edificio B - planta baja', 60, 'Mesas, proyector y ordenadores de consulta', TRUE),
(5, 'Salón de Actos', 'SALON_ACTOS', 'Edificio B - planta baja', 120, 'Escenario, sonido y proyector', TRUE),
(6, 'Laboratorio de Física', 'LABORATORIO', 'Edificio C - planta 1', 24, 'Material de laboratorio y pizarra digital', TRUE),
(7, 'Laboratorio de Biología', 'LABORATORIO', 'Edificio C - planta 1', 24, 'Microscopios y material de laboratorio', TRUE),
(8, 'Gimnasio', 'INSTALACION_DEPORTIVA', 'Pabellón deportivo', 50, 'Material deportivo y equipo de sonido', TRUE),
(9, 'Aula Polivalente', 'AULA', 'Edificio A - planta baja', 35, 'Pizarra digital y proyector', TRUE),
(10, 'Sala de Reuniones', 'SALA_REUNIONES', 'Edificio B - planta 1', 15, 'Pantalla y sistema de videoconferencia', FALSE);

-- Las reservas activas no se solapan ni para espacios ni para profesores.
-- La reserva 10 esta cancelada y sirve para probar la funcion almacenada.
INSERT INTO reservas
(id_reserva, fecha, hora_inicio, hora_fin, id_profesor, id_espacio,
 numero_asistentes, estado, motivo) VALUES
(1, '2026-11-19', '08:15:00', '09:10:00', 1, 1, 25, 'Confirmado', 'Práctica de programación Java'),
(2, '2026-11-19', '09:10:00', '10:05:00', 1, 1, 25, 'Confirmado', 'Continuación práctica de programación'),
(3, '2026-11-19', '10:05:00', '11:00:00', 2, 2, 20, 'Pendiente', 'Práctica de bases de datos'),
(4, '2026-11-20', '08:15:00', '09:10:00', 3, 9, 28, 'Confirmado', 'Taller de matemáticas'),
(5, '2026-11-20', '09:10:00', '10:05:00', 4, 4, 30, 'Confirmado', 'Club de lectura'),
(6, '2026-11-21', '10:05:00', '11:00:00', 5, 3, 20, 'Confirmado', 'Actividad de inglés con realidad virtual'),
(7, '2026-11-21', '11:30:00', '12:25:00', 6, 5, 60, 'Pendiente', 'Conferencia sobre historia contemporánea'),
(8, '2026-11-22', '08:15:00', '09:10:00', 7, 6, 20, 'Confirmado', 'Experimento de física'),
(9, '2026-11-22', '09:10:00', '10:05:00', 8, 7, 22, 'Confirmado', 'Observación de muestras biológicas'),
(10, '2026-11-23', '12:25:00', '13:20:00', 9, 8, 40, 'Cancelado', 'Jornada de actividades deportivas');

-- ============================================================
-- FUNCION ALMACENADA PARA CALLABLESTATEMENT
-- Cuenta reservas NO CANCELADAS de un profesor.
-- ============================================================
DELIMITER $$
CREATE FUNCTION fn_contar_reservas_profesor(p_id_profesor INT)
RETURNS INT
NOT DETERMINISTIC
READS SQL DATA
BEGIN
    DECLARE v_total INT DEFAULT 0;
    SELECT COUNT(*) INTO v_total
    FROM reservas
    WHERE id_profesor = p_id_profesor
      AND estado <> 'CANCELADA';
    RETURN v_total;
END$$
DELIMITER ;
