package com.contenedores.catalogos.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends CatalogoException {

    public ResourceNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
