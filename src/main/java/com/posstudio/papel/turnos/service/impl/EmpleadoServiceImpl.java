package com.posstudio.papel.turnos.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.posstudio.papel.common.exception.BusinessException;
import com.posstudio.papel.common.exception.ResourceNotFoundException;
import com.posstudio.papel.turnos.dto.request.EmpleadoRequestDTO;
import com.posstudio.papel.turnos.dto.responsive.EmpleadoResponsiveDTO;
import com.posstudio.papel.turnos.model.Empleado;
import com.posstudio.papel.turnos.repository.EmpleadoRepository;
import com.posstudio.papel.turnos.repository.TurnoEmpleadoRepository;
import com.posstudio.papel.turnos.service.EmpleadoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EmpleadoServiceImpl implements EmpleadoService {
    private final EmpleadoRepository empleadoRepository;
    private final TurnoEmpleadoRepository turnoEmpleadoRepository;

    private EmpleadoResponsiveDTO conversorDTO(Empleado empleado) {
        return new EmpleadoResponsiveDTO(empleado.getId(), empleado.getNombre());
    }

    @Override
    public EmpleadoResponsiveDTO crearEmpleado(EmpleadoRequestDTO data) {
        if (empleadoRepository.findByNombre(data.nombre()).isPresent()) {
            throw new BusinessException("Empleado ya existe con ese nombre", 409);
        }

        Empleado empleado = Empleado.builder()
                .nombre(data.nombre())
                .activo(true)
                .build();

        empleadoRepository.save(empleado);
        return conversorDTO(empleado);
    }

    @Override
    public EmpleadoResponsiveDTO editarEmpleado(EmpleadoRequestDTO data, Long id) {
        Empleado empleado = findById(id);

        if (!empleado.getNombre().equals(data.nombre())) {
            empleadoRepository.findByNombre(data.nombre())
                    .ifPresent(existente -> {
                        throw new BusinessException("Ya existe un empleado con el nombre: " + data.nombre(), 409);
                    });
            empleado.setNombre(data.nombre());
        }

        return conversorDTO(empleado);
    }

    @Override
    public void desactivarEmpleado(Long id) {
        if (turnoEmpleadoRepository.existsEmpleadoActivoEnTurnoAbierto(id)) {
            throw new BusinessException(
                    "No se puede desactivar el empleado porque tiene un turno activo sin cerrar", 409);
        }

        Empleado empleado = findById(id);
        empleado.setActivo(false);

    }

    @Override
    public Empleado findById(Long id) {
        return empleadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado", id.toString()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpleadoResponsiveDTO> listarEmpleados() {
        return empleadoRepository.findByActivo(true).stream()
                .map(this::conversorDTO)
                .toList();
    }
}
