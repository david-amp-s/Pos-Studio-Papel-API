package com.posstudio.papel.ventas.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

public record EditarDetalleVentaRequestDTO(
        @NotNull(message = "La cantidad no puede ser null") Integer cantidad,
        BigDecimal descuento) {

}
