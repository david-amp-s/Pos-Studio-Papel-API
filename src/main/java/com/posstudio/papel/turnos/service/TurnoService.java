package com.posstudio.papel.turnos.service;

import com.posstudio.papel.turnos.dto.request.TurnoEmpleadoRequest;

import com.posstudio.papel.turnos.dto.responsive.TurnoResponsiveDTO;
import com.posstudio.papel.turnos.model.Turno;

public interface TurnoService {
    TurnoResponsiveDTO crearTurno(TurnoEmpleadoRequest data);

    Turno buscarTurnoId(Long id);
}
