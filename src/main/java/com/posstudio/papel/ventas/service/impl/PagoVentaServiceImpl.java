package com.posstudio.papel.ventas.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.posstudio.papel.common.enums.MetodoPago;
import com.posstudio.papel.common.exception.BusinessException;
import com.posstudio.papel.common.exception.ResourceNotFoundException;
import com.posstudio.papel.ventas.dto.request.MetodoPagoDTO;
import com.posstudio.papel.ventas.dto.request.PagoVentaRequestDTO;
import com.posstudio.papel.ventas.dto.responsive.PagoVentaResponsiveDTO;
import com.posstudio.papel.ventas.model.Pagoventa;
import com.posstudio.papel.ventas.model.Venta;
import com.posstudio.papel.ventas.repository.PagoVentaRepository;
import com.posstudio.papel.ventas.repository.VentaRepository;
import com.posstudio.papel.ventas.service.PagoVentaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PagoVentaServiceImpl implements PagoVentaService {
    private final PagoVentaRepository pagoVentaRepository;

    private PagoVentaResponsiveDTO conversorDTO(Pagoventa data) {
        return new PagoVentaResponsiveDTO(
                data.getId(),
                data.getVenta().getId(),
                data.getMetodo(),
                data.getMonto());
    }

    @Override
    public List<PagoVentaResponsiveDTO> añadirPago(PagoVentaRequestDTO data, Venta venta) {
        BigDecimal total = data.pagos().stream().map(MetodoPagoDTO::monto).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (total.compareTo(venta.getTotal()) != 0) {
            if (total.compareTo(venta.getTotal()) > 0) {
                throw new BusinessException("El total pagado es mayor al monto");
            } else {
                throw new BusinessException("El total es menor al monto");
            }
        }
        List<Pagoventa> pagosventa = data.pagos().stream().map(pag -> Pagoventa.builder()
                .metodo(pag.metodoPago())
                .monto(pag.monto())
                .venta(venta)
                .build()).toList();
        pagoVentaRepository.saveAll(pagosventa);
        return pagosventa.stream().map(this::conversorDTO).toList();
    }

    @Override
    public BigDecimal calcularTotalPagosEnVenta(Long turnoId) {
        return pagoVentaRepository.findByVenta_TurnoId(turnoId).stream().map(Pagoventa::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal calcularPagosEnEfectivo(Long turnoId) {
        return pagoVentaRepository.findByVenta_TurnoIdAndMetodo(turnoId, MetodoPago.EFECTIVO).stream()
                .map(Pagoventa::getMonto).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal calcularPagosEnTransf(Long turnoId) {
        return pagoVentaRepository.findByVenta_TurnoIdAndMetodo(turnoId, MetodoPago.TRANSFERENCIA).stream()
                .map(Pagoventa::getMonto).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal calcularPagosEnTarjeta(Long turnoId) {
        return pagoVentaRepository.findByVenta_TurnoIdAndMetodo(turnoId, MetodoPago.TARJETA).stream()
                .map(Pagoventa::getMonto).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
