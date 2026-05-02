package com.posstudio.papel.ventas.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.posstudio.papel.common.enums.EstadoTurno;
import com.posstudio.papel.common.enums.EstadoVenta;
import com.posstudio.papel.common.exception.BusinessException;
import com.posstudio.papel.common.exception.ResourceNotFoundException;
import com.posstudio.papel.security.model.Usuario;
import com.posstudio.papel.turnos.model.Turno;
import com.posstudio.papel.turnos.repository.TurnoRepository;
import com.posstudio.papel.ventas.dto.request.DetalleVentaRequestDTO;
import com.posstudio.papel.ventas.dto.responsive.VentaResponsiveDTO;
import com.posstudio.papel.ventas.model.Venta;
import com.posstudio.papel.ventas.repository.VentaRepository;
import com.posstudio.papel.ventas.service.DetalleVentaService;
import com.posstudio.papel.ventas.service.VentaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class VentaServiceImpl implements VentaService {
    private final VentaRepository ventaRepository;
    private final TurnoRepository turnoRepository;
    private final DetalleVentaService detalleVentaService;

    private VentaResponsiveDTO conversorDTO(Venta data) {
        return new VentaResponsiveDTO(data.getId(), data.getUsuario().getNombre(), data.getTurno().getTipoTurno(),
                data.getTotal(), data.getFecha(), data.getEstado(), data.getDetalles().size());
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
        return conversorDTO(venta);
    }

    @Override
    public VentaResponsiveDTO editarDetalleventa(Long detalleVentaId, DetalleVentaRequestDTO data, Long ventaId) {
        Venta venta = findById(ventaId);
        if (venta.getEstado() != EstadoVenta.ABIERTA) {
            throw new BusinessException("Para añadir Detalle venta debe de estar abierta la venta");
        }
        detalleVentaService.editarDetalleVenta(data, detalleVentaId, venta);

        return conversorDTO(venta);
    }

    @Override
    public VentaResponsiveDTO cerrarVenta() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cerrarVenta'");
    }

    @Override
    public VentaResponsiveDTO eliminarDetalleVenta(Long ventaId, Long detalleVentaId) {
        Venta venta = findById(ventaId);
        if (venta.getEstado() != EstadoVenta.ABIERTA) {
            throw new BusinessException("Para añadir Detalle venta debe de estar abierta la venta");
        }

        detalleVentaService.eliminarDetalleVenta(detalleVentaId, venta);
        return conversorDTO(venta);
    }

}
