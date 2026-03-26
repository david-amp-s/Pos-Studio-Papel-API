package com.posstudio.papel.exception.productos;

public class ProductoExistenteException extends RuntimeException {
    public ProductoExistenteException() {
        super("El producto ya se encuentra registrado");
    }
}
