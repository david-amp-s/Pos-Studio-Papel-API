package com.posstudio.papel.turnos.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

public record PagoCierreTurnoDTO(
                @NotNull(message = "NO puede ser null cierre turno") BigDecimal totalCaja) {

}
