package com.posstudio.papel.inventario.service;

import java.util.List;

import com.posstudio.papel.common.enums.TipoMovimientoInventario;
import com.posstudio.papel.inventario.dto.request.ProductoRequestDTO;
import com.posstudio.papel.inventario.dto.responsive.ProductoResponsiveDTO;
import com.posstudio.papel.inventario.model.Producto;

public interface ProductoService {
    Producto findByNombre(String nombre);

    ProductoResponsiveDTO crearProducto(ProductoRequestDTO data);

    List<ProductoResponsiveDTO> listarProducto();

    ProductoResponsiveDTO editarProducto(Long id, ProductoRequestDTO data);

    ProductoResponsiveDTO ajustarStock(TipoMovimientoInventario tipoMovimiento, Integer cantidad, Producto producto,
            Long referencia);

    Producto findByid(Long id);

    void desactivarProducto(Long id);

    void validarProductoNoExiste(String nombre);
}
