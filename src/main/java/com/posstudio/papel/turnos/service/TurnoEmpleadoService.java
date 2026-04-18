package com.posstudio.papel.turnos.service;

import com.posstudio.papel.turnos.dto.request.TurnoEmpleadoRequest;
import com.posstudio.papel.turnos.dto.responsive.TurnoEmpleadoResponsiveDTO;

public interface TurnoEmpleadoService {
    TurnoEmpleadoResponsiveDTO crearTurnoEmpleado(TurnoEmpleadoRequest data);
}
