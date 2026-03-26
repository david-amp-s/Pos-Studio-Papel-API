package com.posstudio.papel.service.impl;

import org.springframework.stereotype.Service;

import com.posstudio.papel.dto.request.CategoriaResquestDTO;
import com.posstudio.papel.dto.responsive.CategoriaResponsiveDTO;
import com.posstudio.papel.exception.categoria.CategoriaInexistenteException;
import com.posstudio.papel.model.Categoria;
import com.posstudio.papel.repository.CategoriaRepository;
import com.posstudio.papel.service.CategoriaService;

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
        throw new UnsupportedOperationException("Unimplemented method 'editarCategoria'");
    }

    @Override
    public CategoriaResponsiveDTO editarCategoria(Long id, CategoriaResponsiveDTO data) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'editarCategoria'");
    }

    @Override
    public void eliminarCategoria(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eliminarCategoria'");
    }

    @Override
    public Categoria findByNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre).orElseThrow(CategoriaInexistenteException::new);
    }

}
