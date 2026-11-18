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
id_departamento VARCHAR(10) NOT NULL,
nombre VARCHAR(100) NOT NULL,
CONSTRAINT pk_departamentos PRIMARY KEY (id_departamento),
CONSTRAINT uq_departamentos_nombre UNIQUE (nombre)
) ENGINE=InnoDB;

-- 2. PROFESORES
CREATE TABLE profesores (
id_profesor VARCHAR(10) NOT NULL,
nombre VARCHAR(60) NOT NULL,
apellidos VARCHAR(100) NOT NULL,
email VARCHAR(150) NOT NULL,
id_departamento VARCHAR(10) NOT NULL,
CONSTRAINT pk_profesores PRIMARY KEY (id_profesor),
CONSTRAINT uq_profesores_email UNIQUE (email),
CONSTRAINT fk_profesores_departamento FOREIGN KEY (id_departamento)
    REFERENCES departamentos(id_departamento)
) ENGINE=InnoDB;

-- 3. ESPACIOS
CREATE TABLE espacios (
id_espacio VARCHAR(10) NOT NULL,
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
id_reserva VARCHAR(10) NOT NULL,
fecha DATE NOT NULL,
hora_inicio TIME NOT NULL,
hora_fin TIME NOT NULL,
id_profesor VARCHAR(10) NOT NULL,
id_espacio varchar(10) NOT NULL,
numero_asistentes INT NOT NULL,
estado ENUM('Confirmado', 'Cancelado', 'Finalizado', 'Pendiente') NOT NULL DEFAULT 'Pendiente',
motivo VARCHAR(255) NOT NULL,
CONSTRAINT pk_reservas PRIMARY KEY (id_reserva),
CONSTRAINT fk_reservas_profesor FOREIGN KEY (id_profesor)
  REFERENCES profesores(id_profesor),
CONSTRAINT fk_reservas_espacio FOREIGN KEY (id_espacio)
  REFERENCES espacios(id_espacio),
CONSTRAINT ck_reservas_horario CHECK (hora_fin > hora_inicio),
CONSTRAINT ck_reservas_asistentes CHECK (numero_asistentes > 0),
INDEX idx_reservas_espacio_fecha (id_espacio, fecha, hora_inicio, hora_fin),
INDEX idx_reservas_profesor_fecha (id_profesor, fecha, hora_inicio, hora_fin)
) ENGINE=InnoDB;

-- ============================================================
-- DATOS INICIALES: 10 REGISTROS POR TABLA
-- ============================================================

INSERT INTO departamentos (id_departamento, nombre) VALUES
('D001', 'Informática'),
('D002', 'Matemáticas'),
('D003', 'Lengua Castellana'),
('D004', 'Inglés'),
('D005', 'Geografía e Historia'),
('D006', 'Física y Química'),
('D007', 'Biología y Geología'),
('D008', 'Educación Física'),
('D009', 'Tecnología'),
('D010', 'Orientación');

INSERT INTO profesores (id_profesor, nombre, apellidos, email, id_departamento) VALUES
('P001', 'Ana', 'Martínez López', 'ana.martinez@centro.test', 'D001'),
('P002', 'Luis', 'García Ruiz', 'luis.garcia@centro.test', 'D001'),
('P003', 'Marta', 'Sánchez Pérez', 'marta.sanchez@centro.test', 'D002'),
('P004', 'Pablo', 'Navarro Gil', 'pablo.navarro@centro.test', 'D003'),
('P005', 'Elena', 'Romero Díaz', 'elena.romero@centro.test', 'D004'),
('P006', 'Carlos', 'Ortega Molina', 'carlos.ortega@centro.test', 'D005'),
('P007', 'Lucía', 'Torres Vidal', 'lucia.torres@centro.test', 'D006'),
('P008', 'Javier', 'Moreno Campos', 'javier.moreno@centro.test', 'D007'),
('P009', 'Sara', 'López Serrano', 'sara.lopez@centro.test', 'D008'),
('P010', 'Diego', 'Fernández Ríos', 'diego.fernandez@centro.test', 'D009');

