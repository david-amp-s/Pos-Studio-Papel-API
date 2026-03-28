package com.posstudio.papel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.posstudio.papel.model.Ubicacion;
import java.util.Optional;

public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {
    Optional<Ubicacion> findByCodigo(String codigo);
}
