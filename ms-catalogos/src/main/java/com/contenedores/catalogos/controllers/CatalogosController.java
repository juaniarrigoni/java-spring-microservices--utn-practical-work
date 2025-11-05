package com.contenedores.catalogos.controllers;

import java.time.Instant;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.contenedores.catalogos.dto.ServiceInfo;

@RestController
public class CatalogosController {

    @GetMapping("/version")
    public ServiceInfo version() {
        return new ServiceInfo("ms-catalogos", "0.1.0", Instant.now());
    }
}
