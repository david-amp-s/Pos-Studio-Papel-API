-- Agregar tipo enum en PostgreSQL
CREATE TYPE tipo_producto AS ENUM ('FISICO', 'SERVICIO');

ALTER TABLE producto
    ADD COLUMN tipo_producto tipo_producto NOT NULL DEFAULT 'FISICO',
    ADD COLUMN favorito BOOLEAN NOT NULL DEFAULT FALSE;

    