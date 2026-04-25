package com.posstudio.papel.turnos.service.impl;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional(readOnly = true)
public class TurnoEmpleadoServiceImpl implements TurnoEmpleadoService {

    private final EmpleadoService empleadoService;
    private final TurnoEmpleadoRepository turnoEmpleadoRepository;

    private TurnoEmpleadoResponsiveDTO conversorDTO(TurnoEmpleado turnoEmpleado) {
        return new TurnoEmpleadoResponsiveDTO(turnoEmpleado.getId(), turnoEmpleado.getTurno().getTipoTurno(),
                turnoEmpleado.getEmpleado().getNombre(), turnoEmpleado.getHoraEntrada(), turnoEmpleado.getHoraSalida());
    }

    @Override
    @Transactional
    public List<TurnoEmpleadoResponsiveDTO> crearTurnoEmpleado(TurnoEmpleadoRequest data, Turno turno) {

        // filtro turnoEmpleado
        List<Empleado> empleados = filtroTurnoEmpleado(data);

        // verificar que los empleados no esten duplicados en el turno
        List<Long> idEmpleadosExistentes = turnoEmpleadoRepository.findEmpleadosActivosByTurnoId(turno.getId()).stream()
                .map(Empleado::getId).toList();

        List<Empleado> empleadosNuevos = empleados.stream().filter(emp -> !idEmpleadosExistentes.contains(emp.getId()))
                .toList();

        if (empleadosNuevos.isEmpty()) {
            throw new BusinessException("Todos los empleados ya están en el turno");
        }

        List<TurnoEmpleado> empleadosConSalidaPrevia = turnoEmpleadoRepository
                .findSalidasByTurnoAndEmpleadoIn(turno, empleadosNuevos);

        if (!empleadosConSalidaPrevia.isEmpty()) {
            String nombres = empleadosConSalidaPrevia.stream()
                    .map(te -> te.getEmpleado().getNombre())
                    .collect(Collectors.joining(", "));
            throw new BusinessException("No se puede reingresar a empleados que ya salieron: " + nombres);
        }

        // guardar en la base de datos
        List<TurnoEmpleado> turnoEmpleados = empleadosNuevos.stream().map(emp -> TurnoEmpleado.builder()
                .turno(turno)
                .empleado(emp)
                .horaEntrada(LocalDateTime.now())
                .build())
                .toList();
        return turnoEmpleadoRepository.saveAll(turnoEmpleados).stream().map(this::conversorDTO).toList();
    }

    @Override
    @Transactional
    public List<TurnoEmpleadoResponsiveDTO> registrarSalidaEmpleado(TurnoEmpleadoRequest data, Turno turno) {

        // 1. Filtrar y validar empleados solicitados

        List<Empleado> empleadosSolicitados = filtroTurnoEmpleado(data);

        List<Long> idsEmpleadosSolicitados = empleadosSolicitados.stream().map(Empleado::getId).toList();

        // 2. Obtener IDs de empleados ACTIVOS en el turno
        List<Long> idsEmpleadosActivos = turnoEmpleadoRepository.findEmpleadosActivosByTurnoId(turno.getId()).stream()
                .map(Empleado::getId).toList();
        // 3. Validar que TODOS los solicitados estén activos
        List<Long> idsNoActivos = idsEmpleadosSolicitados.stream().filter(id -> !idsEmpleadosActivos.contains(id))
                .toList();

        if (!idsNoActivos.isEmpty()) {
            throw new BusinessException("Los siguientes empleados no están activos en el turno: " + idsNoActivos);
        }
        // 4. Validar que el turno NO quede sin empleados
        if (idsEmpleadosActivos.size() - idsEmpleadosSolicitados.size() <= 0) {
            throw new BusinessException("El turno no puede quedar sin empleados");
        }
        // 5. Registrar salida SOLO de los activos (usando el método correcto)
        List<TurnoEmpleado> turnoEmpleados = turnoEmpleadoRepository.findActivosByTurnoAndEmpleadoIn(turno,
                empleadosSolicitados);

        turnoEmpleados.forEach(te -> te.setHoraSalida(LocalDateTime.now()));
        turnoEmpleadoRepository.saveAll(turnoEmpleados);

        return turnoEmpleados.stream().map(this::conversorDTO).toList();
    }

    @Override
    @Transactional
    public void eliminarRegistroEmpleado(Turno turno, Long empleadoId) {
        // buscar turno empleadp
        TurnoEmpleado turnoEmpleado = turnoEmpleadoRepository.findByTurnoIdAndEmpleadoId(turno.getId(), empleadoId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Turno empleado no encontrado", empleadoId.toString()));
        if (turnoEmpleado.getHoraSalida() != null) {
            throw new BusinessException("No se puede eliminar un empleado que ya registró salida");
        }
        // validar que el turno no quede vacio
        List<Empleado> empleadosEnTurno = turnoEmpleadoRepository.findEmpleadosActivosByTurnoId(turno.getId());
        if (empleadosEnTurno.size() <= 1) {
            throw new BusinessException("El turno no puede quedar vacio");
        }
        // max 1 hora
        LocalDateTime limiteEdicion = LocalDateTime.now().minusHours(1);

        if (turnoEmpleado.getHoraEntrada().isBefore(limiteEdicion)) {
            throw new BusinessException("No se puede eliminar un registro con más de 1 hora de antigüedad");
        }
        // eliminar
        turnoEmpleadoRepository.delete(turnoEmpleado);
    }

    private List<Empleado> filtroTurnoEmpleado(TurnoEmpleadoRequest data) {

        // validar que no hayan ids repetidos
        Set<Long> validacionIds = new HashSet<>(data.empleadoIds());
        if (validacionIds.size() != data.empleadoIds().size()) {
            throw new BusinessException("La lista de empleados contiene IDs duplicados");
        }
        // buscar empleados
        return validacionIds.stream().map(emp -> empleadoService.findById(emp)).toList();

    }

    @Override
    public void registarCierreTurno(Turno turno) {
        List<TurnoEmpleado> sinSalida = turnoEmpleadoRepository
                .findByTurnoIdAndHoraSalidaIsNull(turno.getId());

        sinSalida.forEach(te -> te.setHoraSalida(LocalDateTime.now()));
        turnoEmpleadoRepository.saveAll(sinSalida);
    }

    @Override
    public List<Empleado> listarEmpleadosEnTurno(Long turnoId) {
        return turnoEmpleadoRepository.findEmpleadosActivosByTurnoId(turnoId);
    }

    @Override
    public List<Empleado> listarEmpleadosFueraDeTurno(Long turnoId) {
        return turnoEmpleadoRepository.findEmpleadosSinRegistroEnTurno(turnoId);
    }

}
