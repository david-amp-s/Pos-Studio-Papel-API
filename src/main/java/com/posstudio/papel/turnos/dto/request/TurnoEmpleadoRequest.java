package com.posstudio.papel.turnos.dto.request;

import java.util.List;

import com.posstudio.papel.common.enums.TipoAccionTurno;

import jakarta.validation.constraints.NotEmpty;

public record TurnoEmpleadoRequest(
                @NotEmpty(message = "La lista no puede estar vacia") List<Long> empleadoIds,
                TipoAccionTurno tipoAccionTurno) {
}