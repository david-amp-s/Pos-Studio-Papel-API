package com.posstudio.papel.ventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.posstudio.papel.ventas.model.DetalleVenta;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {

}
