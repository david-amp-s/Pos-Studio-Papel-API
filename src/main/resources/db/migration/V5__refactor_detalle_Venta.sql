-- Migración V5: Refactor detalle_venta para PostgreSQL

-- 1. Hacer producto_id nullable (quitar NOT NULL si existe)
ALTER TABLE detalle_venta 
    ALTER COLUMN producto_id DROP NOT NULL;

-- 2. Agregar columna producto_pendiente_id y su FK
ALTER TABLE detalle_venta 
    ADD COLUMN producto_pendiente_id BIGINT;

ALTER TABLE detalle_venta 
    ADD CONSTRAINT fk_detalle_producto_pendiente 
        FOREIGN KEY (producto_pendiente_id) 
        REFERENCES producto_pendiente(id);

-- 3. Check constraint: exactamente uno debe estar presente (producto_id o producto_pendiente_id)
ALTER TABLE detalle_venta 
    ADD CONSTRAINT chk_producto_exclusivo 
        CHECK (
            (producto_id IS NOT NULL AND producto_pendiente_id IS NULL) OR 
            (producto_id IS NULL AND producto_pendiente_id IS NOT NULL)
        );