package com.posstudio.papel.inventario.service.impl;

import org.springframework.stereotype.Service;

import com.posstudio.papel.inventario.dto.request.MovimientoInventarioRequestDTO;
import com.posstudio.papel.inventario.model.MovimientoInventario;
import com.posstudio.papel.inventario.repository.MovimientoInventarioRepository;
import com.posstudio.papel.inventario.service.MovimientoInventarioService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovimientoInventarioServiceImp implements MovimientoInventarioService {
    private final MovimientoInventarioRepository movimientoInventarioRepository;

    @Override
    public void guardar(MovimientoInventarioRequestDTO movimiento) {
        MovimientoInventario = MovimientoInventario.builder()
    }

}
