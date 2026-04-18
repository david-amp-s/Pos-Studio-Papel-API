-- ==========================================
-- V2__refactor_turnos.sql
-- ==========================================

-- =========================
-- ENUMS NUEVOS
-- =========================

CREATE TYPE tipo_turno AS ENUM ('MANANA', 'TARDE');

CREATE TYPE tipo_comentario AS ENUM ('INFO', 'ALERTA', 'TAREA');

CREATE TYPE tipo_log_turno AS ENUM ('DIFERENCIA_CAJA', 'AJUSTE', 'ERROR');

CREATE TYPE tipo_pago AS ENUM ('MANANA', 'TARDE', 'NOCHE', 'COMPLETA');


-- =========================
-- TURNO (ALTER)
-- =========================

ALTER TABLE turno
ADD COLUMN fecha DATE,
ADD COLUMN tipo_turno tipo_turno,
ADD COLUMN diferencia NUMERIC(12,2);

-- migrar fecha desde fecha_apertura
UPDATE turno
SET fecha = DATE(fecha_apertura);

-- hacer NOT NULL después de poblar
ALTER TABLE turno
ALTER COLUMN fecha SET NOT NULL,
ALTER COLUMN tipo_turno SET NOT NULL;

-- unique por día + tipo
ALTER TABLE turno
ADD CONSTRAINT unique_turno_por_dia UNIQUE (fecha, tipo_turno);


-- =========================
-- TURNO_EMPLEADO (ALTER)
-- =========================

ALTER TABLE turno_empleado
RENAME COLUMN hora_inicio TO hora_entrada;

ALTER TABLE turno_empleado
RENAME COLUMN hora_fin TO hora_salida;


ALTER TABLE turno_empleado
ADD CONSTRAINT unique_turno_empleado UNIQUE (turno_id, empleado_id);


-- =========================
-- COMENTARIO_TURNO (ROMPER RELACIONES)
-- =========================

ALTER TABLE comentario_turno
DROP CONSTRAINT IF EXISTS comentario_turno_turno_id_fkey;

ALTER TABLE comentario_turno
DROP CONSTRAINT IF EXISTS comentario_turno_empleado_id_fkey;

ALTER TABLE comentario_turno
DROP COLUMN IF EXISTS turno_id;

ALTER TABLE comentario_turno
DROP COLUMN IF EXISTS empleado_id;

ALTER TABLE comentario_turno
RENAME COLUMN comentario TO mensaje;

ALTER TABLE comentario_turno
ADD COLUMN tipo tipo_comentario DEFAULT 'INFO',
ADD COLUMN creado_por BIGINT;


-- =========================
-- LOG_TURNO (NUEVA)
-- =========================

CREATE TABLE log_turno (
    id BIGSERIAL PRIMARY KEY,
    turno_id BIGINT NOT NULL,
    tipo tipo_log_turno NOT NULL,
    descripcion TEXT,
    monto_diferencia NUMERIC(12,2),
    creado_por BIGINT NOT NULL,
    fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_log_turno
        FOREIGN KEY (turno_id)
        REFERENCES turno(id)
        ON DELETE CASCADE
);


-- =========================
-- REGISTRO_PAGO (NUEVA)
-- =========================

CREATE TABLE registro_pago (
    id BIGSERIAL PRIMARY KEY,
    turno_empleado_id BIGINT NOT NULL,
    tipo_pago tipo_pago NOT NULL,
    monto NUMERIC(12,2) NOT NULL,
    registrado_por BIGINT NOT NULL,
    fecha_registro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_pago_turno_empleado
        FOREIGN KEY (turno_empleado_id)
        REFERENCES turno_empleado(id)
        ON DELETE CASCADE,

    CONSTRAINT unique_pago_por_turno_empleado
        UNIQUE (turno_empleado_id)
);


-- =========================
-- ÍNDICES
-- =========================

CREATE UNIQUE INDEX IF NOT EXISTS unico_turno_abierto
ON turno (estado)
WHERE estado = 'ABIERTO';

CREATE INDEX IF NOT EXISTS idx_turno_fecha ON turno(fecha);
CREATE INDEX IF NOT EXISTS idx_turno_empleado_turno ON turno_empleado(turno_id);
CREATE INDEX IF NOT EXISTS idx_turno_empleado_empleado ON turno_empleado(empleado_id);
CREATE INDEX IF NOT EXISTS idx_pago_turno_empleado ON registro_pago(turno_empleado_id);