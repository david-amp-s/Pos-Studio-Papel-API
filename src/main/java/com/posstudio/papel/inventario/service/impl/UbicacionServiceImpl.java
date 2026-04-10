package com.posstudio.papel.inventario.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.posstudio.papel.common.exception.BusinessException;
import com.posstudio.papel.common.exception.ResourceNotFoundException;
import com.posstudio.papel.inventario.dto.request.UbicacionRequestDTO;
import com.posstudio.papel.inventario.dto.responsive.UbicacionResponsiveDTO;
import com.posstudio.papel.inventario.model.Ubicacion;
import com.posstudio.papel.inventario.repository.UbicacionRepository;
import com.posstudio.papel.inventario.service.UbicacionService;

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
            throw new BusinessException("Ubicacion ya existe con ese nombre");
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
        return ubicacionRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Codigo", codigo));
    }

    @Override
    public Ubicacion findById(Long id) {
        return ubicacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Codigo", id.toString()));
    }

    @Override
    public List<UbicacionResponsiveDTO> listarUbicaciones() {
        return ubicacionRepository.findAll().stream().map(this::conversorDTO).toList();
    }

}
