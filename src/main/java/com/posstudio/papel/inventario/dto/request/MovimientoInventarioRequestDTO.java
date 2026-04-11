package com.posstudio.papel.inventario.dto.request;

import com.posstudio.papel.common.enums.TipoMovimientoInventario;
import com.posstudio.papel.inventario.model.Producto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MovimientoInventarioRequestDTO(
        @NotNull(message = "EL producto debe de existir") Producto producto,
        @NotBlank(message = "No puede estar vacio el tipo de movimiento") TipoMovimientoInventario tipoMovimientoInventario,
        @NotBlank(message = "Debe de haber una cantidad de moivmiento") Integer cantidad,
        @NotBlank(message = "La referencia de la accion no puede") Long referencia,
        @NotBlank(message = "No puede estar vacio la descripcion  del movimiento") String descripcion) {

}
