package com.posstudio.papel.ventas.dto.validaciones;

import com.posstudio.papel.security.model.Usuario;
import com.posstudio.papel.turnos.model.Turno;

public record ValidacionesTienda(
        Usuario usuario,
        Turno turno) {

}
