package com.posstudio.papel.turnos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.posstudio.papel.turnos.model.Empleado;
import com.posstudio.papel.turnos.model.Turno;
import com.posstudio.papel.turnos.model.TurnoEmpleado;

public interface TurnoEmpleadoRepository extends JpaRepository<TurnoEmpleado, Long> {
    @Query("""
                SELECT te.empleado.id
                FROM TurnoEmpleado te
                WHERE te.turno.id = :turnoId
            """)
    List<Long> findEmpleadoIdsByTurnoId(Long turnoId);

    // NUEVO: Trae empleados que NO han salido (horaSalida IS NULL)
    @Query("""
            SELECT te.empleado
            FROM TurnoEmpleado te
            WHERE te.turno.id = :turnoId
            AND te.horaSalida IS NULL
            """)
    List<Empleado> findEmpleadosActivosByTurnoId(@Param("turnoId") Long turnoId);

    // NUEVO: Trae TurnoEmpleado completo solo de los que NO han salido
    @Query("""
            SELECT te
            FROM TurnoEmpleado te
            WHERE te.turno = :turno
            AND te.empleado IN :empleados
            AND te.horaSalida IS NULL
            """)
    List<TurnoEmpleado> findActivosByTurnoAndEmpleadoIn(@Param("turno") Turno turno,
            @Param("empleados") List<Empleado> empleados);

    // NUEVO: Trae TurnoEmpleado de los que SÍ han salido (para validar reingreso)
    @Query("""
            SELECT te
            FROM TurnoEmpleado te
            WHERE te.turno = :turno
            AND te.empleado IN :empleados
            AND te.horaSalida IS NOT NULL
            """)
    List<TurnoEmpleado> findSalidasByTurnoAndEmpleadoIn(@Param("turno") Turno turno,
            @Param("empleados") List<Empleado> empleados);

    // busca todos los turnoEmpleados por el turno id y empleado id
    @Query("""
            SELECT te
            FROM TurnoEmpleado te
            WHERE te.turno.id = :turnoId
            AND te.empleado.id IN :empleadoIds
            """)
    List<TurnoEmpleado> findAllByTurnoIdAndEmpleadoIds(@Param("turnoId") Long turnoId,
            @Param("empleadoIds") List<Long> empleadoIds);

    // NUEVO: Empleados que NO tienen NINGÚN registro en el turno
    @Query("""
            SELECT e
            FROM Empleado e
            WHERE e.activo = true
            AND NOT EXISTS (
                SELECT 1
                FROM TurnoEmpleado te
                WHERE te.empleado.id = e.id
                AND te.turno.id = :turnoId
            )
            """)
    List<Empleado> findEmpleadosSinRegistroEnTurno(@Param("turnoId") Long turnoId);

    Optional<TurnoEmpleado> findByTurnoIdAndEmpleadoId(Long turnoId, Long empleadoId);

    List<TurnoEmpleado> findByTurnoIdAndHoraSalidaIsNull(Long turnoId);

}
