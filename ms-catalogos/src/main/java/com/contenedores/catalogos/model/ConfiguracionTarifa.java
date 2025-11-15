package com.contenedores.catalogos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad que almacena la configuración general de tarifas del sistema.
 * Solo puede haber una configuración activa a la vez.
 */
@Entity
@Table(name = "configuracion_tarifas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracionTarifa {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotBlank(message = "El nombre de la configuración es obligatorio")
    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @NotNull(message = "El precio del litro de combustible es obligatorio")
    @Positive(message = "El precio del litro de combustible debe ser positivo")
    @Column(name = "precio_litro_combustible", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioLitroCombustible;

    @NotNull(message = "El cargo de gestión por tramo es obligatorio")
    @Positive(message = "El cargo de gestión por tramo debe ser positivo")
    @Column(name = "cargo_gestion_por_tramo", nullable = false, precision = 10, scale = 2)
    private BigDecimal cargoGestionPorTramo;

    @NotNull(message = "La velocidad promedio es obligatoria")
    @Positive(message = "La velocidad promedio debe ser positiva")
    @Column(name = "velocidad_promedio_km_h", nullable = false, precision = 5, scale = 2)
    private BigDecimal velocidadPromedioKmH;

    @NotNull(message = "El costo de estadía diario por defecto es obligatorio")
    @Positive(message = "El costo de estadía diario debe ser positivo")
    @Column(name = "costo_estadia_diario_default", nullable = false, precision = 10, scale = 2)
    private BigDecimal costoEstadiaDiarioDefault;

    @Column(name = "activa", nullable = false)
    private Boolean activa = true;

    @Column(name = "vigencia_desde")
    private LocalDateTime vigenciaDesde;

    @Column(name = "vigencia_hasta")
    private LocalDateTime vigenciaHasta;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        fechaCreacion = LocalDateTime.now();
        if (vigenciaDesde == null) {
            vigenciaDesde = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        fechaModificacion = LocalDateTime.now();
    }
}
