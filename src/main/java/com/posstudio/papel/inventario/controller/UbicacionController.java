package com.posstudio.papel.inventario.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.posstudio.papel.common.responsive.ApiResponse;
import com.posstudio.papel.inventario.dto.request.UbicacionRequestDTO;
import com.posstudio.papel.inventario.dto.responsive.UbicacionResponsiveDTO;
import com.posstudio.papel.inventario.service.UbicacionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/ubicacion")
@PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
@RequiredArgsConstructor
public class UbicacionController {
    private final UbicacionService ubicacionService;

    @PostMapping("")
    public ResponseEntity<ApiResponse<UbicacionResponsiveDTO>> crearUbicacion(
            @Valid @RequestBody UbicacionRequestDTO data) {

        return ResponseEntity.ok(ApiResponse.ok(ubicacionService.crearUbicacion(data)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UbicacionResponsiveDTO>> editarUbicacion(@Valid @PathVariable Long id,
            @RequestBody UbicacionRequestDTO data) {

        return ResponseEntity.ok(ApiResponse.ok(ubicacionService.editarUbicacion(id, data)));
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<UbicacionResponsiveDTO>>> listarUbicaciones() {
        return ResponseEntity.ok(ApiResponse.ok(ubicacionService.listarUbicaciones()));
    }

}
