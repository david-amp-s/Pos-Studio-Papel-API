package com.posstudio.papel.common.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, String id) {
        super(resource + " con id " + id + " no encontrado");
    }

}
