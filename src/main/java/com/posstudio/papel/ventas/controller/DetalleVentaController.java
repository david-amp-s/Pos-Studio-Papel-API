package com.posstudio.papel.ventas.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.posstudio.papel.common.responsive.ApiResponse;
import com.posstudio.papel.ventas.dto.request.DetalleVentaRequestDTO;
import com.posstudio.papel.ventas.dto.request.EditarDetalleVentaRequestDTO;
import com.posstudio.papel.ventas.dto.responsive.DetalleVentaResponsiveDTO;
import com.posstudio.papel.ventas.service.DetalleVentaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/detalle_venta")
@PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
@RequiredArgsConstructor
public class DetalleVentaController {
    private final DetalleVentaService detalleVentaService;

    @PostMapping("{ventaId}")
    public ResponseEntity<ApiResponse<DetalleVentaResponsiveDTO>> añadirDetalleVenta(
            @Valid @RequestBody DetalleVentaRequestDTO data,
            @PathVariable Long ventaId) {
        return ResponseEntity.ok(ApiResponse.ok(detalleVentaService.crearDetalleVenta(data, ventaId)));
    }

    @PutMapping("{ventaId}/{detalleVentaId}")
    public ResponseEntity<ApiResponse<DetalleVentaResponsiveDTO>> editarDetalleVenta(
            @PathVariable Long ventaId, @PathVariable Long detalleVentaId,
            @Valid @RequestBody EditarDetalleVentaRequestDTO data) {
        return ResponseEntity.ok(ApiResponse.ok(detalleVentaService.editarDetalleVenta(data, detalleVentaId, ventaId)));
    }

    @DeleteMapping("/{ventaId}/{detalleVentaId}")
    public ResponseEntity<Void> eliminarDetalleVenta(@PathVariable Long ventaId,
            @PathVariable Long detalleVentaId) {
        detalleVentaService.eliminarDetalleVenta(ventaId, detalleVentaId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/accion/add/{ventaId}/{detalleVentaId}")
    public ResponseEntity<ApiResponse<DetalleVentaResponsiveDTO>> añadirDetalleVentaEnUno(@PathVariable Long ventaId,
            @PathVariable Long detalleVentaId) {
        return ResponseEntity.ok(ApiResponse.ok(detalleVentaService.añadirDetalleVentaEnUno(ventaId, detalleVentaId)));
    }

    @PostMapping("/accion/del/{ventaId}/{detalleVentaId}")
    public ResponseEntity<ApiResponse<DetalleVentaResponsiveDTO>> eliminarDetalleVentaEnUno(@PathVariable Long ventaId,
            @PathVariable Long detalleVentaId) {
        return ResponseEntity
                .ok(ApiResponse.ok(detalleVentaService.eliminarDetalleVentaEnUno(ventaId, detalleVentaId)));
    }
}