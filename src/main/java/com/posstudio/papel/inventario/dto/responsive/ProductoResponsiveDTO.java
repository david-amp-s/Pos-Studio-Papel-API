package com.posstudio.papel.inventario.dto.responsive;

import java.math.BigDecimal;

import com.posstudio.papel.common.enums.TipoProducto;

public record ProductoResponsiveDTO(
        Long id,
        String nombre,
        String codigoDeBarras,
        BigDecimal precio,
        TipoProducto tipoProducto,
        Integer stock,
        String categoria,
        String ubicacion,
        String unidadNegocio,
        Boolean favorito) {
}
