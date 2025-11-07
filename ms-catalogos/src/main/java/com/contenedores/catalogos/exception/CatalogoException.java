package com.contenedores.catalogos.exception;

import org.springframework.http.HttpStatus;

public class CatalogoException extends RuntimeException {

    private final HttpStatus status;

    public CatalogoException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
