package com.posstudio.papel.ventas.dto.request;

import java.math.BigDecimal;

import com.posstudio.papel.common.enums.MetodoPago;

import jakarta.validation.constraints.NotNull;

public record MetodoPagoDTO(
        @NotNull(message = "El metodo de pago no puede ser null") MetodoPago metodoPago,
        @NotNull(message = "Debe haber un monto") BigDecimal monto) {

}