INSERT INTO espacios
(id_espacio, nombre, tipo, ubicacion, capacidad, equipamiento, disponible) VALUES
('E001', 'Aula Informática 1', 'AULA_INFORMATICA', 'Edificio A - planta 1', 30, '30 ordenadores, proyector y pizarra digital', TRUE),
('E002', 'Aula Informática 2', 'AULA_INFORMATICA', 'Edificio A - planta 1', 25, '25 ordenadores y proyector', TRUE),
('E003', 'Aula ATECA', 'AULA_TECNOLOGICA', 'Edificio A - planta 2', 24, 'Equipos VR, impresora 3D y ordenadores', TRUE),
('E004', 'Biblioteca', 'BIBLIOTECA', 'Edificio B - planta baja', 60, 'Mesas, proyector y ordenadores de consulta', TRUE),
('E005', 'Salón de Actos', 'SALON_ACTOS', 'Edificio B - planta baja', 120, 'Escenario, sonido y proyector', TRUE),
('E006', 'Laboratorio de Física', 'LABORATORIO', 'Edificio C - planta 1', 24, 'Material de laboratorio y pizarra digital', TRUE),
('E007', 'Laboratorio de Biología', 'LABORATORIO', 'Edificio C - planta 1', 24, 'Microscopios y material de laboratorio', TRUE),
('E008', 'Gimnasio', 'INSTALACION_DEPORTIVA', 'Pabellón deportivo', 50, 'Material deportivo y equipo de sonido', TRUE),
('E009', 'Aula Polivalente', 'AULA', 'Edificio A - planta baja', 35, 'Pizarra digital y proyector', TRUE),
('E010', 'Sala de Reuniones', 'SALA_REUNIONES', 'Edificio B - planta 1', 15, 'Pantalla y sistema de videoconferencia', FALSE);

-- Las reservas activas no se solapan ni para espacios ni para profesores.
-- La reserva 10 esta cancelada y sirve para probar la funcion almacenada.
INSERT INTO reservas
(id_reserva, fecha, hora_inicio, hora_fin, id_profesor, id_espacio,
 numero_asistentes, estado, motivo) VALUES
('R001', '2026-11-19', '08:15:00', '09:10:00', 'P001', 'E001', 25, 'Confirmado', 'Práctica de programación Java'),
('R002', '2026-11-19', '09:10:00', '10:05:00', 'P001', 'E001', 25, 'Confirmado', 'Continuación práctica de programación'),
('R003', '2026-11-19', '10:05:00', '11:00:00', 'P002', 'E002', 20, 'Pendiente', 'Práctica de bases de datos'),
('R004', '2026-11-20', '08:15:00', '09:10:00', 'P003', 'E009', 28, 'Confirmado', 'Taller de matemáticas'),
('R005', '2026-11-20', '09:10:00', '10:05:00', 'P004', 'E004', 30, 'Confirmado', 'Club de lectura'),
('R006', '2026-11-21', '10:05:00', '11:00:00', 'P005', 'E003', 20, 'Confirmado', 'Actividad de inglés con realidad virtual'),
('R007', '2026-11-21', '11:30:00', '12:25:00', 'P006', 'E005', 60, 'Pendiente', 'Conferencia sobre historia contemporánea'),
('R008', '2026-11-22', '08:15:00', '09:10:00', 'P007', 'E006', 20, 'Confirmado', 'Experimento de física'),
('R009', '2026-11-22', '09:10:00', '10:05:00', 'P008', 'E007', 22, 'Confirmado', 'Observación de muestras biológicas'),
('R010', '2026-11-23', '12:25:00', '13:20:00', 'P009', 'E008', 40, 'Cancelado', 'Jornada de actividades deportivas');

-- ============================================================
-- FUNCION ALMACENADA PARA CALLABLESTATEMENT
-- Cuenta reservas NO CANCELADAS de un profesor.
-- ============================================================
DELIMITER $$
CREATE FUNCTION IF NOT EXISTS fn_contar_reservas_profesor(p_id_profesor varchar(10))
    RETURNS INT
    NOT DETERMINISTIC
    READS SQL DATA
BEGIN
    DECLARE v_total INT DEFAULT 0;
    SELECT COUNT(*) INTO v_total
    FROM reservas
    WHERE id_profesor = p_id_profesor
      AND estado <> 'Cancelado';
    RETURN v_total;
END$$
DELIMITER ;
