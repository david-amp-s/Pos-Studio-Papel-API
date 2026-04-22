package com.posstudio.papel.turnos.dto.responsive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.posstudio.papel.common.enums.EstadoTurno;
import com.posstudio.papel.common.enums.TipoTurno;

public record TurnoResponsiveDTO(
                Long id,
                LocalDate fecha,
                TipoTurno tipoTurno,
                EstadoTurno estadoTurno,
                BigDecimal dineroApertura,
                BigDecimal dineroCierre,
                BigDecimal diferencia,
                LocalDateTime fechaApertura,
                LocalDateTime fechaCierre) {

}
