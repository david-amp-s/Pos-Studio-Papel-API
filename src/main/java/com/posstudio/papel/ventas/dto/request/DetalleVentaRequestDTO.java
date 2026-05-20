package com.posstudio.papel.ventas.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

public record DetalleVentaRequestDTO(
        Long productoId,
        Long productoPendienteId,
        @NotNull(message = "No puede estar vacia la cantidad") Integer cantidad,
        BigDecimal descuento) {

}
