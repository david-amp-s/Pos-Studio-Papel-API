package com.posstudio.papel.turnos.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.posstudio.papel.common.enums.EstadoTurno;
import com.posstudio.papel.common.exception.BusinessException;
import com.posstudio.papel.turnos.dto.request.TurnoEmpleadoRequest;
import com.posstudio.papel.turnos.dto.responsive.TurnoEmpleadoResponsiveDTO;
import com.posstudio.papel.turnos.model.Empleado;
import com.posstudio.papel.turnos.model.Turno;
import com.posstudio.papel.turnos.model.TurnoEmpleado;
import com.posstudio.papel.turnos.repository.TurnoEmpleadoRepository;
import com.posstudio.papel.turnos.service.EmpleadoService;
import com.posstudio.papel.turnos.service.TurnoEmpleadoService;
import com.posstudio.papel.turnos.service.TurnoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TurnoEmpleadoServiceImpl implements TurnoEmpleadoService {
    private final TurnoService turnoService;
    private final EmpleadoService empleadoService;
    private final TurnoEmpleadoRepository turnoEmpleadoRepository;

    private TurnoEmpleadoResponsiveDTO conversorDTO(TurnoEmpleado turnoEmpleado) {
        return new TurnoEmpleadoResponsiveDTO(turnoEmpleado.getId(), turnoEmpleado.getTurno().getTipoTurno(),
                turnoEmpleado.getEmpleado().getNombre(), turnoEmpleado.getHoraEntrada(), turnoEmpleado.getHoraSalida());
    }

    @Override
    public List<TurnoEmpleadoResponsiveDTO> crearTurnoEmpleado(TurnoEmpleadoRequest data, Long turnoId) {

        // validar turno abierto
        Turno turno = turnoService.buscarTurnoId(turnoId);
        validarTurnoAbierto(turno);
        // obtener a los empleados

        List<Empleado> empleados = data.empleadoIds().stream()
                .map(emp -> empleadoService.findById(emp)).toList();

        // verificar que los empleados no esten duplicados en el turno
        List<Long> empleadosExistentes = turnoEmpleadoRepository.findEmpleadoIdsByTurnoId(turno.getId());
        for (Empleado empleado : empleados) {
            if (empleadosExistentes.contains(empleado.getId())) {
                throw new BusinessException("El empleado con id " + empleado.getId() + " ya está en el turno");
            }
        }
        // evitar duplicados en la solicitud
        Set<Long> uniqueIds = new HashSet<>(data.empleadoIds());

        if (uniqueIds.size() != data.empleadoIds().size()) {
            throw new BusinessException("Hay IDs duplicados en la solicitud");
        }
        // guardar en la base de datos
        List<TurnoEmpleado> turnoEmpleados = empleados.stream().map(emp -> TurnoEmpleado.builder()
                .turno(turno)
                .empleado(emp)
                .horaEntrada(LocalDateTime.now())
                .build())
                .toList();
        turnoEmpleadoRepository.saveAll(turnoEmpleados);
        // retornarlo mapeado
        return turnoEmpleados.stream().map(this::conversorDTO).toList();
    }

    @Override
    public List<TurnoEmpleadoResponsiveDTO> registrarSalidaEmpleado(TurnoEmpleadoRequest data, Long turnoId) {
        // buscar turno
        Turno turno = turnoService.buscarTurnoId(turnoId);
        // validar turno activo
        validarTurnoAbierto(turno);
        // validar que no hayan ids repetidos

        // buscar empleados
        // validar que los empleados esten dentro del turno
        // registrar salida
        // guardarla
        // retornar mapeo
    }

    private List<Empleado> filtroTurnoEmpleado(TurnoEmpleadoRequest data, Long turnoId) {
        // buscar turno
        Turno turno = turnoService.buscarTurnoId(turnoId);
        // validar turno activo
        if (turno.getEstadoTurno() != EstadoTurno.ABIERTO) {
            throw new BusinessException("El turno debe de estar abierto para registrar empleados");
        }
        // validar que no hayan ids repetidos
        Set<Long> validacionIds = new HashSet<>(data.empleadoIds());
        // buscar empleados
        List<Empleado> empleados = validacionIds.stream().map(emp -> empleadoService.findById(emp)).toList();
        return empleados;

    }

    @Override
    public void eliminarRegistroEmpleado(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eliminarRegistroEmpleado'");
    }

}
