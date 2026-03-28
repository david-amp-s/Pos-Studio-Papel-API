package com.posstudio.papel.service;

import com.posstudio.papel.dto.request.UbicacionRequestDTO;
import com.posstudio.papel.dto.responsive.UbicacionResponsiveDTO;
import com.posstudio.papel.model.Ubicacion;

public interface UbicacionService {
    UbicacionResponsiveDTO crearUbicacion(UbicacionRequestDTO data);

    UbicacionResponsiveDTO editarUbicacion(Long id, UbicacionRequestDTO data);

    Ubicacion findByCodigo(String codigo);

    Ubicacion findById(Long id);
}
