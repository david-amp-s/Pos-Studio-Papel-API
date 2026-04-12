package com.posstudio.papel.inventario.dto.responsive;

import java.math.BigDecimal;

public record ProductoPendienteResponsiveDTO(Long id,
        String nombre,
        BigDecimal precio,
        Boolean registrado) {

}
