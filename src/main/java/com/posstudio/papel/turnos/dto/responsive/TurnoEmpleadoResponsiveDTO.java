package com.posstudio.papel.turnos.dto.responsive;

import java.time.LocalDateTime;

import com.posstudio.papel.common.enums.TipoTurno;

public record TurnoEmpleadoResponsiveDTO(
        Long id,
        TipoTurno turno,
        String empleado,
        LocalDateTime horaEntrada,
        LocalDateTime horaSalida) {

}
