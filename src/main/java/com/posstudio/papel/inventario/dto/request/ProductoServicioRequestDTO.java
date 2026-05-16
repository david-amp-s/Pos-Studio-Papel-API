package com.posstudio.papel.inventario.dto.request;

import java.math.BigDecimal;

import com.posstudio.papel.common.enums.TipoProducto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductoServicioRequestDTO(
        @NotBlank(message = "El nombre no puede estar vacio") String nombre,
        @NotNull(message = "EL precio no puede estar vacio") BigDecimal precio,
        @NotNull(message = "El tipo de producto no puede ser nulo") TipoProducto tipoProducto) {

}
