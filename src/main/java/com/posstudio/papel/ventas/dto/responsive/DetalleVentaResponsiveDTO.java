package com.posstudio.papel.ventas.dto.responsive;

import java.math.BigDecimal;

public record DetalleVentaResponsiveDTO(
                Long id,
                Long productoId,
                String productoNombre,
                Long productoPendienteId,
                String productoPendienteNombre,
                Integer cantidad,
                BigDecimal precioUnitario,
                BigDecimal subtotal,
                BigDecimal descuento) {
}
