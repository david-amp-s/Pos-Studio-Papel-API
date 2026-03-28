package com.posstudio.papel.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.posstudio.papel.dto.request.UbicacionRequestDTO;
import com.posstudio.papel.dto.responsive.UbicacionResponsiveDTO;
import com.posstudio.papel.service.UbicacionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/ubicacion")
@PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
@RequiredArgsConstructor
public class UbicacionController {
    private final UbicacionService ubicacionService;

    @PostMapping("")
    public UbicacionResponsiveDTO crearUbicacion(@Valid @RequestBody UbicacionRequestDTO data) {

        return ubicacionService.crearUbicacion(data);
    }

    @PutMapping("/{id}")
    public UbicacionResponsiveDTO editarUbicacion(@Valid 
        @PathVariable Long id, @RequestBody UbicacionRequestDTO data) {

        return ubicacionService.editarUbicacion(id, data);
    }
}
