package com.posstudio.papel.inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.posstudio.papel.inventario.model.Producto;

import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    Optional<Producto> findByNombre(String nombre);
}
