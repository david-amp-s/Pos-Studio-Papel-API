package com.posstudio.papel.turnos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.posstudio.papel.turnos.model.Empleado;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    Optional<Empleado> findByNombre(String nombre);

    List<Empleado> findByActivo(Boolean activo);
}
