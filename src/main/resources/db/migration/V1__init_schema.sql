-- ==========================================
-- POS STUDIO PAPEL
-- BASE DE DATOS
-- ==========================================


-- ==========================================
-- ENUMS
-- ==========================================

CREATE TYPE unidad_negocio AS ENUM (
'PAPELERIA',
'VITRINA',
'PIERCING'
);

CREATE TYPE metodo_pago AS ENUM (
'EFECTIVO',
'TARJETA',
'TRANSFERENCIA'
);

CREATE TYPE estado_turno AS ENUM (
'ABIERTO',
'CERRADO'
);

CREATE TYPE tipo_movimiento_inventario AS ENUM (
'VENTA',
'COMPRA',
'DEVOLUCION',
'AJUSTE',
'CONSUMO_INTERNO'
);


-- ==========================================
-- USUARIOS
-- ==========================================

CREATE TABLE usuario (
id BIGSERIAL PRIMARY KEY,
nombre VARCHAR(100) NOT NULL,
usuario VARCHAR(50) UNIQUE NOT NULL,
contrasena TEXT NOT NULL,
rol VARCHAR(20) NOT NULL,
activo BOOLEAN DEFAULT TRUE,
fecha_creacion TIMESTAMP DEFAULT NOW()
);


-- ==========================================
-- EMPLEADOS
-- ==========================================

CREATE TABLE empleado (
id BIGSERIAL PRIMARY KEY,
nombre VARCHAR(100) NOT NULL,
activo BOOLEAN DEFAULT TRUE,
fecha_creacion TIMESTAMP DEFAULT NOW()
);


-- ==========================================
-- CATEGORIAS
-- ==========================================

CREATE TABLE categoria (
id BIGSERIAL PRIMARY KEY,
nombre VARCHAR(100) NOT NULL,
fecha_creacion TIMESTAMP DEFAULT NOW()
);


-- ==========================================
-- UBICACION PRODUCTOS
-- ==========================================

CREATE TABLE ubicacion_producto (
id BIGSERIAL PRIMARY KEY,
codigo VARCHAR(20) UNIQUE NOT NULL,
descripcion TEXT
);


-- ==========================================
-- PRODUCTOS
-- ==========================================

CREATE TABLE producto (
id BIGSERIAL PRIMARY KEY,
nombre VARCHAR(200) NOT NULL,
codigo_barras VARCHAR(100) UNIQUE,
precio NUMERIC(10,2) NOT NULL,
stock INTEGER NOT NULL DEFAULT 0,
categoria_id BIGINT,
ubicacion_id BIGINT,
unidad_negocio unidad_negocio NOT NULL,
activo BOOLEAN DEFAULT TRUE,
fecha_creacion TIMESTAMP DEFAULT NOW(),
fecha_actualizacion TIMESTAMP,
FOREIGN KEY (categoria_id) REFERENCES categoria(id),
FOREIGN KEY (ubicacion_id) REFERENCES ubicacion_producto(id)
);


-- ==========================================
-- PROVEEDORES
-- ==========================================

CREATE TABLE proveedor (
id BIGSERIAL PRIMARY KEY,
nombre VARCHAR(200) NOT NULL,
telefono VARCHAR(50),
fecha_creacion TIMESTAMP DEFAULT NOW()
);


-- ==========================================
-- COMPRAS
-- ==========================================

CREATE TABLE compra (
id BIGSERIAL PRIMARY KEY,
proveedor_id BIGINT,
fecha TIMESTAMP DEFAULT NOW(),
total NUMERIC(10,2),
descripcion TEXT,
FOREIGN KEY (proveedor_id) REFERENCES proveedor(id)
);


-- ==========================================
-- DETALLE COMPRA
-- ==========================================

CREATE TABLE detalle_compra (
id BIGSERIAL PRIMARY KEY,
compra_id BIGINT NOT NULL,
producto_id BIGINT NOT NULL,
cantidad INTEGER NOT NULL,
precio_unitario NUMERIC(10,2),
subtotal NUMERIC(10,2),
FOREIGN KEY (compra_id) REFERENCES compra(id),
FOREIGN KEY (producto_id) REFERENCES producto(id)
);


-- ==========================================
-- TURNOS
-- ==========================================

CREATE TABLE turno (
id BIGSERIAL PRIMARY KEY,
estado estado_turno DEFAULT 'ABIERTO',
fecha_apertura TIMESTAMP DEFAULT NOW(),
fecha_cierre TIMESTAMP,
dinero_apertura NUMERIC(10,2) DEFAULT 0,
dinero_cierre NUMERIC(10,2)
);


-- ==========================================
-- EMPLEADOS EN TURNO
-- ==========================================

CREATE TABLE turno_empleado (
id BIGSERIAL PRIMARY KEY,
turno_id BIGINT NOT NULL,
empleado_id BIGINT NOT NULL,
hora_inicio TIMESTAMP NOT NULL,
hora_fin TIMESTAMP,
FOREIGN KEY (turno_id) REFERENCES turno(id),
FOREIGN KEY (empleado_id) REFERENCES empleado(id)
);


-- ==========================================
-- COMENTARIOS TURNOS
-- ==========================================

