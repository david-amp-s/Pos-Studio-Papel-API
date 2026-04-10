package com.posstudio.papel.inventario.service;

import com.posstudio.papel.inventario.dto.request.ProductoRequestDTO;
import com.posstudio.papel.inventario.dto.responsive.ProductoResponsiveDTO;
import com.posstudio.papel.inventario.model.Producto;

public interface ProductoService {
    Producto findByNombre(String nombre);

    ProductoResponsiveDTO crearProducto(ProductoRequestDTO data);

    ProductoResponsiveDTO listarProducto();

    ProductoResponsiveDTO editarProducto(Long id, ProductoRequestDTO data);

    void eliminarProducto();

}
