package com.posstudio.papel.service;

import com.posstudio.papel.dto.request.ProductoRequestDTO;
import com.posstudio.papel.dto.responsive.ProductoResponsiveDTO;

public interface ProductoSerive {
    ProductoResponsiveDTO crearProducto(ProductoRequestDTO producto);

    ProductoResponsiveDTO listarProducto();

    ProductoResponsiveDTO editarProducto(Long id, ProductoRequestDTO producto);

    void eliminarProducto();

}
