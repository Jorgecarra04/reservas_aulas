-- ============================================
-- SCRIPT SQL COMPLETO PARA RESERVA DE AULAS
-- Compatible con MariaDB / MySQL
-- ============================================

-- 1. CREAR LA BASE DE DATOS
DROP DATABASE IF EXISTS reservas_aulas;
CREATE DATABASE reservas_aulas CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE reservas_aulas;

-- ============================================
-- 2. CREAR LAS TABLAS
-- ============================================

-- Tabla USUARIOS
CREATE TABLE usuarios (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          nombre VARCHAR(100) NOT NULL,
                          email VARCHAR(150) NOT NULL UNIQUE,
                          password VARCHAR(255) NOT NULL,
                          role VARCHAR(50) NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          INDEX idx_email (email)
) ENGINE=InnoDB;

-- Tabla AULAS
CREATE TABLE aulas (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       nombre VARCHAR(50) NOT NULL UNIQUE,
                       capacidad INT NOT NULL,
                       es_aula_de_ordenadores BOOLEAN NOT NULL DEFAULT FALSE,
                       numero_ordenadores INT DEFAULT 0,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       INDEX idx_nombre (nombre),
                       INDEX idx_capacidad (capacidad),
                       INDEX idx_ordenadores (es_aula_de_ordenadores)
) ENGINE=InnoDB;

-- Tabla TRAMOS HORARIOS
CREATE TABLE tramos_horarios (
                                 id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                 dia_semana VARCHAR(20) NOT NULL,
                                 sesion_dia INT NOT NULL,
                                 hora_inicio TIME NOT NULL,
                                 hora_fin TIME NOT NULL,
                                 tipo VARCHAR(20) NOT NULL,
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                 INDEX idx_dia_sesion (dia_semana, sesion_dia),
                                 CONSTRAINT chk_dia_semana CHECK (dia_semana IN ('LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES')),
                                 CONSTRAINT chk_tipo CHECK (tipo IN ('LECTIVA', 'RECREO', 'MEDIODIA'))
) ENGINE=InnoDB;

-- Tabla RESERVAS
CREATE TABLE reservas (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          fecha DATE NOT NULL,
                          motivo VARCHAR(500) NOT NULL,
                          numero_asistentes INT NOT NULL,
                          fecha_creacion DATE NOT NULL,
                          aula_id BIGINT NOT NULL,
                          tramo_horario_id BIGINT NOT NULL,
                          usuario_id BIGINT NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                          FOREIGN KEY (aula_id) REFERENCES aulas(id) ON DELETE CASCADE,
                          FOREIGN KEY (tramo_horario_id) REFERENCES tramos_horarios(id) ON DELETE CASCADE,
                          FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,

                          INDEX idx_fecha (fecha),
                          INDEX idx_aula (aula_id),
                          INDEX idx_tramo (tramo_horario_id),
                          INDEX idx_usuario (usuario_id),
                          INDEX idx_fecha_aula_tramo (fecha, aula_id, tramo_horario_id)
) ENGINE=InnoDB;

-- ============================================
-- 3. INSERTAR DATOS INICIALES
-- ============================================

-- AULAS (12 aulas variadas)
INSERT INTO aulas (nombre, capacidad, es_aula_de_ordenadores, numero_ordenadores) VALUES
                                                                                      ('Aula 101', 30, FALSE, 0),
                                                                                      ('Aula 102', 28, FALSE, 0),
                                                                                      ('Aula 103', 25, FALSE, 0),
                                                                                      ('Aula 201', 35, FALSE, 0),
                                                                                      ('Aula 202', 32, FALSE, 0),
                                                                                      ('Aula Informática 1', 24, TRUE, 24),
                                                                                      ('Aula Informática 2', 20, TRUE, 20),
                                                                                      ('Aula Informática 3', 22, TRUE, 22),
                                                                                      ('Laboratorio Química', 18, FALSE, 0),
                                                                                      ('Laboratorio Física', 20, FALSE, 0),
                                                                                      ('Salón de Actos', 200, FALSE, 0),
                                                                                      ('Sala de Reuniones', 15, FALSE, 0);

