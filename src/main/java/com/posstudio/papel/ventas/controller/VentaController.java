package com.posstudio.papel.ventas.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.posstudio.papel.common.responsive.ApiResponse;
import com.posstudio.papel.ventas.dto.request.DetalleVentaRequestDTO;
import com.posstudio.papel.ventas.dto.responsive.VentaResponsiveDTO;
import com.posstudio.papel.ventas.service.VentaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/venta")
@PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
@RequiredArgsConstructor
public class VentaController {
    private final VentaService ventaService;

    @PostMapping()
    public ResponseEntity<ApiResponse<VentaResponsiveDTO>> crearVenta() {
        return ResponseEntity.ok(ApiResponse.ok(ventaService.crearVenta()));
    }

    @GetMapping("activas")
    public ResponseEntity<ApiResponse<List<VentaResponsiveDTO>>> listarAbiertas() {
        return ResponseEntity.ok(ApiResponse.ok(ventaService.listarVentasActivas()));
    }

    @GetMapping("cerradas")
    public ResponseEntity<ApiResponse<List<VentaResponsiveDTO>>> listarCerradas() {
        return ResponseEntity.ok(ApiResponse.ok(ventaService.listarVentasCerradas()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelarVenta(@PathVariable Long id) {
        ventaService.cancelarVenta(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("añadir/{id}")
    public ResponseEntity<ApiResponse<VentaResponsiveDTO>> añadirDetalleVenta(@PathVariable Long id,
            @Valid @RequestBody DetalleVentaRequestDTO data) {
        return ResponseEntity.ok(ApiResponse.ok(ventaService.añadirDetalleventa(id, data)));
    }

    @PutMapping("editar/{detalleVentaId}/{ventaId}")
    public ResponseEntity<ApiResponse<VentaResponsiveDTO>> editarDetalleVenta(@PathVariable Long detalleVentaId,
            @Valid @RequestBody DetalleVentaRequestDTO data,
            @PathVariable Long ventaId) {
        return ResponseEntity.ok(ApiResponse.ok(ventaService.editarDetalleventa(detalleVentaId, data, ventaId)));
    }
}
