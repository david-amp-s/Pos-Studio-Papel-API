package com.posstudio.papel.turnos.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.posstudio.papel.common.enums.EstadoTurno;
import com.posstudio.papel.common.enums.TipoAccionTurno;
import com.posstudio.papel.common.enums.TipoTurno;
import com.posstudio.papel.common.exception.BusinessException;
import com.posstudio.papel.common.exception.ResourceNotFoundException;
import com.posstudio.papel.turnos.dto.request.TurnoEmpleadoRequest;
import com.posstudio.papel.turnos.dto.responsive.EmpleadoResponsiveDTO;
import com.posstudio.papel.turnos.dto.responsive.TurnoResponsiveDTO;
import com.posstudio.papel.turnos.model.Empleado;
import com.posstudio.papel.turnos.model.Turno;
import com.posstudio.papel.turnos.repository.TurnoRepository;
import com.posstudio.papel.turnos.service.TurnoEmpleadoService;
import com.posstudio.papel.turnos.service.TurnoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TurnoServiceImpl implements TurnoService {
    private final TurnoRepository turnoRepository;
    private final TurnoEmpleadoService turnoEmpleadoService;

    private TurnoResponsiveDTO conversorDTO(Turno turno) {
        return new TurnoResponsiveDTO(turno.getId(), turno.getFecha(), turno.getTipoTurno(), turno.getEstadoTurno(),
                turno.getDineroApertura(),
                turno.getDineroCierre(), turno.getDiferencia(), turno.getFechaApertura(), turno.getFechaCierre());
    }

    @Override
    @Transactional
    public TurnoResponsiveDTO crearTurno(TurnoEmpleadoRequest data) {

        if (data.tipoAccionTurno() == null || data.tipoAccionTurno() != TipoAccionTurno.INGRESO) {
            throw new BusinessException("Tipo accion debe ser ingreso");
        }
        // buscar no haya turno abierto
        if (turnoRepository.findByEstadoTurno(EstadoTurno.ABIERTO).isPresent()) {
            throw new BusinessException("Ya hay un turno activo");
        }

        LocalDate fecha = LocalDate.now();
        TipoTurno tipoTurno = verificacionHorario(fecha);

        Turno turno = Turno.builder()
                .fecha(fecha)
                .tipoTurno(tipoTurno)
                .estadoTurno(EstadoTurno.ABIERTO)
                .dineroApertura(BigDecimal.valueOf(
                        200000))
                .fechaApertura(LocalDateTime.now())
                .build();

        if (data.empleadoIds() == null || data.empleadoIds().isEmpty()) {
            throw new BusinessException("Debe asignar al menos un empleado al turno");
        }
        Turno turnoGuardado = turnoRepository.save(turno);
        turnoEmpleadoService.crearTurnoEmpleado(data, turnoGuardado);
        return conversorDTO(turnoGuardado);
    }

    private TipoTurno verificacionHorario(LocalDate fecha) {

        boolean existeManana = turnoRepository
                .existsByFechaAndTipoTurno(fecha, TipoTurno.MANANA);

        if (!existeManana) {
            return TipoTurno.MANANA;
        }

        boolean existeTarde = turnoRepository
                .existsByFechaAndTipoTurno(fecha, TipoTurno.TARDE);

        if (existeTarde) {
            throw new BusinessException("Ya se registraron los turnos de mañana y tarde del día");
        }

        return TipoTurno.TARDE;
    }

    @Override
    public Turno buscarTurnoId(Long id) {
        return turnoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Turno", id.toString()));
    }

    @Override
    @Transactional
    public TurnoResponsiveDTO editarTurno(TurnoEmpleadoRequest data) {
        // validar que el accion turno no sea null
        if (data.tipoAccionTurno() == null) {
            throw new ResourceNotFoundException("La accion no puede ser null", null);
        }
        // buscar turno y validar
        Turno turno = turnoRepository.findByEstadoTurno(EstadoTurno.ABIERTO)
                .orElseThrow(() -> new BusinessException("No hay turnos activos"));
        if (turno.getEstadoTurno() != EstadoTurno.ABIERTO) {
            throw new BusinessException("El turno debe de estar abierto para registrar empleados");
        }

        // hacer un listado de opciones:
        switch (data.tipoAccionTurno()) {
            // añadir empleados
            case INGRESO ->
                turnoEmpleadoService.crearTurnoEmpleado(data, turno);

            // generar salida de empleado
            case SALIDA -> turnoEmpleadoService.registrarSalidaEmpleado(data, turno);
            // eliminar registro empleado
            case ELIMINAR -> turnoEmpleadoService.eliminarRegistroEmpleado(turno, data.empleadoIds().getFirst());
            default -> throw new BusinessException("Acción inválida");
        }
        return conversorDTO(turno);
    }

    @Override
    @Transactional
    public TurnoResponsiveDTO cerrarTurno() {
        // validar que el turno este abierto
        Turno turno = turnoRepository.findByEstadoTurno(EstadoTurno.ABIERTO)
                .orElseThrow(() -> new BusinessException("No hay turno activo"));

        if (turno.getEstadoTurno() != EstadoTurno.ABIERTO) {
            throw new BusinessException("El turno ya se encuentra cerrado");
        }

        // validar que haya al menos un empleado en turno
        List<Empleado> empleadosActivos = turnoEmpleadoService.listarEmpleadosEnTurno(turno.getId());

        if (empleadosActivos.isEmpty()) {
            throw new BusinessException("No se puede cerrar un turno sin empleados");
        }
        // salida de empleados
        turnoEmpleadoService.registarCierreTurno(turno);
        // en modulo de ventas no puede haber una venta pendiente
        // registrar dinero cierre (Donde si dinero cierre <= 0 y lo traeremos del
        // modulo de ventas ) registraremos un log pero todavia no esta en el mvp

        turno.setEstadoTurno(EstadoTurno.CERRADO);
        turno.setDineroCierre(BigDecimal.ZERO);
        turno.setDiferencia(BigDecimal.ZERO);
        turno.setFechaCierre(LocalDateTime.now());
        // cerrar turno
        turnoRepository.save(turno);
        return conversorDTO(turno);
    }

    public TurnoResponsiveDTO obtenerTurnoActivo() {
        return turnoRepository.findByEstadoTurno(EstadoTurno.ABIERTO)
                .map(this::conversorDTO)
                .orElse(null);
    }

    @Override
    public List<EmpleadoResponsiveDTO> empleadoEnTurno() {
        Turno turno = turnoRepository.findByEstadoTurno(EstadoTurno.ABIERTO)
                .orElseThrow(() -> new BusinessException("No hay turno abierto"));
        return turnoEmpleadoService.listarEmpleadosEnTurno(turno.getId()).stream()
                .map(emp -> new EmpleadoResponsiveDTO(emp.getId(), emp.getNombre())).toList();
    }

    @Override
    public List<EmpleadoResponsiveDTO> empleadosAfueraTurno() {
        Turno turno = turnoRepository.findByEstadoTurno(EstadoTurno.ABIERTO)
                .orElseThrow(() -> new BusinessException("No hay turno abierto"));
        return turnoEmpleadoService.listarEmpleadosFueraDeTurno(turno.getId()).stream()
                .map(emp -> new EmpleadoResponsiveDTO(emp.getId(), emp.getNombre())).toList();
    }
}
