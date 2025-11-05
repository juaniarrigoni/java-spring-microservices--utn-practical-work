package com.contenedores.solicitudes.controllers;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.contenedores.solicitudes.dto.SolicitudRequest;
import com.contenedores.solicitudes.dto.SolicitudResponse;

@RestController
public class SolicitudesController {

    private final AtomicLong sequence = new AtomicLong();

    @PostMapping({"/", ""})
    @ResponseStatus(HttpStatus.CREATED)
    public SolicitudResponse crear(@RequestBody @Valid SolicitudRequest request) {
        long id = sequence.incrementAndGet();
        return new SolicitudResponse(id, request.solicitante(), request.descripcion(), "CREADA", Instant.now());
    }
}
