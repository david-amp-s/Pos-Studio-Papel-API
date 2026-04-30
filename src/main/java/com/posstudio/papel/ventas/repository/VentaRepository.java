package com.posstudio.papel.ventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.posstudio.papel.ventas.model.Venta;
import java.util.List;
import com.posstudio.papel.common.enums.EstadoVenta;
import com.posstudio.papel.security.model.Usuario;
import com.posstudio.papel.turnos.model.Turno;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByEstado(EstadoVenta estado);

    List<Venta> findByEstadoAndUsuarioAndTurno(EstadoVenta estado, Usuario usuario, Turno turno);
}
