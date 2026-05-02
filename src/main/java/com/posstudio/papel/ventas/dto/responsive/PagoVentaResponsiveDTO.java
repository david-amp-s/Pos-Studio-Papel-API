package com.posstudio.papel.ventas.dto.responsive;

import java.math.BigDecimal;

import com.posstudio.papel.common.enums.MetodoPago;

public record PagoVentaResponsiveDTO(
                Long id,
                Long ventaId,
                MetodoPago metodoPago,
                BigDecimal monto) {

}
