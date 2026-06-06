package com.posstudio.papel.inventario.service;

import java.util.List;

import com.posstudio.papel.inventario.dto.request.ProductoPendienteRequestDTO;
import com.posstudio.papel.inventario.dto.request.ProductoRequestDTO;
import com.posstudio.papel.inventario.dto.request.ProductoServicioRequestDTO;
import com.posstudio.papel.inventario.dto.responsive.ProductoPendienteResponsiveDTO;
import com.posstudio.papel.inventario.model.ProductoPendiente;

public interface ProductoPendienteService {
    ProductoPendienteResponsiveDTO crearProductoPendiente(ProductoPendienteRequestDTO data);

    void ajustarProductoPendiente(Long id, ProductoRequestDTO data);

    void ajustarProductoServicioPendiente(Long id, ProductoServicioRequestDTO data);

    ProductoPendiente findById(Long id);

    List<ProductoPendienteResponsiveDTO> listarProductosPendientes();

    Long totalProductosPendientes();
}
