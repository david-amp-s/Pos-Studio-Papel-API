package com.posstudio.papel.inventario.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.posstudio.papel.common.enums.TipoMovimientoInventario;
import com.posstudio.papel.common.exception.BusinessException;
import com.posstudio.papel.common.exception.ResourceNotFoundException;
import com.posstudio.papel.inventario.dto.request.MovimientoInventarioRequestDTO;
import com.posstudio.papel.inventario.dto.request.ProductoRequestDTO;
import com.posstudio.papel.inventario.dto.responsive.ProductoResponsiveDTO;
import com.posstudio.papel.inventario.model.Categoria;
import com.posstudio.papel.inventario.model.Producto;
import com.posstudio.papel.inventario.model.Ubicacion;
import com.posstudio.papel.inventario.repository.ProductoRepository;
import com.posstudio.papel.inventario.service.CategoriaService;
import com.posstudio.papel.inventario.service.MovimientoInventarioService;
import com.posstudio.papel.inventario.service.ProductoService;
import com.posstudio.papel.inventario.service.UbicacionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {
    private final ProductoRepository productoRepository;
    private final CategoriaService categoriaService;
    private final UbicacionService ubicacionService;
    private final MovimientoInventarioService movimientoInventario;

    private ProductoResponsiveDTO conversorDTO(Producto producto) {
        return new ProductoResponsiveDTO(producto.getId(), producto.getNombre(), producto.getCodigoDeBarras(),
                producto.getPrecio(), producto.getStock(), producto.getCategoria().getNombre(),
                producto.getUbicacion().getCodigo(), producto.getUnidadNegocio().name());
    }

    @Override
    public ProductoResponsiveDTO crearProducto(ProductoRequestDTO data) {
        validarProductoNoExiste(data.nombre());
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
        ajustarStock(TipoMovimientoInventario.CREACION, data.stock(), producto, producto.getId());
        return conversorDTO(producto);
    }

    @Override
    public List<ProductoResponsiveDTO> listarProducto() {
        return productoRepository.findByActivo(true).stream().map(this::conversorDTO).toList();
    }

    @Override
    public ProductoResponsiveDTO editarProducto(Long id, ProductoRequestDTO data) {
        Producto producto = findByid(id);
        Categoria categoria = categoriaService.findByNombre(data.categoria());
        Ubicacion ubicacion = ubicacionService.findByCodigo(data.ubicacion());

        producto.setNombre(data.nombre());
        producto.setCodigoDeBarras(data.codigoDeBarras());
        producto.setPrecio(data.precio());
        producto.setCategoria(categoria);
        producto.setUbicacion(ubicacion);
        producto.setUnidadNegocio(data.unidadNegocio());
        productoRepository.save(producto);
        if (!producto.getStock().equals(data.stock())) {
            ajustarStock(TipoMovimientoInventario.AJUSTE, data.stock(), producto, id);
        }
        return conversorDTO(producto);
    }

    @Override
    public Producto findByNombre(String nombre) {
        return productoRepository.findByNombre(nombre)
                .orElseThrow(() -> new ResourceNotFoundException("Nombre", nombre));
    }

    @Override
    public ProductoResponsiveDTO ajustarStock(TipoMovimientoInventario tipoMovimiento, Integer cantidad,
            Producto producto, Long referencia) {
        switch (tipoMovimiento) {
            case CREACION:
                producto.setStock(cantidad);
                productoRepository.save(producto);
                movimientoInventario
                        .guardar(new MovimientoInventarioRequestDTO(producto, tipoMovimiento, cantidad, referencia,
                                "Creacion del producto"));
                return conversorDTO(producto);
            case COMPRA:

                if (cantidad < 0) {
                    throw new BusinessException("La cantidad de productos no puede ser negativa");
                }

                producto.setStock(cantidad + producto.getStock());
                productoRepository.save(producto);

                movimientoInventario.guardar(new MovimientoInventarioRequestDTO(producto, tipoMovimiento, cantidad,
                        referencia, "Compra del producto"));

                return conversorDTO(producto);

            case VENTA:
                if (producto.getStock() < cantidad) {
                    System.out.println("DEBE DE AJUSTARSE INVENTARIO");
                    producto.setStock(0);
                    // Aca se guardara en una tabla de alertas_inventario para ajustes urgentes
                    // (future)
                    productoRepository.save(producto);
                    movimientoInventario.guardar(
                            new MovimientoInventarioRequestDTO(producto, tipoMovimiento, cantidad, referencia,
                                    "Se vendio el producto con mas cantidad de lo que habia en el stock"));
                    return conversorDTO(producto);
                }
                producto.setStock(producto.getStock() - cantidad);
                productoRepository.save(producto);
                movimientoInventario.guardar(new MovimientoInventarioRequestDTO(producto, tipoMovimiento, cantidad,
                        referencia, "Venta del producto"));
                return conversorDTO(producto);

            case DEVOLUCION:
                producto.setStock(cantidad + producto.getStock());
                productoRepository.save(producto);
                movimientoInventario.guardar(new MovimientoInventarioRequestDTO(producto, tipoMovimiento, cantidad,
                        referencia, "Hubo una devolucion del producto"));
                return conversorDTO(producto);
            case AJUSTE:
                producto.setStock(cantidad);
                productoRepository.save(producto);
                movimientoInventario.guardar(new MovimientoInventarioRequestDTO(producto, tipoMovimiento, cantidad,
                        referencia, "Se ajusto el stock del producto"));
                return conversorDTO(producto);
            default:
                throw new BusinessException("Error al seleccionar el tipo de movimiento");
        }

    }

    @Override
    public void desactivarProducto(Long id) {
        Producto producto = findByid(id);
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    @Override
    public Producto findByid(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", id.toString()));
    }

    public void validarProductoNoExiste(String nombre) {
        if (productoRepository.findByNombre(nombre).isPresent()) {
            throw new BusinessException("Producto ya existe con ese nombre", 409);
        }

    }
}
