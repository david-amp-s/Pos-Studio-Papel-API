package com.posstudio.papel.ventas.service;

import java.util.List;

import com.posstudio.papel.ventas.dto.request.PagoVentaRequestDTO;
import com.posstudio.papel.ventas.dto.responsive.PagoVentaResponsiveDTO;
import com.posstudio.papel.ventas.model.Venta;

public interface PagoVentaService {
    List<PagoVentaResponsiveDTO> añadirPago(PagoVentaRequestDTO data, Venta venta);

    PagoVentaResponsiveDTO listarPagos(Venta venta);

}
