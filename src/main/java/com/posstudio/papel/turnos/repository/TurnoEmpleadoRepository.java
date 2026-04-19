package com.posstudio.papel.turnos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.posstudio.papel.turnos.model.TurnoEmpleado;

public interface TurnoEmpleadoRepository extends JpaRepository<TurnoEmpleado, Long> {
    @Query("""
                SELECT te.empleado.id
                FROM TurnoEmpleado te
                WHERE te.turno.id = :turnoId
            """)
    List<Long> findEmpleadoIdsByTurnoId(Long turnoId);
}
