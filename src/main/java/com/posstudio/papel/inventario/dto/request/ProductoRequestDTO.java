package com.posstudio.papel.inventario.dto.request;

import java.math.BigDecimal;

import com.posstudio.papel.common.enums.UnidadNegocio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductoRequestDTO(@NotBlank(message = "No debe de estar vacio") String nombre,
        String codigoDeBarras,
        @NotNull(message = "Debe de tener un valor inicial") BigDecimal precio,
        Integer stock,
        String categoria,
        @NotBlank(message = "No debe de estar vacio") String ubicacion,
        @NotNull(message = "Debe seleccionar una unidad de negocio") UnidadNegocio unidadNegocio) {
}
