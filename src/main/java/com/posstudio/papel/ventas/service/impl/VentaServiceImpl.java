package com.posstudio.papel.ventas.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.posstudio.papel.common.enums.EstadoTurno;
import com.posstudio.papel.common.enums.EstadoVenta;
import com.posstudio.papel.common.enums.TipoMovimientoInventario;
import com.posstudio.papel.common.exception.BusinessException;
import com.posstudio.papel.common.exception.ResourceNotFoundException;
import com.posstudio.papel.inventario.service.ProductoService;
import com.posstudio.papel.security.model.Usuario;
import com.posstudio.papel.turnos.model.Turno;
import com.posstudio.papel.turnos.repository.TurnoRepository;
import com.posstudio.papel.ventas.dto.request.DetalleVentaRequestDTO;
import com.posstudio.papel.ventas.dto.request.EditarDetalleVentaRequestDTO;
import com.posstudio.papel.ventas.dto.request.PagoVentaRequestDTO;
import com.posstudio.papel.ventas.dto.responsive.DetalleVentaResponsiveDTO;
import com.posstudio.papel.ventas.dto.responsive.PagoVentaResponsiveDTO;
import com.posstudio.papel.ventas.dto.responsive.VentaResponsiveDTO;
import com.posstudio.papel.ventas.model.Venta;
import com.posstudio.papel.ventas.repository.VentaRepository;
import com.posstudio.papel.ventas.service.DetalleVentaService;
import com.posstudio.papel.ventas.service.PagoVentaService;
import com.posstudio.papel.ventas.service.VentaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class VentaServiceImpl implements VentaService {
    private final VentaRepository ventaRepository;
    private final TurnoRepository turnoRepository;
    private final DetalleVentaService detalleVentaService;
    private final PagoVentaService pagoVentaService;
    private final ProductoService productoService;

    private VentaResponsiveDTO conversorDTO(Venta data) {
        // Convertir los detalles de venta
        List<DetalleVentaResponsiveDTO> detallesDTO = data.getDetalles() != null
                ? data.getDetalles().stream()
                        .map(detalle -> new DetalleVentaResponsiveDTO(
                                detalle.getId(),
                                detalle.getProducto().getNombre(),
                                detalle.getCantidad(),
                                detalle.getPrecioUnitario(),
                                detalle.getSubtotal(),
                                detalle.getDescuento()))
                        .toList()
                : List.of(); // Lista vacía si no hay detalles

        // Convertir los pagos
        List<PagoVentaResponsiveDTO> pagosDTO = data.getPagos() != null
                ? data.getPagos().stream()
                        .map(pago -> new PagoVentaResponsiveDTO(
                                pago.getId(),
                                pago.getVenta().getId(),
                                pago.getMetodo(),
                                pago.getMonto()))
                        .toList()
                : List.of(); // Lista vacía si no hay pagos

        return new VentaResponsiveDTO(
                data.getId(),
                data.getUsuario().getNombre(),
                data.getTurno().getTipoTurno(),
                data.getTotal(),
                data.getFecha(),
                data.getEstado(),
                detallesDTO,
                pagosDTO);
    }

    @Override
    public VentaResponsiveDTO crearVenta() {

        Turno turno = turnoRepository.findByEstadoTurno(EstadoTurno.ABIERTO)
                .orElseThrow(() -> new BusinessException("Debe de haber un turno abierto para crear una venta"));
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Venta venta = Venta.builder()
                .usuario(usuario)
                .turno(turno)
                .total(BigDecimal.ZERO)
                .estado(EstadoVenta.ABIERTA)
                .build();
        ventaRepository.save(venta);
        return conversorDTO(venta);
    }

    @Override
    public void cancelarVenta(Long ventaId) {

        Venta venta = findById(ventaId);
        if (venta.getEstado() == EstadoVenta.CERRADA) {
            throw new BusinessException("No se puede eliminar una venta cerrada");
        }
        ventaRepository.delete(venta);
    }

    @Override
    public List<VentaResponsiveDTO> listarVentasActivas() {
        Turno turno = turnoRepository.findByEstadoTurno(EstadoTurno.ABIERTO)
                .orElseThrow(
                        () -> new BusinessException("Debe de haber un turno abierto para listar ventas pendientes"));
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ventaRepository.findByEstadoAndUsuarioAndTurno(EstadoVenta.ABIERTA, usuario, turno)
                .stream().map(this::conversorDTO).toList();
    }

    @Override
    public List<VentaResponsiveDTO> listarVentasCerradas() {
        Turno turno = turnoRepository.findByEstadoTurno(EstadoTurno.ABIERTO)
                .orElseThrow(() -> new BusinessException("Debe de haber un turno abierto para listar ventas hechas"));
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ventaRepository.findByEstadoAndUsuarioAndTurno(EstadoVenta.CERRADA, usuario, turno)
                .stream().map(this::conversorDTO).toList();
    }

    private Venta findById(Long id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada", id.toString()));
    }

    @Override
    public VentaResponsiveDTO añadirDetalleventa(Long ventaId, DetalleVentaRequestDTO data) {
        Venta venta = findById(ventaId);
        if (venta.getEstado() != EstadoVenta.ABIERTA) {
            throw new BusinessException("Para añadir Detalle venta debe de estar abierta la venta");
        }
        detalleVentaService.crearDetalleVenta(data, venta);
        venta.recalcularTotal();
        return conversorDTO(venta);
    }

    @Override
    public VentaResponsiveDTO editarDetalleventa(Long detalleVentaId, EditarDetalleVentaRequestDTO data, Long ventaId) {
        Venta venta = findById(ventaId);
        if (venta.getEstado() != EstadoVenta.ABIERTA) {
            throw new BusinessException("Para añadir Detalle venta debe de estar abierta la venta");
        }
        detalleVentaService.editarDetalleVenta(data, detalleVentaId, venta);
        venta.recalcularTotal();
        return conversorDTO(venta);
    }

    @Override
    public VentaResponsiveDTO cerrarVenta(Long ventaId, PagoVentaRequestDTO pago) {
        Venta venta = findById(ventaId);
        if (venta.getEstado() != EstadoVenta.ABIERTA) {
            throw new BusinessException("Para cerrar la venta el estado debe de estar abierto");
        }
        if (venta.getDetalles().isEmpty()) {
            throw new BusinessException("No se puede cerrar una venta sin productos");
        }
        pagoVentaService.añadirPago(pago, venta);
        venta.getDetalles()
                .forEach(det -> productoService.ajustarStock(TipoMovimientoInventario.VENTA, det.getCantidad(),
                        det.getProducto(), ventaId));

        venta.setEstado(EstadoVenta.CERRADA);
        ventaRepository.save(venta);
        return conversorDTO(venta);
    }

    @Override
    public VentaResponsiveDTO eliminarDetalleVenta(Long ventaId, Long detalleVentaId) {
        Venta venta = findById(ventaId);
        if (venta.getEstado() != EstadoVenta.ABIERTA) {
            throw new BusinessException("Para añadir Detalle venta debe de estar abierta la venta");
        }

        detalleVentaService.eliminarDetalleVenta(detalleVentaId, venta);
        venta.recalcularTotal();
        return conversorDTO(venta);
    }

    @Override
    public VentaResponsiveDTO añadirDetalleVentaEnUno(Long ventaId, Long detalleVentaId) {
        Venta venta = findById(ventaId);
        if (venta.getEstado() != EstadoVenta.ABIERTA) {
            throw new BusinessException("Para añadir detalle venta debe de estar abierta");
        }
        detalleVentaService.añadirDetalleVentaEnUno(venta, detalleVentaId);
        venta.recalcularTotal();
        return conversorDTO(venta);
    }

    @Override
    public VentaResponsiveDTO eliminarDetalleVentaEnUno(Long ventaId, Long detalleVentaId) {
        Venta venta = findById(ventaId);
        if (venta.getEstado() != EstadoVenta.ABIERTA) {
            throw new BusinessException("Para añadir detalle venta debe de estar abierta");
        }
        detalleVentaService.eliminarDetalleVentaEnUno(venta, detalleVentaId);
        venta.recalcularTotal();
        return conversorDTO(venta);
    }

}
