package com.posstudio.papel.inventario.service.impl;

import org.springframework.stereotype.Service;

import com.posstudio.papel.common.enums.TipoMovimientoInventario;
import com.posstudio.papel.common.exception.BusinessException;
import com.posstudio.papel.inventario.dto.request.ProductoRequestDTO;
import com.posstudio.papel.inventario.dto.responsive.ProductoResponsiveDTO;
import com.posstudio.papel.inventario.model.Categoria;
import com.posstudio.papel.inventario.model.Producto;
import com.posstudio.papel.inventario.model.Ubicacion;
import com.posstudio.papel.inventario.repository.ProductoRepository;
import com.posstudio.papel.inventario.service.CategoriaService;
import com.posstudio.papel.inventario.service.ProductoService;
import com.posstudio.papel.inventario.service.UbicacionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {
    private final ProductoRepository productoRepository;
    private final CategoriaService categoriaService;
    private final UbicacionService ubicacionService;

    public ProductoResponsiveDTO conversorDTO(Producto producto) {
        return new ProductoResponsiveDTO(producto.getId(), producto.getNombre(), producto.getCodigoDeBarras(),
                producto.getPrecio(), producto.getStock(), producto.getCategoria().getNombre(),
                producto.getUbicacion().getCodigo(), producto.getUnidadNegocio().name());
    }

    @Override
    public ProductoResponsiveDTO crearProducto(ProductoRequestDTO data) {
        if (productoRepository.findByNombre(data.nombre()).isPresent()) {
            throw new BusinessException("Producto ya existe con ese nombre", 409);
        }
        Categoria categoria = categoriaService.findByNombre(data.categoria());
        Ubicacion ubicacion = ubicacionService.findByCodigo(data.ubicacion());

        Producto producto = Producto.builder()
                .nombre(data.nombre())
                .codigoDeBarras(data.codigoDeBarras())
                .precio(data.precio())
                .stock(0)
                .categoria(categoria)
                .ubicacion(ubicacion)
                .unidadNegocio(data.unidadNegocio())
                .build();
        productoRepository.save(producto);
        ajustarStock(TipoMovimientoInventario.COMPRA, data.stock(), producto);
        return conversorDTO(producto);
    }

    @Override
    public ProductoResponsiveDTO listarProducto() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listarProducto'");
    }

    @Override
    public ProductoResponsiveDTO editarProducto(Long id, ProductoRequestDTO data) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'editarProducto'");
    }

    @Override
    public Producto findByNombre(String nombre) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByNombre'");
    }

    @Override
    public ProductoResponsiveDTO ajustarStock(TipoMovimientoInventario tipoMovimiento, Integer cantidad,
            Producto producto) {
        switch (tipoMovimiento) {
            case COMPRA:
                if (cantidad < 0) {
                    throw new BusinessException("La cantidad de productos no puede ser negativa");
                }
                Producto productoAjustar = producto;
                productoAjustar.setStock(cantidad);
                productoRepository.save(productoAjustar);
                return conversorDTO(productoAjustar);
            case VENTA:
                if (producto.getStock() < cantidad) {
                    System.out.println("DEBE DE AJUSTARSE INVENTARIO");

                }
            default:
                break;
        }
    }

    @Override
    public void desactivarProducto() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'desactivarProducto'");
    }

}
