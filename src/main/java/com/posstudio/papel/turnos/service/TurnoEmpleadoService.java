package com.posstudio.papel.turnos.service;

import java.util.List;

import com.posstudio.papel.turnos.dto.request.TurnoEmpleadoRequest;
import com.posstudio.papel.turnos.dto.responsive.TurnoEmpleadoResponsiveDTO;
import com.posstudio.papel.turnos.model.Empleado;
import com.posstudio.papel.turnos.model.Turno;

public interface TurnoEmpleadoService {
    List<TurnoEmpleadoResponsiveDTO> crearTurnoEmpleado(TurnoEmpleadoRequest data, Turno turno);

    List<TurnoEmpleadoResponsiveDTO> registrarSalidaEmpleado(TurnoEmpleadoRequest data, Turno turno);

    void eliminarRegistroEmpleado(Turno turno, Long empleadoId);

    void registarCierreTurno(Turno turno);

    List<Empleado> listarEmpleadosEnTurno(Long turnoId);

    List<Empleado> listarEmpleadosFueraDeTurno(Long turnoId);
}
