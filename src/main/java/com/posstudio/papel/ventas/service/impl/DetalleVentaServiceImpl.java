package com.posstudio.papel.ventas.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.posstudio.papel.common.enums.TipoMovimientoInventario;
import com.posstudio.papel.common.exception.BusinessException;
import com.posstudio.papel.inventario.model.Producto;
import com.posstudio.papel.inventario.service.ProductoService;
import com.posstudio.papel.ventas.dto.request.DetalleVentaRequestDTO;
import com.posstudio.papel.ventas.dto.responsive.DetalleVentaResponsiveDTO;
import com.posstudio.papel.ventas.model.DetalleVenta;
import com.posstudio.papel.ventas.model.Venta;
import com.posstudio.papel.ventas.repository.DetalleVentaRepository;
import com.posstudio.papel.ventas.service.DetalleVentaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DetalleVentaServiceImpl implements DetalleVentaService {
    private final DetalleVentaRepository detalleVentaRepository;
    private final ProductoService productoService;

    private DetalleVentaResponsiveDTO conversorDTO(DetalleVenta data) {
        return new DetalleVentaResponsiveDTO(data.getId(), data.getProducto().getNombre(), data.getCantidad(),
                data.getPrecioUnitario(), data.getSubtotal());
    }

    @Override
    public DetalleVentaResponsiveDTO crearDetalleVenta(DetalleVentaRequestDTO data, Venta venta) {
        Producto producto = productoService.findByid(data.productoId());
        DetalleVenta verificarProducto = detalleVentaRepository.findByProductoAndVenta(producto, venta).orElse(null);
        if (verificarProducto != null) {
            throw new BusinessException("Ya hay un detalle venta con este producto en la misma venta");
        }
        productoService.ajustarStock(TipoMovimientoInventario.VENTA, data.cantidad(), producto, venta.getId());
        DetalleVenta detalleVenta = DetalleVenta.builder()
                .venta(venta)
                .producto(producto)
                .cantidad(data.cantidad())
                .precioUnitario(producto.getPrecio())
                .build();
        detalleVentaRepository.save(detalleVenta);
        return conversorDTO(detalleVenta);
    }

    @Override
    public DetalleVentaResponsiveDTO editarDetalleVenta(DetalleVentaRequestDTO data, Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'editarDetalleVenta'");
    }

    @Override
    public DetalleVentaRequestDTO eliminarDetalleventa(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eliminarDetalleventa'");
    }

}
