package com.posstudio.papel.inventario.repository;

import org.springframework.data.jpa.domain.Specification;

import com.posstudio.papel.inventario.dto.filter.ProductoFiltroDTO;
import com.posstudio.papel.inventario.model.Producto;

// ProductoSpecification.java
public class ProductoSpecification {

    public static Specification<Producto> conFiltros(ProductoFiltroDTO filtro) {
        return Specification
                .where(activo())
                .and(nombreContiene(filtro.nombre()))
                .and(categoriaNombre(filtro.categoria()))
                .and(ubicacionCodigo(filtro.ubicacion()))
                .and(codigoDeBarras(filtro.codigoDeBarras()));
    }

    private static Specification<Producto> activo() {
        return (root, query, cb) -> cb.isTrue(root.get("activo"));
    }

    private static Specification<Producto> nombreContiene(String nombre) {
        return (root, query, cb) -> {
            if (nombre == null || nombre.isBlank())
                return null;
            return cb.like(cb.lower(root.get("nombre")), "%" + nombre.toLowerCase() + "%");
        };
    }

    private static Specification<Producto> categoriaNombre(String categoria) {
        return (root, query, cb) -> {
            if (categoria == null || categoria.isBlank())
                return null;
            // Join con la tabla categoria
            return cb.equal(root.join("categoria").get("nombre"), categoria);
        };
    }

    private static Specification<Producto> ubicacionCodigo(String ubicacion) {
        return (root, query, cb) -> {
            if (ubicacion == null || ubicacion.isBlank())
                return null;
            return cb.equal(root.join("ubicacion").get("codigo"), ubicacion);
        };
    }

    private static Specification<Producto> codigoDeBarras(String codigo) {
        return (root, query, cb) -> {
            if (codigo == null || codigo.isBlank())
                return null;
            return cb.equal(root.get("codigoDeBarras"), codigo);
        };
    }
}
