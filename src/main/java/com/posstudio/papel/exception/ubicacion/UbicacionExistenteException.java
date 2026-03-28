package com.posstudio.papel.exception.ubicacion;

public class UbicacionExistenteException extends RuntimeException {
    public UbicacionExistenteException() {
        super("El codigo de la ubicacion ya existe");
    }
}
