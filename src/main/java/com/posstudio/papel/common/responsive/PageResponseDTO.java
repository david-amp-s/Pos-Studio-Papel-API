package com.posstudio.papel.common.responsive;

import java.util.List;

import org.springframework.data.domain.Page;

public record PageResponseDTO<T>(
        List<T> contenido,
        int paginaActual,
        int totalPaginas,
        long totalElementos,
        boolean esUltimaPagina) {
    public static <T> PageResponseDTO<T> from(Page<T> page) {
        return new PageResponseDTO<>(
                page.getContent(),
                page.getNumber(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.isLast());
    }
}