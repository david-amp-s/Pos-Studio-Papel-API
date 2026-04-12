package com.posstudio.papel.inventario.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.posstudio.papel.common.responsive.ApiResponse;
import com.posstudio.papel.inventario.dto.request.CategoriaResquestDTO;
import com.posstudio.papel.inventario.dto.responsive.CategoriaResponsiveDTO;
import com.posstudio.papel.inventario.service.CategoriaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/categorias")
@PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
@RequiredArgsConstructor
public class CategoriaController {
    private final CategoriaService categoriaService;

    @PostMapping
    public ResponseEntity<ApiResponse<CategoriaResponsiveDTO>> crearCategoria(
            @Valid @RequestBody CategoriaResquestDTO data) {
        return ResponseEntity.ok(ApiResponse.ok(categoriaService.crearCategoria(data)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoriaResponsiveDTO>> editarCategoria(@PathVariable Long id,
            @Valid @RequestBody CategoriaResquestDTO data) {
        return ResponseEntity.ok(ApiResponse.ok(categoriaService.editarCategoria(id, data)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoriaResponsiveDTO>>> listaCategoria() {
        return ResponseEntity.ok(ApiResponse.ok(categoriaService.listaCategoria()));
    }

}
