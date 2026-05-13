package com.posstudio.papel.ventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.posstudio.papel.ventas.model.Venta;

import jakarta.persistence.LockModeType;

import java.util.List;
import java.util.Optional;

import com.posstudio.papel.common.enums.EstadoVenta;
import com.posstudio.papel.security.model.Usuario;
import com.posstudio.papel.turnos.model.Turno;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByEstado(EstadoVenta estado);

    List<Venta> findByEstadoAndUsuarioAndTurno(EstadoVenta estado, Usuario usuario, Turno turno);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT v FROM Venta v WHERE v.id = :id")
    Optional<Venta> findByIdWithLock(@Param("id") Long id);

    @Query("""
    SELECT v FROM Venta v
    LEFT JOIN FETCH v.detalles d
    LEFT JOIN FETCH d.producto
    LEFT JOIN FETCH v.usuario
    LEFT JOIN FETCH v.turno
    WHERE v.id = :id
""")
Optional<Venta> findByIdConDetalles(@Param("id") Long id);
}
