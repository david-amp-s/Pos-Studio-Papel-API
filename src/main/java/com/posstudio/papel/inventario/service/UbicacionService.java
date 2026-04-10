package com.posstudio.papel.inventario.service;

import java.util.List;

import com.posstudio.papel.inventario.dto.request.UbicacionRequestDTO;
import com.posstudio.papel.inventario.dto.responsive.UbicacionResponsiveDTO;
import com.posstudio.papel.inventario.model.Ubicacion;

public interface UbicacionService {
    UbicacionResponsiveDTO crearUbicacion(UbicacionRequestDTO data);

    UbicacionResponsiveDTO editarUbicacion(Long id, UbicacionRequestDTO data);

    List<UbicacionResponsiveDTO> listarUbicaciones();

    Ubicacion findByCodigo(String codigo);

    Ubicacion findById(Long id);
}
