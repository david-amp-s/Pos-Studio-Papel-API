package com.posstudio.papel.ventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.posstudio.papel.ventas.model.DetalleVenta;
import com.posstudio.papel.ventas.model.Venta;

import jakarta.persistence.LockModeType;

import java.math.BigDecimal;
import java.util.Optional;

import com.posstudio.papel.inventario.model.Producto;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT d FROM DetalleVenta d WHERE d.id = :id")
    Optional<DetalleVenta> findByIdWithLock(@Param("id") Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<DetalleVenta> findByProductoAndVenta(Producto producto, Venta venta);

    @Query("SELECT COALESCE(SUM(d.subtotal), 0) FROM DetalleVenta d WHERE d.venta.id = :ventaId")
    BigDecimal sumSubtotalByVentaId(@Param("ventaId") Long ventaId);
}
