package com.posstudio.papel.turnos.dto.request;

import jakarta.validation.constraints.NotBlank;

public record EmpleadoRequestDTO(
        @NotBlank(message = "No puede estar vacio el nombre") String nombre) {

}
