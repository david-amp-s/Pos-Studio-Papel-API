package com.posstudio.papel.ventas.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

public record DetalleVentaRequestDTO(
                @NotNull(message = "No puede estar vacio el id de producto") Long productoId,
                @NotNull(message = "No puede estar vacia la cantidad") Integer cantidad,
                BigDecimal descuento) {

}
