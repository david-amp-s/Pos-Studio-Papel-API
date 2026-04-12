package com.posstudio.papel.inventario.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductoPendienteRequestDTO(
                @NotBlank(message = "El nombre no puede estar vacio") String nombre,
                @NotNull(message = "El precio no puede estar vacio") BigDecimal precio) {

}
