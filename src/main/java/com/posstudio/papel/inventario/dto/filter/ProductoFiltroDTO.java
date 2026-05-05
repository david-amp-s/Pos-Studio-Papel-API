package com.posstudio.papel.inventario.dto.filter;

// ProductoFiltroDTO.java
public record ProductoFiltroDTO(
        String nombre, // búsqueda incremental
        String categoria,
        String ubicacion,
        String codigoDeBarras) {
}