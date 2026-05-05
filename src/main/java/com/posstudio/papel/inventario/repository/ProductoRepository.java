package com.posstudio.papel.inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.posstudio.papel.inventario.model.Producto;

import java.util.Optional;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long>, JpaSpecificationExecutor<Producto> {
    Optional<Producto> findByNombre(String nombre);

    List<Producto> findByActivo(Boolean activo);
}
