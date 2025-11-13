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
 * Entidad que almacena las tarifas variables según el volumen del contenedor.
 * Define rangos de volumen (min, max) y el costo base por km correspondiente.
 * Implementa la regla de negocio: "El costo por km depende del volumen del contenedor"
 */
@Entity
@Table(name = "tarifas_por_volumen",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_tarifa_volumen_rango_activa",
        columnNames = {"volumen_min_m3", "volumen_max_m3", "activa"}
    )
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarifaPorVolumen {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotBlank(message = "El nombre de la tarifa es obligatorio")
    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @NotNull(message = "El volumen mínimo es obligatorio")
    @Column(name = "volumen_min_m3", nullable = false, precision = 10, scale = 2)
    private BigDecimal volumenMinM3;

    @NotNull(message = "El volumen máximo es obligatorio")
    @Column(name = "volumen_max_m3", precision = 10, scale = 2)
    private BigDecimal volumenMaxM3; // null significa sin límite superior

    @NotNull(message = "El costo base por km es obligatorio")
    @Positive(message = "El costo base por km debe ser positivo")
    @Column(name = "costo_base_km", nullable = false, precision = 10, scale = 2)
    private BigDecimal costoBaseKm;

    @Column(name = "activa", nullable = false)
    private Boolean activa = true;

    @Column(name = "orden_prioridad", nullable = false)
    private Integer ordenPrioridad = 0;

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

    /**
     * Verifica si esta tarifa aplica para un volumen dado
     */
    public boolean aplicaParaVolumen(BigDecimal volumenM3) {
        if (volumenM3 == null || !activa) {
            return false;
        }
        
        boolean cumpleMin = volumenM3.compareTo(volumenMinM3) >= 0;
        boolean cumpleMax = volumenMaxM3 == null || volumenM3.compareTo(volumenMaxM3) < 0;
        
        return cumpleMin && cumpleMax;
    }
}
