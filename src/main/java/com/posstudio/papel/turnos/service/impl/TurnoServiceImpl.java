package com.posstudio.papel.turnos.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.posstudio.papel.common.enums.EstadoTurno;
import com.posstudio.papel.common.enums.TipoTurno;
import com.posstudio.papel.common.exception.BusinessException;
import com.posstudio.papel.common.exception.ResourceNotFoundException;
import com.posstudio.papel.turnos.dto.request.TurnoEmpleadoRequest;
import com.posstudio.papel.turnos.dto.request.TurnoRequestDTO;
import com.posstudio.papel.turnos.dto.responsive.TurnoResponsiveDTO;
import com.posstudio.papel.turnos.model.Turno;
import com.posstudio.papel.turnos.repository.TurnoEmpleadoRepository;
import com.posstudio.papel.turnos.repository.TurnoRepository;
import com.posstudio.papel.turnos.service.TurnoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TurnoServiceImpl implements TurnoService {
    private final TurnoRepository turnoRepository;
    private final TurnoEmpleadoRepository turnoEmpleadoRepository;

    @Override
    public TurnoResponsiveDTO crearTurno(TurnoEmpleadoRequest data) {
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
                .dineroApertura(200000)
                .dineroCierre(null)
                .diferencia(null)
                .fecha_apertura(fechaApertura)
                .fecha_cierre()
                .build();

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
}
