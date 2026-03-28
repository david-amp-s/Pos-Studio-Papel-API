package com.posstudio.papel.service.impl;

import org.springframework.stereotype.Service;

import com.posstudio.papel.dto.request.CategoriaResquestDTO;
import com.posstudio.papel.dto.responsive.CategoriaResponsiveDTO;
import com.posstudio.papel.exception.categoria.CategoriaExistenteException;
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
        if (categoriaRepository.findByNombre(data.nombre()).isPresent()) {
            throw new CategoriaExistenteException();
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
    public void eliminarCategoria(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eliminarCategoria'");
    }

    @Override
    public Categoria findByNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre).orElseThrow(CategoriaInexistenteException::new);
    }

    @Override
    public Categoria findById(Long id) {
        return categoriaRepository.findById(id).orElseThrow();
    }

}
