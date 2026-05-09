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
}
