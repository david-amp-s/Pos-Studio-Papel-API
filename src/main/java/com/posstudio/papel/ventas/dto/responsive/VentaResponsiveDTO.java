package com.posstudio.papel.ventas.dto.responsive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.posstudio.papel.common.enums.EstadoVenta;
import com.posstudio.papel.common.enums.TipoTurno;
import com.posstudio.papel.ventas.model.DetalleVenta;
import com.posstudio.papel.ventas.model.Pagoventa;

public record VentaResponsiveDTO(
        Long id,
        String nombreUsuario,
        TipoTurno tipoTurno,
        BigDecimal total,
        BigDecimal descuento,
        LocalDateTime fecha,
        EstadoVenta estadoVenta,
        List<DetalleVenta> detalles,
        List<Pagoventa> pagos) {

}
