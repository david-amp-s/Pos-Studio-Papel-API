package com.posstudio.papel.ventas.dto.responsive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.posstudio.papel.common.enums.EstadoVenta;
import com.posstudio.papel.common.enums.TipoTurno;

public record VentaResponsiveDTO(
        Long id,
        String nombreUsuario,
        TipoTurno tipoTurno,
        BigDecimal total,
        LocalDateTime fecha,
        EstadoVenta estadoVenta,
        Integer CantiadDeProductos) {

}
