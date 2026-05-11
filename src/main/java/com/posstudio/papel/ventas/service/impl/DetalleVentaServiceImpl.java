package com.posstudio.papel.ventas.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.posstudio.papel.common.enums.EstadoVenta;
import com.posstudio.papel.common.exception.BusinessException;
import com.posstudio.papel.common.exception.ResourceNotFoundException;
import com.posstudio.papel.inventario.model.Producto;
import com.posstudio.papel.inventario.service.ProductoService;
import com.posstudio.papel.ventas.dto.request.DetalleVentaRequestDTO;
import com.posstudio.papel.ventas.dto.request.EditarDetalleVentaRequestDTO;
import com.posstudio.papel.ventas.dto.responsive.DetalleVentaResponsiveDTO;
import com.posstudio.papel.ventas.model.DetalleVenta;
import com.posstudio.papel.ventas.model.Venta;
import com.posstudio.papel.ventas.repository.DetalleVentaRepository;
import com.posstudio.papel.ventas.repository.VentaRepository;
import com.posstudio.papel.ventas.service.DetalleVentaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DetalleVentaServiceImpl implements DetalleVentaService {
    private final DetalleVentaRepository detalleVentaRepository;
    private final ProductoService productoService;
    private final VentaRepository ventaRepository;

    private DetalleVentaResponsiveDTO conversorDTO(DetalleVenta data) {
        return new DetalleVentaResponsiveDTO(data.getId(), data.getProducto().getId(), data.getProducto().getNombre(),
                data.getCantidad(),
                data.getPrecioUnitario(), data.getSubtotal(), data.getDescuento());
    }

    @Override
    public DetalleVentaResponsiveDTO crearDetalleVenta(DetalleVentaRequestDTO data, Long ventaId) {
        Venta venta = validarVenta(ventaId);
        Producto producto = productoService.findByid(data.productoId());
        DetalleVenta verificarProducto = detalleVentaRepository.findByProductoAndVenta(producto, venta).orElse(null);
        if (verificarProducto != null) {
            verificarProducto.setCantidad(verificarProducto.getCantidad() + 1);
            detalleVentaRepository.save(verificarProducto);
            recalcularTotalVenta(venta);
            return conversorDTO(verificarProducto);
        }

        DetalleVenta detalleVenta = DetalleVenta.builder()
                .venta(venta)
                .producto(producto)
                .cantidad(data.cantidad())
                .precioUnitario(producto.getPrecio())
                .descuento(BigDecimal.ZERO)
                .build();
        detalleVentaRepository.save(detalleVenta);
        recalcularTotalVenta(venta);
        return conversorDTO(detalleVenta);
    }

    @Override
    public DetalleVentaResponsiveDTO editarDetalleVenta(EditarDetalleVentaRequestDTO data, Long detalleVentaId,
            Long ventaId) {
        Venta venta = validarVenta(ventaId);
        DetalleVenta detalleVenta = findById(detalleVentaId);
        if (!venta.getId().equals(detalleVenta.getVenta().getId())) {
            throw new BusinessException("No se puede modificar el detalle producto de otro turno");
        }
        detalleVenta.setCantidad(data.cantidad());
        detalleVenta.setDescuento(data.descuento());
        detalleVentaRepository.save(detalleVenta);
        recalcularTotalVenta(venta);
        return conversorDTO(detalleVenta);
    }

    private DetalleVenta findById(Long id) {
        return detalleVentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detalle venta no encontrada", id.toString()));
    }

    @Override
    public void eliminarDetalleVenta(Long ventaId, Long detalleVentaId) {
        Venta venta = validarVenta(ventaId);
        DetalleVenta detalleVenta = findById(detalleVentaId);
        if (!venta.getId().equals(detalleVenta.getVenta().getId())) {
            throw new BusinessException("No se puede eliminar el detalle producto de otro turno");
        }
        detalleVentaRepository.delete(detalleVenta);
        recalcularTotalVenta(venta);
    }

    @Override
    public DetalleVentaResponsiveDTO añadirDetalleVentaEnUno(Long ventaId, Long detalleVentaId) {
        Venta venta = validarVenta(ventaId);
        DetalleVenta detalleVenta = findById(detalleVentaId);
        if (!venta.getId().equals(detalleVenta.getVenta().getId())) {
            throw new BusinessException("No se puede modificar el detalle producto de otro turno");
        }
        detalleVenta.setCantidad(detalleVenta.getCantidad() + 1);
        detalleVentaRepository.save(detalleVenta);
        recalcularTotalVenta(venta);
        return conversorDTO(detalleVenta);
    }

    @Override
    public DetalleVentaResponsiveDTO eliminarDetalleVentaEnUno(Long ventaId, Long detalleVentaId) {
        Venta venta = validarVenta(ventaId);
        DetalleVenta detalleVenta = findById(detalleVentaId);
        if (!venta.getId().equals(detalleVenta.getVenta().getId())) {
            throw new BusinessException("No se puede modificar el detalle producto de otro turno");
        }

        if (detalleVenta.getCantidad() > 1) {
            // Si hay más de 1, solo reducimos la cantidad
            detalleVenta.setCantidad(detalleVenta.getCantidad() - 1);
            detalleVentaRepository.save(detalleVenta);
            recalcularTotalVenta(venta);
            return conversorDTO(detalleVenta);
        } else if (detalleVenta.getCantidad() == 1) {
            // Si solo hay 1, eliminamos el detalle completo
            detalleVentaRepository.delete(detalleVenta);
            recalcularTotalVenta(venta);
            return null; // o lanzar una excepción indicando que se eliminó
        } else {
            throw new BusinessException("La cantidad no puede ser menor a 1");
        }
    }

    private Venta validarVenta(Long ventaId) {
        Venta venta = ventaRepository.findByIdWithLock(ventaId)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada", ventaId.toString()));
        if (!venta.getEstado().equals(EstadoVenta.ABIERTA)) {
            throw new BusinessException("No se puede editar una venta si no esta ABIERTA");
        }
        return venta;
    }

    private void recalcularTotalVenta(Venta venta) {
        BigDecimal total = detalleVentaRepository.sumSubtotalByVentaId(venta.getId());
        venta.setTotal(total);
        ventaRepository.save(venta);
    }

    @Override
    public List<DetalleVentaResponsiveDTO> listarDetalleVentaEnVenta(Long ventaId) {
        Venta venta = ventaRepository.findById(ventaId)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada", ventaId.toString()));
        return detalleVentaRepository.findByVenta(venta).stream().map(this::conversorDTO).toList();
    }
}