CREATE TABLE comentario_turno (
id BIGSERIAL PRIMARY KEY,
turno_id BIGINT,
empleado_id BIGINT,
comentario TEXT NOT NULL,
fecha TIMESTAMP DEFAULT NOW(),
FOREIGN KEY (turno_id) REFERENCES turno(id),
FOREIGN KEY (empleado_id) REFERENCES empleado(id)
);


-- ==========================================
-- VENTAS
-- ==========================================

CREATE TABLE venta (
id BIGSERIAL PRIMARY KEY,
usuario_id BIGINT,
turno_id BIGINT,
total NUMERIC(10,2) NOT NULL,
descuento NUMERIC(10,2) DEFAULT 0,
fecha TIMESTAMP DEFAULT NOW(),
FOREIGN KEY (usuario_id) REFERENCES usuario(id),
FOREIGN KEY (turno_id) REFERENCES turno(id)
);


-- ==========================================
-- DETALLE VENTA
-- ==========================================

CREATE TABLE detalle_venta (
id BIGSERIAL PRIMARY KEY,
venta_id BIGINT NOT NULL,
producto_id BIGINT NOT NULL,
cantidad INTEGER NOT NULL,
precio_unitario NUMERIC(10,2) NOT NULL,
subtotal NUMERIC(10,2) NOT NULL,
FOREIGN KEY (venta_id) REFERENCES venta(id),
FOREIGN KEY (producto_id) REFERENCES producto(id)
);


-- ==========================================
-- PAGOS DE VENTA
-- ==========================================

CREATE TABLE pago_venta (
id BIGSERIAL PRIMARY KEY,
venta_id BIGINT NOT NULL,
metodo metodo_pago NOT NULL,
monto NUMERIC(10,2) NOT NULL,
FOREIGN KEY (venta_id) REFERENCES venta(id)
);


-- ==========================================
-- FACTURAS (OPCIONAL)
-- ==========================================

CREATE TABLE factura (
id BIGSERIAL PRIMARY KEY,
venta_id BIGINT UNIQUE NOT NULL,
numero_factura VARCHAR(50) UNIQUE NOT NULL,
cliente_nombre VARCHAR(200),
cliente_documento VARCHAR(50),
cliente_direccion TEXT,
fecha TIMESTAMP DEFAULT NOW(),
total NUMERIC(10,2) NOT NULL,
FOREIGN KEY (venta_id) REFERENCES venta(id)
);


-- ==========================================
-- MOVIMIENTO INVENTARIO
-- ==========================================

CREATE TABLE movimiento_inventario (
id BIGSERIAL PRIMARY KEY,
producto_id BIGINT NOT NULL,
tipo tipo_movimiento_inventario NOT NULL,
cantidad INTEGER NOT NULL,
referencia_id BIGINT,
descripcion TEXT,
fecha TIMESTAMP DEFAULT NOW(),
FOREIGN KEY (producto_id) REFERENCES producto(id)
);


-- ==========================================
-- CLIENTES
-- ==========================================

CREATE TABLE cliente (
id BIGSERIAL PRIMARY KEY,
nombre VARCHAR(200) NOT NULL,
telefono VARCHAR(50),
fecha_creacion TIMESTAMP DEFAULT NOW()
);


-- ==========================================
-- DEUDA CLIENTE
-- ==========================================

CREATE TABLE deuda_cliente (
id BIGSERIAL PRIMARY KEY,
cliente_id BIGINT NOT NULL,
venta_id BIGINT,
monto NUMERIC(10,2) NOT NULL,
pagado BOOLEAN DEFAULT FALSE,
fecha TIMESTAMP DEFAULT NOW(),
FOREIGN KEY (cliente_id) REFERENCES cliente(id),
FOREIGN KEY (venta_id) REFERENCES venta(id)
);


-- ==========================================
-- SOLICITUD PRODUCTOS
-- ==========================================

CREATE TABLE solicitud_producto (
id BIGSERIAL PRIMARY KEY,
nombre_producto VARCHAR(200) NOT NULL,
cliente_id BIGINT,
descripcion TEXT,
fecha TIMESTAMP DEFAULT NOW(),
FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);


-- ==========================================
-- PRODUCTOS PENDIENTES
-- ==========================================

CREATE TABLE producto_pendiente (
id BIGSERIAL PRIMARY KEY,
nombre VARCHAR(200) NOT NULL,
precio NUMERIC(10,2),
registrado BOOLEAN DEFAULT FALSE,
fecha TIMESTAMP DEFAULT NOW()
);


-- ==========================================
-- DEVOLUCIONES
-- ==========================================

CREATE TABLE devolucion (
id BIGSERIAL PRIMARY KEY,
producto_id BIGINT,
venta_id BIGINT,
cantidad INTEGER NOT NULL,
motivo TEXT,
fecha TIMESTAMP DEFAULT NOW(),
FOREIGN KEY (producto_id) REFERENCES producto(id),
FOREIGN KEY (venta_id) REFERENCES venta(id)
);


-- ==========================================
-- AUDITORIA
-- ==========================================

CREATE TABLE auditoria (
id BIGSERIAL PRIMARY KEY,
usuario_id BIGINT,
accion VARCHAR(100) NOT NULL,
entidad VARCHAR(100),
entidad_id BIGINT,
descripcion TEXT,
fecha TIMESTAMP DEFAULT NOW(),
FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);