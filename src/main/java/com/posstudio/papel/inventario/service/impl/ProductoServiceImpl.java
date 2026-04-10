package com.posstudio.papel.inventario.service.impl;

import org.springframework.stereotype.Service;

import com.posstudio.papel.inventario.dto.request.ProductoRequestDTO;
import com.posstudio.papel.inventario.dto.responsive.ProductoResponsiveDTO;
import com.posstudio.papel.inventario.model.Categoria;
import com.posstudio.papel.inventario.model.Producto;
import com.posstudio.papel.inventario.repository.ProductoRepository;
import com.posstudio.papel.inventario.service.CategoriaService;
import com.posstudio.papel.inventario.service.ProductoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {
    private final ProductoRepository productoRepository;
    private final CategoriaService categoriaService;

    public ProductoResponsiveDTO conversorDTO(Producto producto) {
        return new ProductoResponsiveDTO(producto.getId(), producto.getNombre(), producto.getCodigoDeBarras(),
                producto.getPrecio(), producto.getStock(), producto.getCategoria().getNombre(),
                producto.getUbicacion().getCodigo(), producto.getUnidadNegocio().name());
    }

    @Override
    public ProductoResponsiveDTO crearProducto(ProductoRequestDTO data) {
        if (productoRepository.findByNombre(data.nombre()).isPresent()) {

        }
        Categoria categoria = categoriaService.findByNombre(data.nombre());
        Producto producto = Producto.builder()
                .nombre(data.nombre())
                .codigoDeBarras(data.codigoDeBarras())
                .precio(data.precio())
                .stock(data.stock())
                .categoria(categoria) // Arreglar aca
                .ubicacion(null) // arreglar aca
                .unidadNegocio(data.unidadNegocio())
                .build();
        productoRepository.save(producto);
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
    public void eliminarProducto() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eliminarProducto'");
    }

    @Override
    public Producto findByNombre(String nombre) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByNombre'");
    }

}
