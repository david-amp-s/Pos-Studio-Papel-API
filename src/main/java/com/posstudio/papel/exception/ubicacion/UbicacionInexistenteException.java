package com.posstudio.papel.exception.ubicacion;

public class UbicacionInexistenteException extends RuntimeException {
    public UbicacionInexistenteException() {
        super("La ubicacion no ha sido creada");
    }
}
