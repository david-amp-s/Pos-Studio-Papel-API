package com.posstudio.papel.dto.responsive;

import java.math.BigDecimal;

public record ProductoResponsiveDTO(
        Long id,
        String nombre,
        String codigoDeBarras,
        BigDecimal precio,
        Integer stock,
        String categoria,
        String ubicacion,
        String unidadNegocio) {
}
