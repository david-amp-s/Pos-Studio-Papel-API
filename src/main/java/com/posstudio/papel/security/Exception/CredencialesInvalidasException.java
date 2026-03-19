package com.posstudio.papel.security.Exception;

public class CredencialesInvalidasException extends RuntimeException {
    public CredencialesInvalidasException(String e) {
        super("Credenciales invalidas : " + e);
    }
}
