package com.posstudio.papel.service;

import com.posstudio.papel.dto.request.ProductoRequestDTO;
import com.posstudio.papel.dto.responsive.ProductoResponsiveDTO;
import com.posstudio.papel.model.Producto;

public interface ProductoService {
    Producto findByNombre(String nombre);

    ProductoResponsiveDTO crearProducto(ProductoRequestDTO data);

    ProductoResponsiveDTO listarProducto();

    ProductoResponsiveDTO editarProducto(Long id, ProductoRequestDTO data);

    void eliminarProducto();

}
