package com.posstudio.papel.ventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.posstudio.papel.ventas.model.DetalleVenta;
import com.posstudio.papel.ventas.model.Venta;

import java.util.Optional;

import com.posstudio.papel.inventario.model.Producto;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
    Optional<DetalleVenta> findByProductoAndVenta(Producto producto, Venta venta);
}
