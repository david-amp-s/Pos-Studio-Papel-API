package com.posstudio.papel.inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.posstudio.papel.inventario.model.Categoria;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByNombre(String nombre);
}
