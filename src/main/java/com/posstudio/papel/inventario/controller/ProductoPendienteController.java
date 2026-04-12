package com.posstudio.papel.inventario.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.posstudio.papel.common.responsive.ApiResponse;
import com.posstudio.papel.inventario.dto.request.ProductoPendienteRequestDTO;
import com.posstudio.papel.inventario.dto.request.ProductoRequestDTO;
import com.posstudio.papel.inventario.dto.responsive.ProductoPendienteResponsiveDTO;
import com.posstudio.papel.inventario.service.ProductoPendienteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/productos_pendientes")
@PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
@RequiredArgsConstructor
public class ProductoPendienteController {
    private final ProductoPendienteService productoPendienteService;

    @PostMapping()
    public ResponseEntity<ApiResponse<ProductoPendienteResponsiveDTO>> crearProductoPendiente(
            @Valid @RequestBody ProductoPendienteRequestDTO data) {

        return ResponseEntity.ok(ApiResponse.ok(productoPendienteService.crearProductoPendiente(data)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> ajustarProductoPendiente(@PathVariable Long id,
            @Valid @RequestBody ProductoRequestDTO data) {
        productoPendienteService.ajustarProductoPendiente(id, data);

        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<ProductoPendienteResponsiveDTO>>> listarProductosPendientes() {
        return ResponseEntity.ok(ApiResponse.ok(productoPendienteService.listarProductosPendientes()));
    }

}
