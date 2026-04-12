package com.posstudio.papel.inventario.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.posstudio.papel.common.exception.ResourceNotFoundException;
import com.posstudio.papel.inventario.dto.request.ProductoPendienteRequestDTO;
import com.posstudio.papel.inventario.dto.request.ProductoRequestDTO;
import com.posstudio.papel.inventario.dto.responsive.ProductoPendienteResponsiveDTO;
import com.posstudio.papel.inventario.model.ProductoPendiente;
import com.posstudio.papel.inventario.repository.ProductoPendienteRepository;
import com.posstudio.papel.inventario.service.ProductoPendienteService;
import com.posstudio.papel.inventario.service.ProductoService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoPendienteServiceImpl implements ProductoPendienteService {
    private final ProductoPendienteRepository productoPendienteRepository;
    private final ProductoService productoService;

    private ProductoPendienteResponsiveDTO conversorDTO(ProductoPendiente producto) {
        return new ProductoPendienteResponsiveDTO(producto.getId(), producto.getNombre(), producto.getPrecio(),
                producto.getRegistrado());
    }

    @Override
    public ProductoPendienteResponsiveDTO crearProductoPendiente(ProductoPendienteRequestDTO data) {
        productoService.validarProductoNoExiste(data.nombre());
        ProductoPendiente productoPendiente = ProductoPendiente.builder()
                .nombre(data.nombre())
                .precio(data.precio())
                .registrado(false)
                .build();
        productoPendienteRepository.save(productoPendiente);

        return conversorDTO(productoPendiente);
    }

    @Transactional
    @Override
    public void ajustarProductoPendiente(Long id, ProductoRequestDTO data) {
        ProductoPendiente productoPendiente = findById(id);
        productoService.crearProducto(data);
        productoPendienteRepository.delete(productoPendiente);
    }

    @Override
    public ProductoPendiente findById(Long id) {
        return productoPendienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto Pendiente", id.toString()));
    }

    @Override
    public List<ProductoPendienteResponsiveDTO> listarProductosPendientes() {
        return productoPendienteRepository.findAll().stream().map(this::conversorDTO).toList();
    }

}
