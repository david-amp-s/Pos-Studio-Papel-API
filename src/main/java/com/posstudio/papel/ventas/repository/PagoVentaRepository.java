package com.posstudio.papel.ventas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.posstudio.papel.common.enums.MetodoPago;
import com.posstudio.papel.ventas.model.Pagoventa;

public interface PagoVentaRepository extends JpaRepository<Pagoventa, Long> {
    List<Pagoventa> findByVenta_TurnoId(Long turnoId);

    List<Pagoventa> findByVenta_TurnoIdAndMetodo(Long turnoId, MetodoPago metodo);
}
