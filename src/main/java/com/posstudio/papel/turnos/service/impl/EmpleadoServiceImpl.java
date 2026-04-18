package com.posstudio.papel.turnos.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.posstudio.papel.common.exception.BusinessException;
import com.posstudio.papel.common.exception.ResourceNotFoundException;
import com.posstudio.papel.turnos.dto.request.EmpleadoRequestDTO;
import com.posstudio.papel.turnos.dto.responsive.EmpleadoResponsiveDTO;
import com.posstudio.papel.turnos.model.Empleado;
import com.posstudio.papel.turnos.repository.EmpleadoRepository;
import com.posstudio.papel.turnos.service.EmpleadoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmpleadoServiceImpl implements EmpleadoService {
    private final EmpleadoRepository empleadoRepository;

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
        empleado.setNombre(data.nombre());
        empleadoRepository.save(empleado);
        return conversorDTO(empleado);
    }

    @Override
    public void desactivarEmpleado(Long id) {
        Empleado empleado = findById(id);
        empleado.setActivo(false);
        empleadoRepository.save(empleado);
    }

    public  Empleado findById(Long id) {
        return empleadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado", id.toString()));
    }

    @Override
    public List<EmpleadoResponsiveDTO> listarEmpleados() {
        return empleadoRepository.findByActivo(true).stream().map(this::conversorDTO).toList();
    }
}