-- TRAMOS HORARIOS - LUNES
INSERT INTO tramos_horarios (dia_semana, sesion_dia, hora_inicio, hora_fin, tipo) VALUES
-- Lunes
('LUNES', 1, '08:00:00', '09:00:00', 'LECTIVA'),
('LUNES', 2, '09:00:00', '10:00:00', 'LECTIVA'),
('LUNES', 3, '10:00:00', '11:00:00', 'LECTIVA'),
('LUNES', 4, '11:00:00', '11:30:00', 'RECREO'),
('LUNES', 5, '11:30:00', '12:30:00', 'LECTIVA'),
('LUNES', 6, '12:30:00', '13:30:00', 'LECTIVA'),
('LUNES', 7, '13:30:00', '15:00:00', 'MEDIODIA'),
('LUNES', 8, '15:00:00', '16:00:00', 'LECTIVA'),
('LUNES', 9, '16:00:00', '17:00:00', 'LECTIVA'),

-- Martes
('MARTES', 1, '08:00:00', '09:00:00', 'LECTIVA'),
('MARTES', 2, '09:00:00', '10:00:00', 'LECTIVA'),
('MARTES', 3, '10:00:00', '11:00:00', 'LECTIVA'),
('MARTES', 4, '11:00:00', '11:30:00', 'RECREO'),
('MARTES', 5, '11:30:00', '12:30:00', 'LECTIVA'),
('MARTES', 6, '12:30:00', '13:30:00', 'LECTIVA'),
('MARTES', 7, '13:30:00', '15:00:00', 'MEDIODIA'),
('MARTES', 8, '15:00:00', '16:00:00', 'LECTIVA'),
('MARTES', 9, '16:00:00', '17:00:00', 'LECTIVA'),

-- Miércoles
('MIERCOLES', 1, '08:00:00', '09:00:00', 'LECTIVA'),
('MIERCOLES', 2, '09:00:00', '10:00:00', 'LECTIVA'),
('MIERCOLES', 3, '10:00:00', '11:00:00', 'LECTIVA'),
('MIERCOLES', 4, '11:00:00', '11:30:00', 'RECREO'),
('MIERCOLES', 5, '11:30:00', '12:30:00', 'LECTIVA'),
('MIERCOLES', 6, '12:30:00', '13:30:00', 'LECTIVA'),
('MIERCOLES', 7, '13:30:00', '15:00:00', 'MEDIODIA'),
('MIERCOLES', 8, '15:00:00', '16:00:00', 'LECTIVA'),
('MIERCOLES', 9, '16:00:00', '17:00:00', 'LECTIVA'),

-- Jueves
('JUEVES', 1, '08:00:00', '09:00:00', 'LECTIVA'),
('JUEVES', 2, '09:00:00', '10:00:00', 'LECTIVA'),
('JUEVES', 3, '10:00:00', '11:00:00', 'LECTIVA'),
('JUEVES', 4, '11:00:00', '11:30:00', 'RECREO'),
('JUEVES', 5, '11:30:00', '12:30:00', 'LECTIVA'),
('JUEVES', 6, '12:30:00', '13:30:00', 'LECTIVA'),
('JUEVES', 7, '13:30:00', '15:00:00', 'MEDIODIA'),
('JUEVES', 8, '15:00:00', '16:00:00', 'LECTIVA'),
('JUEVES', 9, '16:00:00', '17:00:00', 'LECTIVA'),

-- Viernes
('VIERNES', 1, '08:00:00', '09:00:00', 'LECTIVA'),
('VIERNES', 2, '09:00:00', '10:00:00', 'LECTIVA'),
('VIERNES', 3, '10:00:00', '11:00:00', 'LECTIVA'),
('VIERNES', 4, '11:00:00', '11:30:00', 'RECREO'),
('VIERNES', 5, '11:30:00', '12:30:00', 'LECTIVA'),
('VIERNES', 6, '12:30:00', '13:30:00', 'LECTIVA');

-- ============================================
-- 4. CREAR USUARIO PARA LA APLICACIÓN
-- ============================================

-- Usuario con permisos específicos para la aplicación
CREATE USER IF NOT EXISTS 'reservas_app'@'%' IDENTIFIED BY 'ReservasApp2024!';
GRANT SELECT, INSERT, UPDATE, DELETE ON reservas_aulas.* TO 'reservas_app'@'%';
FLUSH PRIVILEGES;
