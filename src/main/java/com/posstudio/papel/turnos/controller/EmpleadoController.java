package com.posstudio.papel.turnos.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.posstudio.papel.common.responsive.ApiResponse;
import com.posstudio.papel.turnos.dto.request.EmpleadoRequestDTO;
import com.posstudio.papel.turnos.dto.responsive.EmpleadoResponsiveDTO;
import com.posstudio.papel.turnos.service.EmpleadoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/empleados")
@PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
@RequiredArgsConstructor
public class EmpleadoController {
    private final EmpleadoService empleadoService;

    @PostMapping()
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<EmpleadoResponsiveDTO>> crearEmpleado(
            @Valid @RequestBody EmpleadoRequestDTO data) {
        return ResponseEntity.ok(ApiResponse.ok(empleadoService.crearEmpleado(data)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<EmpleadoResponsiveDTO>> editarEmpleado(@Valid @PathVariable Long id,
            @Valid @RequestBody EmpleadoRequestDTO data) {
        return ResponseEntity.ok(ApiResponse.ok(empleadoService.editarEmpleado(data, id)));
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<EmpleadoResponsiveDTO>>> listarEmpleados() {
        return ResponseEntity.ok(ApiResponse.ok(empleadoService.listarEmpleados()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivarEmpleado(@PathVariable Long id) {
        empleadoService.desactivarEmpleado(id);
        return ResponseEntity.noContent().build();
    }

}
