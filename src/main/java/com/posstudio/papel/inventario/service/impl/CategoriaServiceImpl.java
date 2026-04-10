package com.posstudio.papel.inventario.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.posstudio.papel.common.exception.BusinessException;
import com.posstudio.papel.common.exception.ResourceNotFoundException;
import com.posstudio.papel.inventario.dto.request.CategoriaResquestDTO;
import com.posstudio.papel.inventario.dto.responsive.CategoriaResponsiveDTO;
import com.posstudio.papel.inventario.model.Categoria;
import com.posstudio.papel.inventario.repository.CategoriaRepository;
import com.posstudio.papel.inventario.service.CategoriaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {
    private final CategoriaRepository categoriaRepository;

    public CategoriaResponsiveDTO conversorDto(Categoria categoria) {
        return new CategoriaResponsiveDTO(categoria.getId(), categoria.getNombre());
    }

    @Override
    public CategoriaResponsiveDTO crearCategoria(CategoriaResquestDTO data) {
        if (categoriaRepository.findByNombre(data.nombre()).isPresent()) {
            throw new BusinessException("Categoria ya existe con ese nombre", 409);
        }
        Categoria categoria = Categoria.builder()
                .nombre(data.nombre())
                .build();
        categoriaRepository.save(categoria);
        return conversorDto(categoria);
    }

    @Override
    public CategoriaResponsiveDTO editarCategoria(Long id, CategoriaResquestDTO data) {
        Categoria categoria = findById(id);
        categoria.setNombre(data.nombre());
        categoriaRepository.save(categoria);
        return conversorDto(categoria);
    }

    @Override
    public Categoria findByNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre)
                .orElseThrow(() -> new ResourceNotFoundException("categoria", nombre));
    }

    @Override
    public Categoria findById(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("categoria", id.toString()));
    }

    @Override
    public List<CategoriaResponsiveDTO> listaCategoria() {
        return categoriaRepository.findAll().stream().map(this::conversorDto).toList();
    }

}
