package com.posstudio.papel.turnos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.posstudio.papel.common.enums.EstadoTurno;
import com.posstudio.papel.turnos.model.Empleado;
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

    @Query("""
                SELECT te.empleado
                FROM TurnoEmpleado te
                WHERE te.turno.id = :turnoId
            """)
    List<Empleado> findEmpleadosByTurnoId(@Param("turnoId") Long turnoId);

    @Query("""
                SELECT e
                FROM Empleado e
                WHERE NOT EXISTS (
                    SELECT 1
                    FROM TurnoEmpleado te
                    WHERE te.empleado.id = e.id
                    AND te.turno.id = :turnoId
                )
            """)
    List<Empleado> findEmpleadosNotInTurno(@Param("turnoId") Long turnoId);
}
