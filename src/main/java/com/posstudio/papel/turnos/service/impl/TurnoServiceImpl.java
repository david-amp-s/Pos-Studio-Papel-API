package com.posstudio.papel.turnos.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.posstudio.papel.common.enums.EstadoTurno;
import com.posstudio.papel.common.enums.TipoTurno;
import com.posstudio.papel.common.exception.BusinessException;
import com.posstudio.papel.common.exception.ResourceNotFoundException;
import com.posstudio.papel.turnos.dto.request.TurnoEmpleadoRequest;
import com.posstudio.papel.turnos.dto.responsive.TurnoResponsiveDTO;
import com.posstudio.papel.turnos.model.Turno;
import com.posstudio.papel.turnos.repository.TurnoRepository;
import com.posstudio.papel.turnos.service.TurnoEmpleadoService;
import com.posstudio.papel.turnos.service.TurnoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TurnoServiceImpl implements TurnoService {
    private final TurnoRepository turnoRepository;
    private final TurnoEmpleadoService turnoEmpleadoService;

    private TurnoResponsiveDTO conversorDTO(Turno turno) {
        return new TurnoResponsiveDTO(turno.getId(), turno.getFecha(), turno.getTipoTurno(), turno.getEstadoTurno(),
                turno.getDineroApertura(),
                turno.getDineroCierre(), turno.getDiferencia(), turno.getFechaApertura(), turno.getFechaCierre());
    }

    @Override
    public TurnoResponsiveDTO crearTurno(TurnoEmpleadoRequest data) {
        // buscar no haya turno abierto
        if (turnoRepository.findByEstadoTurno(EstadoTurno.ABIERTO).isPresent()) {
            throw new BusinessException("Ya hay un turno activo");
        }
        LocalDate fecha = LocalDate.now();
        TipoTurno tipoTurno = verificacionHorario(fecha);
        LocalDateTime fechaApertura = LocalDateTime.now();
        Turno turno = Turno.builder()
                .fecha(fecha)
                .tipoTurno(tipoTurno)
                .estadoTurno(EstadoTurno.ABIERTO)
                .dineroApertura(BigDecimal.valueOf(
                        200000))
                .fechaApertura(fechaApertura)
                .build();
        turnoRepository.save(turno);
        turnoEmpleadoService.crearTurnoEmpleado(data, turno.getId());
        return conversorDTO(turno);
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
    public TurnoResponsiveDTO editarTurno(Long turnoId, TurnoEmpleadoRequest data) {
        // validar que el accion turno no sea null
        if (data.tipoAccionTurno() == null) {
            throw new ResourceNotFoundException("La accion no puede ser null", null);
        }

        // buscar turno
        Turno turno = buscarTurnoId(turnoId);

        // hacer un listado de opciones:
        switch (data.tipoAccionTurno()) {
            // añadir empleados
            case INGRESO:
                turnoEmpleadoService.crearTurnoEmpleado(data, turnoId);
                break;
            // generar salida de empleado
            case SALIDA:
                turnoEmpleadoService.registrarSalidaEmpleado(data, turnoId);
                break;
            // eliminar registro empleado
            case ELIMINAR:
                turnoEmpleadoService.eliminarRegistroEmpleado(turnoId);
                break;
            default:
                throw new BusinessException("Acción inválida");
        }
        return conversorDTO(turno);
    }

    @Override
    public TurnoResponsiveDTO cerrarTurno(Long turnoId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cerrarTurno'");
    }
}
