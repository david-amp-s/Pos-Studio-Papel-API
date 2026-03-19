package com.posstudio.papel.security.Exception;

public class TokenExpitadoException extends RuntimeException {
    public TokenExpitadoException(String e) {
        super("token expirado : " + e);
    }
}
