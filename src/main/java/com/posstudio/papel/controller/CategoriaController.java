package com.posstudio.papel.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.posstudio.papel.dto.request.CategoriaResquestDTO;
import com.posstudio.papel.dto.responsive.CategoriaResponsiveDTO;
import com.posstudio.papel.service.CategoriaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/categoria")
@PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
@RequiredArgsConstructor
public class CategoriaController {
    private final CategoriaService categoriaService;

    @PostMapping()
    public CategoriaResponsiveDTO crearCategoria(@Valid @RequestBody CategoriaResquestDTO data) {
        return categoriaService.crearCategoria(data);
    }

    @PutMapping("/{id}")
    public CategoriaResponsiveDTO editarCategoria(@PathVariable Long id,
            @Valid @RequestBody CategoriaResquestDTO data) {
        return categoriaService.editarCategoria(id, data);
    }
}
