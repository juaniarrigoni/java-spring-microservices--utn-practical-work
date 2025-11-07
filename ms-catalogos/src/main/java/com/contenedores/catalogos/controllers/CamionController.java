package com.contenedores.catalogos.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.contenedores.catalogos.dto.CamionRequest;
import com.contenedores.catalogos.dto.CamionResponse;
import com.contenedores.catalogos.service.CamionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/camiones")
public class CamionController {

    private final CamionService camionService;

    public CamionController(CamionService camionService) {
        this.camionService = camionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CamionResponse create(@Valid @RequestBody CamionRequest request) {
        return camionService.create(request);
    }

    @GetMapping
    public List<CamionResponse> list() {
        return camionService.list();
    }

    @GetMapping("/{id}")
    public CamionResponse get(@PathVariable Long id) {
        return camionService.get(id);
    }

    @PutMapping("/{id}")
    public CamionResponse update(@PathVariable Long id, @Valid @RequestBody CamionRequest request) {
        return camionService.update(id, request);
    }

    @PatchMapping("/{id}/activar")
    public CamionResponse activate(@PathVariable Long id) {
        return camionService.activate(id);
    }

    @PatchMapping("/{id}/desactivar")
    public CamionResponse deactivate(@PathVariable Long id) {
        return camionService.deactivate(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        camionService.delete(id);
    }
}
