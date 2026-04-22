package com.posstudio.papel.turnos.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.posstudio.papel.common.responsive.ApiResponse;
import com.posstudio.papel.turnos.dto.request.TurnoEmpleadoRequest;
import com.posstudio.papel.turnos.dto.responsive.EmpleadoResponsiveDTO;
import com.posstudio.papel.turnos.dto.responsive.TurnoResponsiveDTO;
import com.posstudio.papel.turnos.service.TurnoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/turnos")
@PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
@RequiredArgsConstructor
public class TurnoController {
    private final TurnoService turnoService;

    @PostMapping()
    public ResponseEntity<ApiResponse<TurnoResponsiveDTO>> crearTurno(@Valid @RequestBody TurnoEmpleadoRequest data) {

        return ResponseEntity.ok(ApiResponse.ok(turnoService.crearTurno(data)));
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<TurnoResponsiveDTO>> cerrarTurno(@PathVariable Long id) {

        return ResponseEntity.ok(ApiResponse.ok(turnoService.cerrarTurno(id)));
    }

    @PutMapping("editar/{id}")
    public ResponseEntity<ApiResponse<TurnoResponsiveDTO>> editarTurno(@PathVariable Long id,
            @Valid @RequestBody TurnoEmpleadoRequest data) {

        return ResponseEntity.ok(ApiResponse.ok(turnoService.editarTurno(id, data)));
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<TurnoResponsiveDTO>> obtenerTurno() {
        return ResponseEntity.ok(ApiResponse.ok(turnoService.obtenerTurnoActivo()));
    }

    @GetMapping("ingresados")
    public ResponseEntity<ApiResponse<List<EmpleadoResponsiveDTO>>> listarEmpleadosIngresados() {
        return ResponseEntity.ok(ApiResponse.ok(turnoService.empleadoEnTurno()));
    }

    @GetMapping("no_ingresados")
    public ResponseEntity<ApiResponse<List<EmpleadoResponsiveDTO>>> listarEmpleadosNoIngresados() {
        return ResponseEntity.ok(ApiResponse.ok(turnoService.empleadosAfueraTurno()));
    }

}
