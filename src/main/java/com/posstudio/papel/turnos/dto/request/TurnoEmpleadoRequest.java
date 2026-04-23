package com.posstudio.papel.turnos.dto.request;

import java.util.List;

import com.posstudio.papel.common.enums.TipoAccionTurno;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record TurnoEmpleadoRequest(
        @NotEmpty(message = "La lista no puede estar vacia") List<Long> empleadoIds,
        @NotNull(message = "La accion no puede ser nula") TipoAccionTurno tipoAccionTurno) {
}