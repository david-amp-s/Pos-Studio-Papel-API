package com.posstudio.papel.ventas.service;

import com.posstudio.papel.ventas.dto.request.DetalleVentaRequestDTO;
import com.posstudio.papel.ventas.dto.responsive.DetalleVentaResponsiveDTO;
import com.posstudio.papel.ventas.model.Venta;

public interface DetalleVentaService {
    DetalleVentaResponsiveDTO crearDetalleVenta(DetalleVentaRequestDTO data, Venta venta);

    DetalleVentaResponsiveDTO editarDetalleVenta(DetalleVentaRequestDTO data, Long id);

    DetalleVentaRequestDTO eliminarDetalleventa(Long id);
}
