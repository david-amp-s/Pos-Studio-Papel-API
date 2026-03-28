package com.posstudio.papel.exception.categoria;

public class CategoriaExistenteException extends RuntimeException {
    public CategoriaExistenteException() {
        super("La categoria ya se encuentra registrada");
    }
}
