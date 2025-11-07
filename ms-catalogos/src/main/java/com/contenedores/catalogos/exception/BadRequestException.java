package com.contenedores.catalogos.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends CatalogoException {

    public BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
