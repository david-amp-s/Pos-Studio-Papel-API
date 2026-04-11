package com.posstudio.papel.inventario.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CategoriaResquestDTO(
        @NotBlank(message = "No puede estar en blanco") String nombre

) {

}
