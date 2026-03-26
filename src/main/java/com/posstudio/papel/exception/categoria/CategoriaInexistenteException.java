package com.posstudio.papel.exception.categoria;

public class CategoriaInexistenteException extends RuntimeException {
    public CategoriaInexistenteException() {
        super("Categoria no encontrada");
    }
}
