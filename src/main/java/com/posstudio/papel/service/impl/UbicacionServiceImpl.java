package com.posstudio.papel.service.impl;

import org.springframework.stereotype.Service;

import com.posstudio.papel.dto.request.UbicacionRequestDTO;
import com.posstudio.papel.dto.responsive.UbicacionResponsiveDTO;
import com.posstudio.papel.exception.ubicacion.UbicacionExistenteException;
import com.posstudio.papel.exception.ubicacion.UbicacionInexistenteException;
import com.posstudio.papel.model.Ubicacion;
import com.posstudio.papel.repository.UbicacionRepository;
import com.posstudio.papel.service.UbicacionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UbicacionServiceImpl implements UbicacionService {
    private final UbicacionRepository ubicacionRepository;

    public UbicacionResponsiveDTO conversorDTO(Ubicacion ubicacion) {
        return new UbicacionResponsiveDTO(ubicacion.getId(), ubicacion.getCodigo());
    }

    @Override
    public UbicacionResponsiveDTO crearUbicacion(UbicacionRequestDTO data) {
        if (ubicacionRepository.findByCodigo(data.codigo()).isPresent()) {
            throw new UbicacionExistenteException();
        }
        Ubicacion ubicacion = Ubicacion.builder()
                .codigo(data.codigo())
                .build();
        ubicacionRepository.save(ubicacion);
        return conversorDTO(ubicacion);
    }

    @Override
    public UbicacionResponsiveDTO editarUbicacion(Long id, UbicacionRequestDTO data) {
        Ubicacion ubicacion = findById(id);
        ubicacion.setCodigo(data.codigo());
        ubicacionRepository.save(ubicacion);
        return conversorDTO(ubicacion);
    }

    @Override
    public Ubicacion findByCodigo(String codigo) {
        return ubicacionRepository.findByCodigo(codigo).orElseThrow(UbicacionInexistenteException::new);
    }

    @Override
    public Ubicacion findById(Long id) {
        return ubicacionRepository.findById(id).orElseThrow();
    }

}
