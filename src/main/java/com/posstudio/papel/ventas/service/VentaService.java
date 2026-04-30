package com.posstudio.papel.ventas.service;

import java.util.List;

import com.posstudio.papel.ventas.dto.responsive.VentaResponsiveDTO;

public interface VentaService {
    // crud
    VentaResponsiveDTO crearVenta();

    List<VentaResponsiveDTO> listarVentasActivas();

    List<VentaResponsiveDTO> listarVentasCerradas();

    void cancelarVenta(Long ventaId);

    // flujo

    List<VentaResponsiveDTO> añadirDetalleventa(Long ventaId);

    List<VentaResponsiveDTO> editarDetalleventa();

    List<VentaResponsiveDTO> eliminarDetalleVenta();
}
