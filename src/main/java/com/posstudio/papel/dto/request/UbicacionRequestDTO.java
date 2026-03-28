package com.posstudio.papel.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UbicacionRequestDTO(
        @NotBlank(message = "El espacio no puede estar vacio") String codigo) {

}
