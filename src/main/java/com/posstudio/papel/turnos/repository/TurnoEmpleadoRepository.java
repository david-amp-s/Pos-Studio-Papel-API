package com.posstudio.papel.turnos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.posstudio.papel.turnos.model.Turno;
import com.posstudio.papel.turnos.model.TurnoEmpleado;

public interface TurnoEmpleadoRepository extends JpaRepository<TurnoEmpleado, Long> {
    @Query("""
                SELECT te.empleado.id
                FROM TurnoEmpleado te
                WHERE te.turno.id = :turnoId
            """)
    List<Long> findEmpleadoIdsByTurnoId(Long turnoId);

    List<TurnoEmpleado> findByTurnoIdAndEmpleadoIdIn(Long turnoId, List<Long> empleadoIds);

    Optional<TurnoEmpleado> findByTurnoIdAndEmpleadoId(Long turnoId, Long empleadoId);

    List<TurnoEmpleado> findByTurnoIdAndHoraSalidaIsNull(Long turnoId);
}
