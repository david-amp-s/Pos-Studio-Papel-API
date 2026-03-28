package com.posstudio.papel.service;

import com.posstudio.papel.dto.request.CategoriaResquestDTO;
import com.posstudio.papel.dto.responsive.CategoriaResponsiveDTO;
import com.posstudio.papel.model.Categoria;

public interface CategoriaService {

    CategoriaResponsiveDTO crearCategoria(CategoriaResquestDTO data);

    CategoriaResponsiveDTO editarCategoria(Long id, CategoriaResquestDTO data);

    void eliminarCategoria(Long id);

    Categoria findByNombre(String nombre);

    Categoria findById(Long id);
}
