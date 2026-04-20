package com.posstudio.papel.turnos.service.impl;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.posstudio.papel.common.exception.BusinessException;
import com.posstudio.papel.common.exception.ResourceNotFoundException;
import com.posstudio.papel.turnos.dto.request.TurnoEmpleadoRequest;
import com.posstudio.papel.turnos.dto.responsive.TurnoEmpleadoResponsiveDTO;
import com.posstudio.papel.turnos.model.Empleado;
import com.posstudio.papel.turnos.model.Turno;
import com.posstudio.papel.turnos.model.TurnoEmpleado;
import com.posstudio.papel.turnos.repository.TurnoEmpleadoRepository;
import com.posstudio.papel.turnos.service.EmpleadoService;
import com.posstudio.papel.turnos.service.TurnoEmpleadoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TurnoEmpleadoServiceImpl implements TurnoEmpleadoService {

    private final EmpleadoService empleadoService;
    private final TurnoEmpleadoRepository turnoEmpleadoRepository;

    private TurnoEmpleadoResponsiveDTO conversorDTO(TurnoEmpleado turnoEmpleado) {
        return new TurnoEmpleadoResponsiveDTO(turnoEmpleado.getId(), turnoEmpleado.getTurno().getTipoTurno(),
                turnoEmpleado.getEmpleado().getNombre(), turnoEmpleado.getHoraEntrada(), turnoEmpleado.getHoraSalida());
    }

    @Override
    public List<TurnoEmpleadoResponsiveDTO> crearTurnoEmpleado(TurnoEmpleadoRequest data, Turno turno) {

        // filtro turnoEmpleado
        List<Empleado> empleados = filtroTurnoEmpleado(data);

        // verificar que los empleados no esten duplicados en el turno
        List<Long> empleadosExistentes = turnoEmpleadoRepository.findEmpleadoIdsByTurnoId(turno.getId());
        for (Empleado empleado : empleados) {
            if (empleadosExistentes.contains(empleado.getId())) {
                throw new BusinessException("El empleado con id " + empleado.getId() + " ya está en el turno");
            }
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
    public List<TurnoEmpleadoResponsiveDTO> registrarSalidaEmpleado(TurnoEmpleadoRequest data, Turno turno) {

        // filtro TurnoEmpleado
        List<Empleado> empleados = filtroTurnoEmpleado(data);
        // validar que los empleados esten dentro del turno
        List<Long> empleadosExistentes = turnoEmpleadoRepository.findEmpleadoIdsByTurnoId(turno.getId());
        for (Empleado empleado : empleados) {
            if (!empleadosExistentes.contains(empleado.getId())) {
                throw new BusinessException(
                        "El empleado con id " + empleado.getId() + " no está registrado en el turno");
            }
        }
        // validar que el turno no quede solo

        if (empleadosExistentes.size() - empleados.size() <= 0) {
            throw new BusinessException("El turno no puede quedar sin empleados");
        }
        // registrar salida
        List<Long> empleadoIds = empleados.stream().map(Empleado::getId).toList();
        List<TurnoEmpleado> turnoEmpleados = turnoEmpleadoRepository
                .findByTurnoIdAndEmpleadoIdIn(turno.getId(), empleadoIds);

        turnoEmpleados.forEach(te -> te.setHoraSalida(LocalDateTime.now()));
        // guardarla
        turnoEmpleadoRepository.saveAll(turnoEmpleados);

        // retornar mapeo
        return turnoEmpleados.stream().map(this::conversorDTO).toList();
    }

    @Override
    public void eliminarRegistroEmpleado(Turno turno, Long empleadoId) {
        TurnoEmpleado turnoEmpleado = turnoEmpleadoRepository.findByTurnoIdAndEmpleadoId(turno.getId(), empleadoId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Turno empleado no encontrado", empleadoId.toString()));

        LocalDateTime limiteEdicion = LocalDateTime.now().minusHours(1);

        if (turnoEmpleado.getHoraEntrada().isBefore(limiteEdicion)) {
            throw new BusinessException("No se puede eliminar un registro con más de 1 hora de antigüedad");
        }

        turnoEmpleadoRepository.delete(turnoEmpleado);
    }

    private List<Empleado> filtroTurnoEmpleado(TurnoEmpleadoRequest data) {

        // validar que no hayan ids repetidos
        Set<Long> validacionIds = new HashSet<>(data.empleadoIds());
        if (validacionIds.size() != data.empleadoIds().size()) {
            throw new BusinessException("La lista de empleados contiene IDs duplicados");
        }
        // buscar empleados
        List<Empleado> empleados = validacionIds.stream().map(emp -> empleadoService.findById(emp)).toList();
        return empleados;

    }

    @Override
    public void registarCierreTurno(Turno turno) {
        List<TurnoEmpleado> sinSalida = turnoEmpleadoRepository
                .findByTurnoIdAndHoraSalidaIsNull(turno.getId());

        sinSalida.forEach(te -> te.setHoraSalida(LocalDateTime.now()));
        turnoEmpleadoRepository.saveAll(sinSalida);
    }

}
