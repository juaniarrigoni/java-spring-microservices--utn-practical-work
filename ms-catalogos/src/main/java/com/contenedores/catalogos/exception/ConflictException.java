package com.contenedores.catalogos.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends CatalogoException {

    public ConflictException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
