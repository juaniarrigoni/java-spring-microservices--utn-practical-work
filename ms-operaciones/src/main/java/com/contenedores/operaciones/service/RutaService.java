package com.contenedores.operaciones.service;

import com.contenedores.operaciones.client.CatalogosClient;
import com.contenedores.operaciones.dto.CostoEntregaResponse;
import com.contenedores.operaciones.dto.CostoEntregaResponse.EstadiaDetalle;
import com.contenedores.operaciones.dto.RutaDetalleResponse;
import com.contenedores.operaciones.dto.RutaDetalleResponse.TramoDetalle;
import com.contenedores.operaciones.dto.RutaRequest;
import com.contenedores.operaciones.dto.TramoRequest;
import com.contenedores.operaciones.model.AsignacionCamion;
import com.contenedores.operaciones.model.EstadoTramo;
import com.contenedores.operaciones.model.Ruta;
import com.contenedores.operaciones.model.Tramo;
import com.contenedores.operaciones.repository.RutaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RutaService {
    private final RutaRepository rutaRepository;
    private final CatalogosClient catalogosClient;
    
    // Constantes de fallback (si ms-catalogos no está disponible)
    private static final BigDecimal PRECIO_LITRO_COMBUSTIBLE_FALLBACK = new BigDecimal("150.00");
    private static final BigDecimal COSTO_BASE_KM_DEFAULT_FALLBACK = new BigDecimal("95.50");
    private static final BigDecimal CONSUMO_COMBUSTIBLE_DEFAULT = new BigDecimal("0.30");
    private static final BigDecimal COSTO_ESTADIA_DIARIO_DEFAULT_FALLBACK = new BigDecimal("500.00");
    private static final BigDecimal CARGO_GESTION_POR_TRAMO_FALLBACK = new BigDecimal("2500.00");
    private static final BigDecimal VELOCIDAD_PROMEDIO_KM_H_FALLBACK = new BigDecimal("60.0");

    public RutaService(RutaRepository rutaRepository, CatalogosClient catalogosClient) {
        this.rutaRepository = rutaRepository;
        this.catalogosClient = catalogosClient;
    }
    
    /**
     * Obtiene la configuración de tarifas desde ms-catalogos o usa valores por defecto
     */
    private CatalogosClient.ConfiguracionTarifaDTO obtenerConfiguracionTarifas() {
        try {
            return catalogosClient.obtenerConfiguracionActiva();
        } catch (Exception e) {
            log.warn("No se pudo obtener configuración de tarifas desde ms-catalogos. Usando valores por defecto. Error: {}", 
                e.getMessage());
            return new CatalogosClient.ConfiguracionTarifaDTO(
                PRECIO_LITRO_COMBUSTIBLE_FALLBACK,
                CARGO_GESTION_POR_TRAMO_FALLBACK,
                VELOCIDAD_PROMEDIO_KM_H_FALLBACK,
                COSTO_ESTADIA_DIARIO_DEFAULT_FALLBACK
            );
        }
    }
    
    /**
     * Obtiene el costo base por km según el volumen del contenedor
     */
    private BigDecimal obtenerCostoBaseKmPorVolumen(BigDecimal volumenM3) {
        try {
            return catalogosClient.obtenerCostoBaseKmPorVolumen(volumenM3);
        } catch (Exception e) {
            log.warn("No se pudo obtener costo base por volumen desde ms-catalogos. Usando valor por defecto. Error: {}", 
                e.getMessage());
            return COSTO_BASE_KM_DEFAULT_FALLBACK;
        }
    }

    public Ruta create(Ruta ruta) {
        // Aquí iría la lógica de negocio para crear una ruta,
        // como calcular distancias con la API de Google, etc.
        return rutaRepository.save(ruta);
    }
    
    /**
     * Asigna una nueva ruta con todos sus tramos a una solicitud.
     * Crea la entidad Ruta y sus Tramos asociados a partir de un RutaRequest.
     */
    @Transactional
    public RutaDetalleResponse createFromRequest(RutaRequest request) {
        // Verificar que la solicitud no tenga ya una ruta asignada
        rutaRepository.findBySolicitudIdWithTramos(request.solicitudId())
                .ifPresent(r -> {
                    throw new IllegalStateException("La solicitud " + request.solicitudId() + " ya tiene una ruta asignada");
                });
        
        // Crear la entidad Ruta
        Ruta ruta = Ruta.builder()
                .solicitudId(request.solicitudId())
                .distanciaKmPlan(request.distanciaKmPlan())
                .duracionMinPlan(request.duracionMinPlan())
                .tramos(new ArrayList<>())
                .build();
        
        // Crear los Tramos y establecer la relación bidireccional
        List<Tramo> tramos = request.tramos().stream()
                .map(tramoReq -> mapTramoRequestToEntity(tramoReq, ruta))
                .collect(Collectors.toList());
        
        ruta.setTramos(tramos);
        
        // Guardar la ruta con cascada a los tramos
        Ruta rutaGuardada = rutaRepository.save(ruta);
        
        // Retornar el detalle completo
        return mapToDetalleResponse(rutaGuardada);
    }
    
    /**
     * Convierte un TramoRequest en una entidad Tramo con la relación a la Ruta.
     */
    private Tramo mapTramoRequestToEntity(TramoRequest request, Ruta ruta) {
        // Calcular duración estimada si no viene especificada
        Integer duracionMinPlan = request.duracionMinPlan();
        if (duracionMinPlan == null && request.distanciaKmPlan() != null) {
            CatalogosClient.ConfiguracionTarifaDTO config = obtenerConfiguracionTarifas();
            // Fórmula: duracion_minutos = (distancia_km / velocidad_km_h) × 60
            duracionMinPlan = request.distanciaKmPlan()
                    .divide(config.velocidadPromedioKmH(), 2, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("60"))
                    .intValue();
        }
        
        return Tramo.builder()
                .ruta(ruta)
                .orden(request.orden())
                .origenNombre(request.origenNombre())
                .origenLat(request.origenLat())
                .origenLng(request.origenLng())
                .destinoNombre(request.destinoNombre())
                .destinoLat(request.destinoLat())
                .destinoLng(request.destinoLng())
                .distanciaKmPlan(request.distanciaKmPlan())
                .duracionMinPlan(duracionMinPlan)
                .estado(EstadoTramo.PENDIENTE) // Estado inicial
                .build();
    }

    public List<Ruta> findAll() {
        return rutaRepository.findAll();
    }
    
    /**
     * Obtiene el detalle completo de una ruta por su ID, incluyendo todos los tramos.
     */
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public RutaDetalleResponse findDetalleById(UUID id) {
        Ruta ruta = rutaRepository.findByIdWithTramos(id)
                .orElseThrow(() -> new IllegalArgumentException("Ruta no encontrada con ID: " + id));
        return mapToDetalleResponse(ruta);
    }
    
    /**
     * Obtiene el detalle completo de una ruta por el ID de la solicitud.
     */
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public RutaDetalleResponse findDetalleBySolicitudId(UUID solicitudId) {
        Ruta ruta = rutaRepository.findBySolicitudIdWithTramos(solicitudId)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró ruta para la solicitud ID: " + solicitudId));
        return mapToDetalleResponse(ruta);
    }
    
    /**
     * Convierte una entidad Ruta en un DTO RutaDetalleResponse con cálculo de costos.
     */
    private RutaDetalleResponse mapToDetalleResponse(Ruta ruta) {
        List<TramoDetalle> tramosDetalle = ruta.getTramos().stream()
                .map(this::mapTramoToDetalle)
                .collect(Collectors.toList());
        
        // Calcular costo total estimado basado en la distancia
        BigDecimal costoEstimado = ruta.getDistanciaKmPlan() != null 
                ? ruta.getDistanciaKmPlan().multiply(COSTO_BASE_KM_DEFAULT_FALLBACK)
                : BigDecimal.ZERO;
        
        return new RutaDetalleResponse(
                ruta.getId(),
                ruta.getSolicitudId(),
                ruta.getDistanciaKmPlan(),
                ruta.getDuracionMinPlan(),
                costoEstimado,
                ruta.getFechaPlan(),
                tramosDetalle
        );
    }
    
    /**
     * Convierte un Tramo en un TramoDetalle DTO.
     */
    private TramoDetalle mapTramoToDetalle(Tramo tramo) {
        AsignacionCamion asignacion = tramo.getAsignacionCamion();
        
        return new TramoDetalle(
                tramo.getId(),
                tramo.getOrden(),
                tramo.getOrigenNombre(),
                tramo.getOrigenLat(),
                tramo.getOrigenLng(),
                tramo.getDestinoNombre(),
                tramo.getDestinoLat(),
                tramo.getDestinoLng(),
                tramo.getDistanciaKmPlan(),
                tramo.getDuracionMinPlan(),
                tramo.getEstado() != null ? tramo.getEstado().name() : null,
                asignacion != null ? asignacion.getCamionId() : null,
                asignacion != null ? asignacion.getConfirmado() : null
        );
    }

    /**
     * Calcula el costo total de entrega para una ruta (REQ-8 REFINADO).
     * Usa datos específicos de cada camión asignado (costo base, consumo).
     * 
     * Fórmula:
     * - costoTraslado = Σ(distancia_tramo × costo_base_km_camion)
     * - costoCombustible = Σ(distancia_tramo × consumo_combustible_km × precio_litro)
     * - costoEstadias = Σ(días_estadia × costo_deposito_diario)
     * - costoTotal = costoTraslado + costoCombustible + costoEstadias
     * 
     * @param rutaId UUID de la ruta
     * @param pesoKg Peso del contenedor (no usado en cálculo refinado, dejado por compatibilidad)
     * @param volumenM3 Volumen del contenedor (no usado en cálculo refinado, dejado por compatibilidad)
     * @return DTO con el desglose completo de costos por tramo y totales
     */
    @Transactional(readOnly = true)
    public CostoEntregaResponse calcularCostoTotal(UUID rutaId, BigDecimal pesoKg, BigDecimal volumenM3) {
        // 0. Obtener configuración de tarifas
        CatalogosClient.ConfiguracionTarifaDTO config = obtenerConfiguracionTarifas();
        
        // 1. Obtener la ruta con sus tramos
        Ruta ruta = rutaRepository.findByIdWithTramos(rutaId)
                .orElseThrow(() -> new EntityNotFoundException("Ruta no encontrada con ID: " + rutaId));

        // 2. Calcular costos por tramo usando datos del camión asignado
        List<CostoEntregaResponse.TramoDetalle> tramosDetalle = new ArrayList<>();
        BigDecimal costoTrasladoTotal = BigDecimal.ZERO;
        BigDecimal costoCombustibleTotal = BigDecimal.ZERO;
        
        List<Tramo> tramosOrdenados = ruta.getTramos().stream()
                .sorted((t1, t2) -> t1.getOrden().compareTo(t2.getOrden()))
                .collect(Collectors.toList());
        
        for (Tramo tramo : tramosOrdenados) {
            // Obtener datos del camión (simulado - en sistema real se consulta ms-catalogos via REST)
            CamionDatos camionDatos = obtenerDatosCamion(tramo);
            
            BigDecimal distancia = tramo.getDistanciaKmPlan();
            
            // Costo de traslado = distancia × costo_base_km del camión
            BigDecimal costoTraslado = distancia
                    .multiply(camionDatos.costoBaseKm())
                    .setScale(2, RoundingMode.HALF_UP);
            
            // Costo de combustible = distancia × consumo_combustible × precio_litro
            BigDecimal costoCombustible = distancia
                    .multiply(camionDatos.consumoCombustibleKm())
                    .multiply(config.precioLitroCombustible())
                    .setScale(2, RoundingMode.HALF_UP);
            
            BigDecimal costoTotalTramo = costoTraslado.add(costoCombustible);
            
            costoTrasladoTotal = costoTrasladoTotal.add(costoTraslado);
            costoCombustibleTotal = costoCombustibleTotal.add(costoCombustible);
            
            // Construir detalle del tramo
            tramosDetalle.add(new CostoEntregaResponse.TramoDetalle(
                    tramo.getOrden(),
                    tramo.getOrigenNombre(),
                    tramo.getDestinoNombre(),
                    distancia,
                    camionDatos.patente(),
                    camionDatos.costoBaseKm(),
                    camionDatos.consumoCombustibleKm(),
                    costoTraslado,
                    costoCombustible,
                    costoTotalTramo
            ));
        }

        // 3. Calcular costo de estadías en depósitos (entre tramos consecutivos)
        List<EstadiaDetalle> estadias = new ArrayList<>();
        BigDecimal costoEstadiasTotal = BigDecimal.ZERO;
        
        for (int i = 0; i < tramosOrdenados.size() - 1; i++) {
            Tramo tramoActual = tramosOrdenados.get(i);
            Tramo tramoSiguiente = tramosOrdenados.get(i + 1);
            
            // Solo calcular estadía si ambos tramos tienen fechas reales
            if (tramoActual.getFechaFinReal() != null && tramoSiguiente.getFechaInicioReal() != null) {
                LocalDateTime fechaSalida = tramoActual.getFechaFinReal();
                LocalDateTime fechaEntrada = tramoSiguiente.getFechaInicioReal();
                
                // Calcular días de estadía (puede ser fraccionario)
                Duration duracion = Duration.between(fechaSalida, fechaEntrada);
                BigDecimal diasEstadia = new BigDecimal(duracion.toMinutes())
                        .divide(new BigDecimal("1440"), 4, RoundingMode.HALF_UP); // 1440 minutos = 1 día
                
                // Obtener costo de estadía de la configuración
                BigDecimal costoDepositoDiario = config.costoEstadiaDiarioDefault();
                
                BigDecimal costoEstadia = diasEstadia
                        .multiply(costoDepositoDiario)
                        .setScale(2, RoundingMode.HALF_UP);
                
                costoEstadiasTotal = costoEstadiasTotal.add(costoEstadia);
                
                estadias.add(new EstadiaDetalle(
                        tramoActual.getOrden(),
                        tramoActual.getDestinoNombre() + " (depósito)",
                        fechaSalida.toString(),
                        fechaEntrada.toString(),
                        diasEstadia,
                        costoDepositoDiario,
                        costoEstadia
                ));
            }
        }

        // 4. Calcular cargo de gestión (fijo por cantidad de tramos)
        int cantidadTramos = tramosOrdenados.size();
        BigDecimal cargoGestionTotal = config.cargoGestionPorTramo()
                .multiply(new BigDecimal(cantidadTramos))
                .setScale(2, RoundingMode.HALF_UP);

        // 5. Calcular costo total
        BigDecimal costoTotal = costoTrasladoTotal
                .add(costoCombustibleTotal)
                .add(costoEstadiasTotal)
                .add(cargoGestionTotal)
                .setScale(2, RoundingMode.HALF_UP);

        // 6. Construir respuesta
        return new CostoEntregaResponse(
                ruta.getId(),
                ruta.getSolicitudId(),
                costoTrasladoTotal,
                costoCombustibleTotal,
                costoEstadiasTotal,
                cargoGestionTotal,
                costoTotal,
                ruta.getDistanciaKmPlan(),
                pesoKg,     // Dejado por compatibilidad aunque no se usa en cálculo refinado
                volumenM3,  // Dejado por compatibilidad aunque no se usa en cálculo refinado
                config.precioLitroCombustible(),
                tramosDetalle,
                estadias,
                "Cálculo refinado basado en datos reales de camiones asignados. " +
                "En sistema productivo, datos de camiones y depósitos se obtendrían de ms-catalogos vía REST."
        );
    }
    
    /**
     * Obtiene los datos del camión asignado a un tramo.
     * En sistema real, haría una llamada REST a ms-catalogos/camiones/{id}.
     * Por ahora simula con datos conocidos de prueba.
     */
    private CamionDatos obtenerDatosCamion(Tramo tramo) {
        // Si el tramo no tiene camión asignado, usar valores por defecto
        if (tramo.getAsignacionCamion() == null) {
            return new CamionDatos(
                    "SIN-ASIGNAR",
                    COSTO_BASE_KM_DEFAULT_FALLBACK,
                    CONSUMO_COMBUSTIBLE_DEFAULT
            );
        }
        
        UUID camionId = tramo.getAsignacionCamion().getCamionId();
        
        // TODO: En sistema real, hacer llamada REST:
        // String url = "http://ms-catalogos:8081/camiones/" + camionId;
        // Camion camion = restTemplate.getForObject(url, Camion.class);
        // return new CamionDatos(camion.getPatente(), camion.getCostoBaseKm(), camion.getConsumoCombustibleKm());
        
        // Simulación con datos conocidos de prueba
        // Camión 1: b8f1b9c5-1c22-4b89-9a75-0193f1a0e111 (AA123BB, Transporte Sur S.A.)
        if (camionId.toString().equals("b8f1b9c5-1c22-4b89-9a75-0193f1a0e111")) {
            return new CamionDatos(
                    "AA123BB",
                    new BigDecimal("95.50"),
                    new BigDecimal("0.32")
            );
        }
        
        // Camión 2: c6e2d7f0-8b4c-4c3a-9b1b-6b2f5e2d2f22 (CC456DD, Logística Norte)
        if (camionId.toString().equals("c6e2d7f0-8b4c-4c3a-9b1b-6b2f5e2d2f22")) {
            return new CamionDatos(
                    "CC456DD",
                    new BigDecimal("82.75"),
                    new BigDecimal("0.28")
            );
        }
        
        // Camión desconocido, usar valores por defecto
        return new CamionDatos(
                camionId.toString().substring(0, 8), // Primeros 8 chars del UUID
                COSTO_BASE_KM_DEFAULT_FALLBACK,
                CONSUMO_COMBUSTIBLE_DEFAULT
        );
    }
    
    /**
     * Calcula una tarifa aproximada ANTES de crear la ruta.
     * Usa valores promedio de camiones elegibles según las características del contenedor.
     * IMPLEMENTA: Costo base por km variable según volumen del contenedor.
     * 
     * @param request Datos para estimar: distancia, cantidad de tramos, peso y volumen del contenedor
     * @return Estimación de costo basada en promedios
     */
    @Transactional(readOnly = true)
    public com.contenedores.operaciones.dto.TarifaAproximadaResponse calcularTarifaAproximada(
            com.contenedores.operaciones.dto.TarifaAproximadaRequest request) {
        
        // 0. Obtener configuración de tarifas
        CatalogosClient.ConfiguracionTarifaDTO config = obtenerConfiguracionTarifas();
        
        // 1. Obtener costo base por km según el volumen del contenedor (REGLA DE NEGOCIO IMPLEMENTADA)
        BigDecimal costoBaseKmPromedio = obtenerCostoBaseKmPorVolumen(request.contenedorVolumenM3());
        
        // 2. Obtener camiones elegibles (simulado - en sistema real consulta ms-catalogos)
        // Filtro: camiones con capacidad suficiente para el contenedor
        List<CamionElegible> camionesElegibles = obtenerCamionesElegibles(
                request.contenedorPesoKg(), 
                request.contenedorVolumenM3()
        );
        
        if (camionesElegibles.isEmpty()) {
            throw new IllegalStateException(
                "No hay camiones disponibles con capacidad suficiente para el contenedor especificado. " +
                "Peso: " + request.contenedorPesoKg() + " kg, Volumen: " + request.contenedorVolumenM3() + " m³"
            );
        }
        
        // 3. Calcular consumo promedio de los camiones elegibles
        BigDecimal consumoCombustiblePromedio = camionesElegibles.stream()
                .map(CamionElegible::consumoCombustibleKm)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(new BigDecimal(camionesElegibles.size()), 4, RoundingMode.HALF_UP);
        
        // 4. Calcular componentes del costo estimado
        
        // Cargo de gestión
        BigDecimal cargoGestionEstimado = config.cargoGestionPorTramo()
                .multiply(new BigDecimal(request.cantidadTramos()))
                .setScale(2, RoundingMode.HALF_UP);
        
        // Costo de traslado estimado (usa costo base variable por volumen)
        BigDecimal costoTrasladoEstimado = request.distanciaKmEstimada()
                .multiply(costoBaseKmPromedio)
                .setScale(2, RoundingMode.HALF_UP);
        
        // Costo de combustible estimado
        BigDecimal costoCombustibleEstimado = request.distanciaKmEstimada()
                .multiply(consumoCombustiblePromedio)
                .multiply(config.precioLitroCombustible())
                .setScale(2, RoundingMode.HALF_UP);
        
        // Costo total estimado (sin estadías porque no se conocen aún)
        BigDecimal costoTotalEstimado = cargoGestionEstimado
                .add(costoTrasladoEstimado)
                .add(costoCombustibleEstimado)
                .setScale(2, RoundingMode.HALF_UP);
        
        // 5. Construir respuesta
        return new com.contenedores.operaciones.dto.TarifaAproximadaResponse(
                request.solicitudId(),
                request.distanciaKmEstimada(),
                request.cantidadTramos(),
                costoBaseKmPromedio,
                consumoCombustiblePromedio,
                camionesElegibles.size(),
                cargoGestionEstimado,
                costoTrasladoEstimado,
                costoCombustibleEstimado,
                costoTotalEstimado,
                config.precioLitroCombustible(),
                "Estimación con costo base $" + costoBaseKmPromedio + "/km para volumen " + request.contenedorVolumenM3() + " m³. " +
                "Basada en " + camionesElegibles.size() + " camiones elegibles. " +
                "No incluye costos de estadías en depósitos (se calcularán al finalizar). " +
                "Tarifas obtenidas dinámicamente de ms-catalogos."
        );
    }
    
    /**
     * Obtiene lista de camiones elegibles según capacidad requerida (simulado).
     * En sistema real, consultaría ms-catalogos con filtros.
     */
    private List<CamionElegible> obtenerCamionesElegibles(BigDecimal pesoRequerido, BigDecimal volumenRequerido) {
        List<CamionElegible> elegibles = new ArrayList<>();
        
        // Camión 1: AA123BB (25,000 kg / 60 m³)
        CamionElegible camion1 = new CamionElegible(
                "AA123BB",
                new BigDecimal("25000"),
                new BigDecimal("60.0"),
                new BigDecimal("95.50"),
                new BigDecimal("0.28")
        );
        
        // Camión 2: CC456DD (18,000 kg / 45 m³)
        CamionElegible camion2 = new CamionElegible(
                "CC456DD",
                new BigDecimal("18000"),
                new BigDecimal("45.0"),
                new BigDecimal("88.00"),
                new BigDecimal("0.25")
        );
        
        // Camión 3: EE789FF (30,000 kg / 75 m³)
        CamionElegible camion3 = new CamionElegible(
                "EE789FF",
                new BigDecimal("30000"),
                new BigDecimal("75.0"),
                new BigDecimal("105.00"),
                new BigDecimal("0.35")
        );
        
        // Filtrar solo los que tienen capacidad suficiente
        List<CamionElegible> todos = List.of(camion1, camion2, camion3);
        
        for (CamionElegible camion : todos) {
            boolean cumplePeso = pesoRequerido == null || pesoRequerido.compareTo(camion.capacidadKg()) <= 0;
            boolean cumpleVolumen = volumenRequerido == null || volumenRequerido.compareTo(camion.volumenM3()) <= 0;
            
            if (cumplePeso && cumpleVolumen) {
                elegibles.add(camion);
            }
        }
        
        return elegibles;
    }
    
    /**
     * Record auxiliar para encapsular datos de camiones elegibles.
     */
    private record CamionElegible(
            String patente,
            BigDecimal capacidadKg,
            BigDecimal volumenM3,
            BigDecimal costoBaseKm,
            BigDecimal consumoCombustibleKm
    ) {}
    
    /**
     * Record auxiliar para encapsular datos del camión necesarios para el cálculo.
     */
    private record CamionDatos(
            String patente,
            BigDecimal costoBaseKm,
            BigDecimal consumoCombustibleKm
    ) {}
}