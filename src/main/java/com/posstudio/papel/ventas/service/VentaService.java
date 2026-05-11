package com.posstudio.papel.ventas.service;

import java.util.List;

import com.posstudio.papel.ventas.dto.request.PagoVentaRequestDTO;
import com.posstudio.papel.ventas.dto.responsive.VentaResponsiveDTO;

public interface VentaService {
    // crud
    VentaResponsiveDTO crearVenta();

    List<VentaResponsiveDTO> listarVentasActivas();

    List<VentaResponsiveDTO> listarVentasCerradas();

    void cancelarVenta(Long ventaId);

    VentaResponsiveDTO cerrarVenta(Long ventaId, PagoVentaRequestDTO pago);

}
