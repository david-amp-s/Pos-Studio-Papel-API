package com.posstudio.papel.inventario.service;

import com.posstudio.papel.inventario.dto.request.MovimientoInventarioRequestDTO;

public interface MovimientoInventarioService {
    void guardar(MovimientoInventarioRequestDTO movimiento);
}
