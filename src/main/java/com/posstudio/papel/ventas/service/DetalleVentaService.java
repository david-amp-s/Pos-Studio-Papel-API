package com.posstudio.papel.ventas.service;

import com.posstudio.papel.ventas.dto.request.DetalleVentaRequestDTO;
import com.posstudio.papel.ventas.dto.request.EditarDetalleVentaRequestDTO;
import com.posstudio.papel.ventas.dto.responsive.DetalleVentaResponsiveDTO;
import com.posstudio.papel.ventas.model.Venta;

public interface DetalleVentaService {
    DetalleVentaResponsiveDTO crearDetalleVenta(DetalleVentaRequestDTO data, Venta venta);

    DetalleVentaResponsiveDTO editarDetalleVenta(EditarDetalleVentaRequestDTO data, Long detalleVentaId, Venta venta);

    void eliminarDetalleVenta(Long detalleVentaId, Venta venta);

    DetalleVentaResponsiveDTO añadirDetalleVentaEnUno(Venta venta, Long detalleVentaId);

    DetalleVentaResponsiveDTO eliminarDetalleVentaEnUno(Venta venta, Long detalleVentaId);
}
