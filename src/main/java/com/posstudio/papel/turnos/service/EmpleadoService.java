package com.posstudio.papel.turnos.service;

import java.util.List;

import com.posstudio.papel.turnos.dto.request.EmpleadoRequestDTO;
import com.posstudio.papel.turnos.dto.responsive.EmpleadoResponsiveDTO;
import com.posstudio.papel.turnos.model.Empleado;

public interface EmpleadoService {
    EmpleadoResponsiveDTO crearEmpleado(EmpleadoRequestDTO data);

    EmpleadoResponsiveDTO editarEmpleado(EmpleadoRequestDTO data, Long id);

    List<EmpleadoResponsiveDTO> listarEmpleados();

    void desactivarEmpleado(Long id);

    Empleado findById(Long id);

}
