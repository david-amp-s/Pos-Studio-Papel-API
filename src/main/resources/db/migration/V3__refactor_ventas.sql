-- v3_mejorado.sql
-- Crear el tipo ENUM para estado de venta
CREATE TYPE estado_venta AS ENUM ('ABIERTA', 'CERRADA');

-- Agregar la columna usando el ENUM
ALTER TABLE venta 
ADD COLUMN estado estado_venta NOT NULL DEFAULT 'ABIERTA';

-- Opcional: Agregar índice para búsquedas rápidas
CREATE INDEX idx_venta_estado ON venta(estado);
CREATE INDEX idx_venta_turno_estado ON venta(turno_id, estado);