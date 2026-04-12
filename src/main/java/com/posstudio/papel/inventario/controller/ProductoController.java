package com.posstudio.papel.inventario.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.posstudio.papel.common.responsive.ApiResponse;
import com.posstudio.papel.inventario.dto.request.ProductoRequestDTO;
import com.posstudio.papel.inventario.dto.responsive.ProductoResponsiveDTO;
import com.posstudio.papel.inventario.service.ProductoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/productos")
@PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
@RequiredArgsConstructor
public class ProductoController {
    private final ProductoService productoService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductoResponsiveDTO>> crearProducto(
            @Valid @RequestBody ProductoRequestDTO data) {
        return ResponseEntity.ok(ApiResponse.ok(productoService.crearProducto(data)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductoResponsiveDTO>>> listarProducto() {
        return ResponseEntity.ok(ApiResponse.ok(productoService.listarProducto()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductoResponsiveDTO>> editarProducto(@PathVariable Long id,
            @Valid @RequestBody ProductoRequestDTO data) {
        return ResponseEntity.ok(ApiResponse.ok(productoService.editarProducto(id, data)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivarProducto(@PathVariable Long id) {
        productoService.desactivarProducto(id);
        return ResponseEntity.noContent().build();
    }

}
