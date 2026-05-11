package com.posstudio.papel.ventas.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotNull;

public record PagoVentaRequestDTO(

                @NotNull(message = "No puede estar vacia la lista de pagos") List<MetodoPagoDTO> pagos) {

}
