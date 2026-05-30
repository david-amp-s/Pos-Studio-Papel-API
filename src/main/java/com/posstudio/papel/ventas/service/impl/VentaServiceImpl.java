package com.posstudio.papel.ventas.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.posstudio.papel.common.enums.EstadoTurno;
import com.posstudio.papel.common.enums.EstadoVenta;
import com.posstudio.papel.common.enums.TipoMovimientoInventario;
import com.posstudio.papel.common.exception.BusinessException;
import com.posstudio.papel.common.exception.ResourceNotFoundException;
import com.posstudio.papel.inventario.service.ProductoService;
import com.posstudio.papel.security.model.Usuario;
import com.posstudio.papel.turnos.model.Turno;
import com.posstudio.papel.turnos.repository.TurnoRepository;
import com.posstudio.papel.ventas.dto.request.PagoVentaRequestDTO;
import com.posstudio.papel.ventas.dto.responsive.DetalleVentaResponsiveDTO;
import com.posstudio.papel.ventas.dto.responsive.PagoVentaResponsiveDTO;
import com.posstudio.papel.ventas.dto.responsive.VentaResponsiveDTO;
import com.posstudio.papel.ventas.model.Venta;
import com.posstudio.papel.ventas.repository.VentaRepository;
import com.posstudio.papel.ventas.service.PagoVentaService;
import com.posstudio.papel.ventas.service.VentaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class VentaServiceImpl implements VentaService {
        private final VentaRepository ventaRepository;
        private final TurnoRepository turnoRepository;

        private final PagoVentaService pagoVentaService;
        private final ProductoService productoService;

        private VentaResponsiveDTO conversorDTO(Venta data) {
                // Convertir los detalles de venta
                List<DetalleVentaResponsiveDTO> detallesDTO = data.getDetalles() != null
                                ? data.getDetalles().stream()
                                                .map(detalle -> new DetalleVentaResponsiveDTO(
                                                                detalle.getId(),
                                                                detalle.getProducto() != null
                                                                                ? detalle.getProducto().getId()
                                                                                : null,
                                                                detalle.getProducto() != null
                                                                                ? detalle.getProducto().getNombre()
                                                                                : null,
                                                                detalle.getProductoPendiente() != null
                                                                                ? detalle.getProductoPendiente().getId()
                                                                                : null,
                                                                detalle.getProductoPendiente() != null
                                                                                ? detalle.getProductoPendiente()
                                                                                                .getNombre()
                                                                                : null,
                                                                detalle.getCantidad(),
                                                                detalle.getPrecioUnitario(),
                                                                detalle.getSubtotal(),
                                                                detalle.getDescuento()))
                                                .toList()
                                : List.of();

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
                                .orElseThrow(() -> new BusinessException(
                                                "Debe de haber un turno abierto para crear una venta"));
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
                                                () -> new BusinessException(
                                                                "Debe de haber un turno abierto para listar ventas pendientes"));
                Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                return ventaRepository.findByEstadoAndUsuarioAndTurno(EstadoVenta.ABIERTA, usuario, turno)
                                .stream().map(this::conversorDTO).toList();
        }

        @Override
        public List<VentaResponsiveDTO> listarVentasCerradas() {
                Turno turno = turnoRepository.findByEstadoTurno(EstadoTurno.ABIERTO)
                                .orElseThrow(() -> new BusinessException(
                                                "Debe de haber un turno abierto para listar ventas hechas"));
                Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                return ventaRepository.findByEstadoAndUsuarioAndTurno(EstadoVenta.CERRADA, usuario, turno)
                                .stream().map(this::conversorDTO).toList();
        }

        private Venta findById(Long id) {
                return ventaRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada", id.toString()));
        }

        @Override
        public VentaResponsiveDTO cerrarVenta(Long ventaId, PagoVentaRequestDTO pago) {
                Venta venta = ventaRepository.findByIdConDetalles(ventaId).orElseThrow(
                                () -> new ResourceNotFoundException("venta no encontrada", ventaId.toString()));
                if (venta.getEstado() != EstadoVenta.ABIERTA) {
                        throw new BusinessException("Para cerrar la venta el estado debe de estar abierto");
                }
                if (venta.getDetalles().isEmpty()) {
                        throw new BusinessException("No se puede cerrar una venta sin productos");
                }
                pagoVentaService.añadirPago(pago, venta);
                venta.getDetalles()
                                .forEach(det -> productoService.ajustarStock(TipoMovimientoInventario.VENTA,
                                                det.getCantidad(),
                                                det.getProducto(), ventaId));

                venta.setEstado(EstadoVenta.CERRADA);
                ventaRepository.save(venta);
                return conversorDTO(venta);
        }

        @Override
        public byte[] generarTicketPDF(Long ventaId) {
                Venta venta = findById(ventaId);

                try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

                        // Márgenes: izq, der, arr, abj
                        Document doc = new Document(new Rectangle(226, 800), 10, 10, 12, 12);
                        PdfWriter.getInstance(doc, baos);
                        doc.open();

                        // ── Fuentes ──────────────────────────────────────────
                        Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
                        Font fontSub = FontFactory.getFont(FontFactory.HELVETICA, 8);
                        Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 8);
                        Font fontBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
                        Font fontTotal = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
                        Font fontFooter = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8);

                        // ── Logo ─────────────────────────────────────────────
                        // Opción A: logo desde classpath (resources/static/logo.png)
                        InputStream logoStream = getClass().getResourceAsStream("/static/logo.png");
                        if (logoStream != null) {
                                Image logo = Image.getInstance(logoStream.readAllBytes());
                                logo.scaleToFit(70, 70);
                                logo.setAlignment(Image.ALIGN_CENTER);
                                doc.add(logo);
                        }

                        // ── Encabezado ───────────────────────────────────────
                        agregarParrafo(doc, "Studio Papel", fontTitulo, Element.ALIGN_CENTER);
                        agregarParrafo(doc, "Miscelánea", fontSub, Element.ALIGN_CENTER);
                        agregarParrafo(doc, "Nit : 51852963", fontSub, Element.ALIGN_CENTER);
                        agregarParrafo(doc, "3166676721", fontSub, Element.ALIGN_CENTER);
                        agregarParrafo(doc, "carrera 97B #153-72", fontSub, Element.ALIGN_CENTER);

                        agregarParrafo(doc, "¡Gracias por tu compra!", fontBold, Element.ALIGN_CENTER);
                        doc.add(Chunk.NEWLINE);

                        // ── Tabla de productos ───────────────────────────────
                        PdfPTable tabla = new PdfPTable(3); // 3 columnas
                        tabla.setWidthPercentage(100);
                        tabla.setWidths(new float[] { 5f, 1.5f, 2.5f }); // Producto | Cant | Precio

                        // Encabezados de tabla
                        agregarCeldaTabla(tabla, "Producto", fontBold, Element.ALIGN_LEFT, true);
                        agregarCeldaTabla(tabla, "Cant", fontBold, Element.ALIGN_CENTER, true);
                        agregarCeldaTabla(tabla, "Precio", fontBold, Element.ALIGN_RIGHT, true);

                        // Filas de productos
                        for (var detalle : venta.getDetalles()) {
                                String nombre = detalle.getProducto().getNombre();
                                agregarCeldaTabla(tabla, nombre, fontNormal, Element.ALIGN_LEFT, false);
                                agregarCeldaTabla(tabla, String.valueOf(detalle.getCantidad()), fontNormal,
                                                Element.ALIGN_CENTER, false);
                                agregarCeldaTabla(tabla, "$" + detalle.getSubtotal().toPlainString(), fontNormal,
                                                Element.ALIGN_RIGHT, false);
                        }

                        doc.add(tabla);

                        // ── Separador + Total ────────────────────────────────
                        doc.add(Chunk.NEWLINE);
                        agregarParrafo(doc, "--------------------------------", fontNormal, Element.ALIGN_CENTER);

                        Paragraph totalPara = new Paragraph("TOTAL    $" + venta.getTotal().toPlainString(), fontTotal);
                        totalPara.setAlignment(Element.ALIGN_RIGHT);
                        doc.add(totalPara);

                        agregarParrafo(doc, "--------------------------------", fontNormal, Element.ALIGN_CENTER);

                        // ── Fecha formateada ─────────────────────────────────
                        String fecha = venta.getFecha()
                                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy  hh:mm a"));
                        agregarParrafo(doc, "Fecha: " + fecha, fontSub, Element.ALIGN_CENTER);
                        doc.add(Chunk.NEWLINE);

                        // ── Footer ───────────────────────────────────────────
                        agregarParrafo(doc, "¡Vuelve pronto! 🩷", fontFooter, Element.ALIGN_CENTER);

                        doc.close();
                        return baos.toByteArray();

                } catch (Exception e) {
                        throw new BusinessException("Error al generar el ticket PDF: " + e.getMessage());
                }
        }

        // ── Helpers privados
        // ──────────────────────────────────────────────────────────

        private void agregarParrafo(Document doc, String texto, Font font, int alineacion) throws Exception {
                Paragraph p = new Paragraph(texto, font);
                p.setAlignment(alineacion);
                doc.add(p);
        }

        private void agregarCeldaTabla(PdfPTable tabla, String texto, Font font, int alineacion, boolean esHeader) {
                PdfPCell celda = new PdfPCell(new Phrase(texto, font));
                celda.setBorder(esHeader ? PdfPCell.BOTTOM : PdfPCell.NO_BORDER);
                celda.setHorizontalAlignment(alineacion);
                celda.setPaddingBottom(3f);
                tabla.addCell(celda);
        }

}
