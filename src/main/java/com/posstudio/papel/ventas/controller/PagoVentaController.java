package com.posstudio.papel.ventas.controller;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.posstudio.papel.common.responsive.ApiResponse;
import com.posstudio.papel.ventas.service.PagoVentaService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/pago_venta")
@PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
@RequiredArgsConstructor
public class PagoVentaController {
    private final PagoVentaService pagoVentaService;

    @GetMapping("total/{turnoId}")
    public ResponseEntity<ApiResponse<BigDecimal>> calcularTotalPagosEnVenta(@PathVariable Long turnoId) {
        return ResponseEntity.ok(ApiResponse.ok(pagoVentaService.calcularTotalPagosEnVenta(turnoId)));
    }

    @GetMapping("total/efectivo/{turnoId}")
    public ResponseEntity<ApiResponse<BigDecimal>> calcularPagosEnEfectivo(@PathVariable Long turnoId) {
        return ResponseEntity.ok(ApiResponse.ok(pagoVentaService.calcularPagosEnEfectivo(turnoId)));
    }

    @GetMapping("total/transf/{turnoId}")
    public ResponseEntity<ApiResponse<BigDecimal>> calcularPagosEnTransf(@PathVariable Long turnoId) {
        return ResponseEntity.ok(ApiResponse.ok(pagoVentaService.calcularPagosEnTransf(turnoId)));
    }

    @GetMapping("total/tarjeta/{turnoId}")
    public ResponseEntity<ApiResponse<BigDecimal>> calcularPagosEnTarjeta(@PathVariable Long turnoId) {
        return ResponseEntity.ok(ApiResponse.ok(pagoVentaService.calcularPagosEnTarjeta(turnoId)));
    }

}
