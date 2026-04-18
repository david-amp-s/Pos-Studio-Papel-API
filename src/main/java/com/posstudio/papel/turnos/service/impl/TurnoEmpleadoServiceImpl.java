package com.posstudio.papel.turnos.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.posstudio.papel.common.enums.EstadoTurno;
import com.posstudio.papel.common.exception.BusinessException;
import com.posstudio.papel.turnos.dto.request.TurnoEmpleadoRequest;
import com.posstudio.papel.turnos.dto.responsive.TurnoEmpleadoResponsiveDTO;
import com.posstudio.papel.turnos.model.Empleado;
import com.posstudio.papel.turnos.model.Turno;
import com.posstudio.papel.turnos.service.EmpleadoService;
import com.posstudio.papel.turnos.service.TurnoEmpleadoService;
import com.posstudio.papel.turnos.service.TurnoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TurnoEmpleadoServiceImpl implements TurnoEmpleadoService {
    private final TurnoService turnoService;
    private final EmpleadoService empleadoService;

    @Override
    public TurnoEmpleadoResponsiveDTO crearTurnoEmpleado(TurnoEmpleadoRequest data, Long turnoId) {
        Turno turno = turnoService.buscarTurnoId(turnoId);
        if (turno.getEstadoTurno() != EstadoTurno.ABIERTO) {
            throw new BusinessException("El turno debe de esatar abierto para registrar empleados");
        }
        List<Empleado> empleados = data.empleadoIds().stream().map(emp -> empleadoService.findById(turnoId)).toList();
        if (empleados.isEmpty()) {
            throw new BusinessException("No se puede hacer registro sin empleados");
        }

        for (Empleado empleado : empleados) {
            if (empleadosEx)
        }
    }

}
