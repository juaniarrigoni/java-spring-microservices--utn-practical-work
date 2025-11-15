# Integración OSRM - ms-operaciones

## Resumen

Se integró **OSRM (Open Source Routing Machine)** en el microservicio `ms-operaciones` para calcular distancias reales entre puntos usando rutas terrestres de Argentina.

## Archivos creados

### DTOs (`dto/osrm/`)
- ✅ `Coordenada.java` - Ya existía, representa lat/lng
- ✅ `OsrmResponse.java` - Respuesta de la API OSRM
- ✅ `DistanciaRequest.java` - Request para calcular distancia
- ✅ `DistanciaResponse.java` - Response con distancia calculada

### Cliente y Servicios
- ✅ `OsrmClient.java` - Cliente HTTP para consumir OSRM
- ✅ `DistanciaService.java` - Lógica de negocio para cálculos

### Controlador
- ✅ `DistanciaController.java` - Endpoints REST

### Configuración
- ✅ `application.yml` - URL de OSRM local: `http://localhost:5000`
- ✅ `application-docker.yml` - URL de OSRM en Docker: `http://osrm-backend:5000`

## Casos de uso implementados

### 1. Calcular distancia origen → depósito
```bash
POST http://localhost:8083/distancias/origen-deposito
{
  "origenNombre": "Puerto Rosario",
  "origen": {
    "latitud": -32.9512,
    "longitud": -60.6669
  },
  "destinoNombre": "Córdoba",
  "destino": {
    "latitud": -31.4201,
    "longitud": -64.1888
  }
}
```

### 2. Calcular distancia depósito → destino
```bash
POST http://localhost:8083/distancias/deposito-destino
{
  "origenNombre": "Córdoba",
  "origen": {
    "latitud": -31.4201,
    "longitud": -64.1888
  },
  "destinoNombre": "Buenos Aires",
  "destino": {
    "latitud": -34.6037,
    "longitud": -58.3816
  }
}
```

### 3. Calcular distancia entre depósitos
```bash
POST http://localhost:8083/distancias/entre-depositos
{
  "origenNombre": "Rosario",
  "origen": {
    "latitud": -32.9468,
    "longitud": -60.6393
  },
  "destinoNombre": "Mendoza",
  "destino": {
    "latitud": -32.8895,
    "longitud": -68.8458
  }
}
```

### 4. Calcular distancia directa (sin depósito)
```bash
POST http://localhost:8083/distancias/directa
{
  "origenNombre": "Origen Cliente",
  "origen": {
    "latitud": -34.6037,
    "longitud": -58.3816
  },
  "destinoNombre": "Destino Final",
  "destino": {
    "latitud": -31.4201,
    "longitud": -64.1888
  }
}
```

### 5. Calcular distancia genérica
```bash
POST http://localhost:8083/distancias/calcular
{
  "origenNombre": "Punto A",
  "origen": {
    "latitud": -34.6037,
    "longitud": -58.3816
  },
  "destinoNombre": "Punto B",
  "destino": {
    "latitud": -31.4201,
    "longitud": -64.1888
  }
}
```

## Respuesta exitosa

```json
{
  "origenNombre": "Puerto Rosario",
  "destinoNombre": "Depósito Córdoba",
  "origen": {
    "latitud": -32.9512,
    "longitud": -60.6669
  },
  "destino": {
    "latitud": -31.4201,
    "longitud": -64.1888
  },
  "distanciaKm": 347.5,
  "duracionMinutos": 258.3,
  "duracionHoras": 4.305,
  "exitoso": true,
  "mensaje": "Distancia calculada exitosamente"
}
```

## Respuesta con error

```json
{
  "exitoso": false,
  "mensaje": "No se pudo calcular la ruta"
}
```

## Uso programático en otros servicios

```java
@Autowired
private DistanciaService distanciaService;

// Calcular solo la distancia en km
Coordenada origen = new Coordenada(-34.6037, -58.3816);
Coordenada destino = new Coordenada(-31.4201, -64.1888);
Double distanciaKm = distanciaService.calcularDistanciaKm(origen, destino);

// Calcular con respuesta completa
DistanciaResponse response = distanciaService.calcularDistancia(
    "Buenos Aires", origen,
    "Córdoba", destino
);
```

## Integración con modelos Ruta y Tramo

Los modelos `Ruta` y `Tramo` ya tienen campos para almacenar distancias:

```java
// En Ruta
private BigDecimal distanciaKmPlan;
private Integer duracionMinPlan;

// En Tramo
private BigDecimal distanciaKmPlan;
private Integer duracionMinPlan;
```

Puedes usarlos así:

```java
DistanciaResponse dist = distanciaService.calcularOrigenADeposito(
    "Origen", coordOrigen,
    "Deposito", coordDeposito
);

Tramo tramo = Tramo.builder()
    .origenNombre("Origen")
    .origenLat(BigDecimal.valueOf(coordOrigen.getLatitud()))
    .origenLng(BigDecimal.valueOf(coordOrigen.getLongitud()))
    .destinoNombre("Depósito")
    .destinoLat(BigDecimal.valueOf(coordDeposito.getLatitud()))
    .destinoLng(BigDecimal.valueOf(coordDeposito.getLongitud()))
    .distanciaKmPlan(BigDecimal.valueOf(dist.getDistanciaKm()))
    .duracionMinPlan(dist.getDuracionMinutos().intValue())
    .build();
```

## Testing

Para probar los endpoints accede a Swagger:
```
http://localhost:8083/swagger-ui/index.html
```

## Requisitos

1. **OSRM debe estar corriendo** en `http://localhost:5000` (local) o `http://osrm-backend:5000` (Docker)
2. **Datos de Argentina procesados** en `/d/osrm-data/`

## Verificar disponibilidad de OSRM

```bash
# Directo a OSRM (ejemplo Córdoba → Buenos Aires)
curl "http://localhost:5000/route/v1/driving/-64.1888,-31.4201;-58.3816,-34.6037?overview=false"
```

## Levantar todo con Docker

```bash
cd docker
docker compose up -d
```

Esto levantará:
- PostgreSQL
- OSRM Backend
- ms-catalogos
- ms-solicitudes
- ms-operaciones (con integración OSRM)
- api-gateway
