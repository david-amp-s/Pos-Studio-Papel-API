package com.posstudio.papel.turnos.service;

import java.util.List;

import com.posstudio.papel.turnos.dto.request.TurnoEmpleadoRequest;
import com.posstudio.papel.turnos.dto.responsive.EmpleadoResponsiveDTO;
import com.posstudio.papel.turnos.dto.responsive.TurnoResponsiveDTO;
import com.posstudio.papel.turnos.model.Turno;

public interface TurnoService {
    TurnoResponsiveDTO crearTurno(TurnoEmpleadoRequest data);

    TurnoResponsiveDTO editarTurno(Long turnoId, TurnoEmpleadoRequest data);

    TurnoResponsiveDTO cerrarTurno(Long turnoId);

    Turno buscarTurnoId(Long id);

    TurnoResponsiveDTO obtenerTurnoActivo();

    List<EmpleadoResponsiveDTO> empleadoEnTurno();

    List<EmpleadoResponsiveDTO> empleadosAfueraTurno();
}
