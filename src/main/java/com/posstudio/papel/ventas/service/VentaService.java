package com.posstudio.papel.ventas.service;

import java.util.List;

import com.posstudio.papel.ventas.dto.request.DetalleVentaRequestDTO;
import com.posstudio.papel.ventas.dto.request.EditarDetalleVentaRequestDTO;
import com.posstudio.papel.ventas.dto.request.PagoVentaRequestDTO;
import com.posstudio.papel.ventas.dto.responsive.VentaResponsiveDTO;

public interface VentaService {
    // crud
    VentaResponsiveDTO crearVenta();

    List<VentaResponsiveDTO> listarVentasActivas();

    List<VentaResponsiveDTO> listarVentasCerradas();

    void cancelarVenta(Long ventaId);

    // flujo

    VentaResponsiveDTO añadirDetalleventa(Long ventaId, DetalleVentaRequestDTO data);

    VentaResponsiveDTO editarDetalleventa(Long detalleVentaId, EditarDetalleVentaRequestDTO data, Long ventaId);

    VentaResponsiveDTO eliminarDetalleVenta(Long ventaId, Long detalleVentaId);

    VentaResponsiveDTO cerrarVenta(Long ventaId, PagoVentaRequestDTO pago);

    VentaResponsiveDTO añadirDetalleVentaEnUno(Long ventaId, Long detalleVentaId);

    VentaResponsiveDTO eliminarDetalleVentaEnUno(Long ventaId, Long detalleVentaId);
}
