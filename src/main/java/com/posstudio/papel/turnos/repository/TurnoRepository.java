package com.posstudio.papel.turnos.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.posstudio.papel.turnos.model.Turno;

import com.posstudio.papel.common.enums.EstadoTurno;
import com.posstudio.papel.common.enums.TipoTurno;

public interface TurnoRepository extends JpaRepository<Turno, Long> {
    Optional<Turno> findByEstadoTurno(EstadoTurno estadoTurno);

    Boolean existsByFecha(LocalDate date);

    boolean existsByFechaAndTipoTurno(LocalDate fecha, TipoTurno tipoTurno);
}
