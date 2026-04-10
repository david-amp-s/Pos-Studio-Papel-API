package com.posstudio.papel.inventario.service;

import java.util.List;

import com.posstudio.papel.inventario.dto.request.CategoriaResquestDTO;
import com.posstudio.papel.inventario.dto.responsive.CategoriaResponsiveDTO;
import com.posstudio.papel.inventario.model.Categoria;

public interface CategoriaService {

    CategoriaResponsiveDTO crearCategoria(CategoriaResquestDTO data);

    CategoriaResponsiveDTO editarCategoria(Long id, CategoriaResquestDTO data);

    List<CategoriaResponsiveDTO> listaCategoria();

    Categoria findByNombre(String nombre);

    Categoria findById(Long id);
}
