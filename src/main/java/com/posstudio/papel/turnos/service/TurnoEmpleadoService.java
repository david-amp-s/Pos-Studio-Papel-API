package com.posstudio.papel.turnos.service;

import java.math.BigDecimal;
import java.util.List;

import com.posstudio.papel.turnos.dto.request.TurnoEmpleadoRequest;
import com.posstudio.papel.turnos.dto.responsive.TurnoEmpleadoResponsiveDTO;

public interface TurnoEmpleadoService {
    List<TurnoEmpleadoResponsiveDTO> crearTurnoEmpleado(TurnoEmpleadoRequest data, Long turnoId);

    List<TurnoEmpleadoResponsiveDTO> registrarSalidaEmpleado(TurnoEmpleadoRequest data, Long turnoId);

    void eliminarRegistroEmpleado(Long id);
}
