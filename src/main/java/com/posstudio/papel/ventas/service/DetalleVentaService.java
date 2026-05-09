package com.posstudio.papel.ventas.service;

import java.util.List;

import com.posstudio.papel.ventas.dto.request.DetalleVentaRequestDTO;
import com.posstudio.papel.ventas.dto.request.EditarDetalleVentaRequestDTO;
import com.posstudio.papel.ventas.dto.responsive.DetalleVentaResponsiveDTO;

public interface DetalleVentaService {
    DetalleVentaResponsiveDTO crearDetalleVenta(DetalleVentaRequestDTO data, Long ventaId);

    DetalleVentaResponsiveDTO editarDetalleVenta(EditarDetalleVentaRequestDTO data, Long detalleVentaId, Long ventaId);

    void eliminarDetalleVenta(Long detalleVentaId, Long ventaId);

    DetalleVentaResponsiveDTO añadirDetalleVentaEnUno(Long ventaId, Long detalleVentaId);

    DetalleVentaResponsiveDTO eliminarDetalleVentaEnUno(Long ventaId, Long detalleVentaId);

    List<DetalleVentaResponsiveDTO> listarDetalleVentaEnVenta(Long ventaId);
}
