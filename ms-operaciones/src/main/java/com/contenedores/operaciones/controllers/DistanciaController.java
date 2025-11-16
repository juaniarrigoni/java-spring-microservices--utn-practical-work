package com.contenedores.operaciones.controllers;

import com.contenedores.operaciones.dto.osrm.DistanciaRequest;
import com.contenedores.operaciones.dto.osrm.DistanciaResponse;
import com.contenedores.operaciones.service.DistanciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para calcular distancias entre puntos usando OSRM
 */
@RestController
@SecurityRequirement(name = "Keycloak")
@RequestMapping("/distancias")
@RequiredArgsConstructor
@Tag(name = "Distancias", description = "Cálculo de distancias entre puntos usando OSRM")
public class DistanciaController {

    private final DistanciaService distanciaService;

    @Operation(summary = "Calcular distancia entre dos puntos",
               description = "Calcula la distancia y tiempo estimado entre origen y destino")
    @PostMapping("/calcular")
    public ResponseEntity<DistanciaResponse> calcularDistancia(
            @RequestBody DistanciaRequest request) {
        
        DistanciaResponse response = distanciaService.calcularDistancia(
                request.getOrigenNombre(),
                request.getOrigen(),
                request.getDestinoNombre(),
                request.getDestino()
        );

        if (response.getExitoso()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Calcular distancia origen a depósito")
    @PostMapping("/origen-deposito")
    public ResponseEntity<DistanciaResponse> calcularOrigenADeposito(
            @RequestBody DistanciaRequest request) {
        
        DistanciaResponse response = distanciaService.calcularOrigenADeposito(
                request.getOrigenNombre(),
                request.getOrigen(),
                request.getDestinoNombre(),
                request.getDestino()
        );

        return response.getExitoso() ? 
                ResponseEntity.ok(response) : 
                ResponseEntity.badRequest().body(response);
    }

    @Operation(summary = "Calcular distancia depósito a destino")
    @PostMapping("/deposito-destino")
    public ResponseEntity<DistanciaResponse> calcularDepositoADestino(
            @RequestBody DistanciaRequest request) {
        
        DistanciaResponse response = distanciaService.calcularDepositoADestino(
                request.getOrigenNombre(),
                request.getOrigen(),
                request.getDestinoNombre(),
                request.getDestino()
        );

        return response.getExitoso() ? 
                ResponseEntity.ok(response) : 
                ResponseEntity.badRequest().body(response);
    }

    @Operation(summary = "Calcular distancia entre depósitos")
    @PostMapping("/entre-depositos")
    public ResponseEntity<DistanciaResponse> calcularEntreDepositos(
            @RequestBody DistanciaRequest request) {
        
        DistanciaResponse response = distanciaService.calcularEntreDepositos(
                request.getOrigenNombre(),
                request.getOrigen(),
                request.getDestinoNombre(),
                request.getDestino()
        );

        return response.getExitoso() ? 
                ResponseEntity.ok(response) : 
                ResponseEntity.badRequest().body(response);
    }

    @Operation(summary = "Calcular distancia directa origen-destino")
    @PostMapping("/directa")
    public ResponseEntity<DistanciaResponse> calcularDirecta(
            @RequestBody DistanciaRequest request) {
        
        DistanciaResponse response = distanciaService.calcularDirecto(
                request.getOrigenNombre(),
                request.getOrigen(),
                request.getDestinoNombre(),
                request.getDestino()
        );

        return response.getExitoso() ? 
                ResponseEntity.ok(response) : 
                ResponseEntity.badRequest().body(response);
    }

}
