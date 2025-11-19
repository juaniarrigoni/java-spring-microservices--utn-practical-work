## 📋 **PREREQUISITOS**

1. **Levantar el sistema completo:**

```bash
cd docker
docker-compose up -d
```

2. **Verificar que todos los servicios estén healthy:**

```bash
docker ps
```

Deberías ver:

- ✅ postgres (puerto 5433)
- ✅ keycloak (puerto 8084)
- ✅ osrm-backend (puerto 5000)
- ✅ ms-catalogos (puerto 8081)
- ✅ ms-solicitudes (puerto 8082)
- ✅ ms-operaciones (puerto 8083)
- ✅ api-gateway (puerto 8080)
3. **Acceder a Swagger UI:**
    
   `http://localhost:8080/swagger-ui.html`
    
   Aquí encontrarás todos los endpoints documentados.

---

## 🔐 **PASO 1: AUTENTICACIÓN PARA OBTENER TOKEN JWT**

**Endpoint:**

`POST http://localhost:8084/realms/tpi-backend/protocol/openid-connect/token`

**Headers:**

```
Content-Type: application/x-www-form-urlencoded

```

**Body (x-www-form-urlencoded):**

```
grant_type=password
client_id=tpi-client
username=operador01
password=pass123

```

**Respuesta:**

```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI...",
  "expires_in": 300,
  "refresh_token": "...",
  "token_type": "Bearer"
}

```

⚠️ **IMPORTANTE:**

1. Copia el `access_token`.
2. En Swagger, hacé clic en **Authorize**.
3. Pegalo en el campo.
4. Ahora todos los endpoints usarán ese token.

---

## 📦 **PASO 2: CREAR DATOS MAESTROS (CATÁLOGOS)**

---

### **2.1. Crear Depósitos**

**Endpoint:**

`POST /api/catalogos/depositos`

**Depósito 1**

```json
{
  "nombre": "Deposito Córdoba Centro",
  "direccion": "Av. Colón 1234, Córdoba",
  "latitud": -31.4201,
  "longitud": -64.1888,
  "costoEstadiaDiario": 500.00
}

```

**Depósito 2**

```json
{
  "nombre": "Deposito Villa María",
  "direccion": "Ruta 9 Km 563, Villa María",
  "latitud": -32.4084,
  "longitud": -63.2399,
  "costoEstadiaDiario": 400.00
}

```

🎯 Guardá los **id** generados.

---

### **2.2. Crear Camiones**

**Endpoint:**

`POST /api/catalogos/camiones`

**Camión 1**

```json
{
  "patente": "AB123CD",
  "tipo": "Camión de carga pesada",
  "capacidadKg": 25000,
  "volumenM3": 80,
  "consumoCombustibleKm": 0.35,
  "costoBaseKm": 150.00,
  "nombreTransportista": "Juan Pérez",
  "telefonoTransportista": "+54 351 123 4567"
}

```

**Camión 2**

```json
{
  "patente": "EF456GH",
  "tipo": "Camión de gran volumen",
  "capacidadKg": 30000,
  "volumenM3": 100,
  "consumoCombustibleKm": 0.40,
  "costoBaseKm": 180.00,
  "nombreTransportista": "María González",
  "telefonoTransportista": "+54 351 987 6543"
}

```

🎯 Guardá los IDs generados.

---

### **2.3. Crear Tarifas Base**

**Endpoint:**

`POST /api/catalogos/tarifas`

```json
{
  "nombre": "Tarifa Estándar 2025",
  "precioBase": 1000.00,
  "precioKm": 120.00,
  "precioKg": 0.50,
  "precioM3": 10.00,
  "vigenciaDesde": "2025-01-01",
  "vigenciaHasta": "2025-12-31"
}

```

---

## 📝 **PASO 3: CREAR UNA SOLICITUD DE TRANSPORTE (Cliente)**

**Endpoint:**

`POST /api/solicitudes/api/solicitudes`

```json
{
  "cliente": {
    "razonSocial": "Constructora ABC S.A.",
    "cuit": "30-71234567-8",
    "email": "contacto@abc.com.ar",
    "telefono": "+54 351 444 5555"
  },
  "contenedor": {
    "codigo": "CONT-2025-001",
    "pesoKg": 15000,
    "volumenM3": 60,
    "tipo": "Contenedor 40 pies HC"
  },
  "origenNombre": "Puerto de Buenos Aires, CABA",
  "origenLat": -34.6037,
  "origenLng": -58.3816,
  "destinoNombre": "Barrio Los Alamos, Córdoba Capital",
  "destinoLat": -31.4135,
  "destinoLng": -64.1811
}

```

**Respuesta:**

```json
{
  "id": "a6948b5c-ecf3-449a-b32c-e6569b4f1923",
  "fechaCreacion": "2025-11-17T10:30:00",
  "estadoActual": "BORRADOR"
}
```

🎯 Guardá el ID de la solicitud.

---

## 🗺️ **PASO 4: CALCULAR DISTANCIA Y DURACIÓN (OSRM)**

> ⚠️ **IMPORTANTE OSRM**: Este paso es necesario porque el proyecto no calcula automáticamente las distancias. Hay que obtener manualmente los valores de OSRM y luego redondearlos para usarlos en los siguientes pasos.

**Endpoint:**

`POST /api/operaciones/distancias/calcular`

**Body:**

```json
{
  "origenNombre": "Puerto de Buenos Aires, CABA",
  "destinoNombre": "Barrio Los Alamos, Córdoba Capital",
  "origen": {
    "latitud": -34.6037,
    "longitud": -58.3816
  },
  "destino": {
    "latitud": -31.4135,
    "longitud": -64.1811
  }
}
```

**Respuesta típica:**

```json
{
  "origenNombre": "Puerto de Buenos Aires, CABA",
  "destinoNombre": "Barrio Los Alamos, Córdoba Capital",
  "origen": {
    "latitud": -34.6037,
    "longitud": -58.3816
  },
  "destino": {
    "latitud": -31.4135,
    "longitud": -64.1811
  },
  "distanciaKm": 696.09,
  "duracionMinutos": 440.59,
  "duracionHoras": 7.34,
  "exitoso": true,
  "mensaje": "Distancia calculada exitosamente"
}
```

📝 **IMPORTANTE - Cómo usar estos valores:**

- **distanciaKm**: Redondeá a 2 decimales para `distanciaKmPlan` → Ej: `696.09`
- **duracionMinutos**: Redondeá al entero más cercano para `duracionMinPlan` → Ej: `441`
- Si tenés múltiples tramos, calculá cada uno por separado y sumá los totales.

---

## 💰 **PASO 5: CALCULAR TARIFA APROXIMADA**

**Endpoint:**

`POST /api/operaciones/rutas/tarifa-aproximada`

**Body:**

```json
{
  "solicitudId": "a6948b5c-ecf3-449a-b32c-e6569b4f1923",
  "distanciaKmEstimada": 696.09,
  "cantidadTramos": 1,
  "contenedorPesoKg": 15000,
  "contenedorVolumenM3": 60
}
```

🎯 Esta estimación te muestra el costo aproximado antes de crear la ruta.

💡 **Nota**: Usá la `distanciaKm` que obtuviste en el paso anterior (OSRM).

---

## ✅ **PASO 6: CREAR RUTA Y TRAMOS**

**Endpoint:**

`POST /api/operaciones/rutas`

**Body:**

```json
{
  "solicitudId": "a6948b5c-ecf3-449a-b32c-e6569b4f1923",
  "distanciaKmPlan": 696.09,
  "duracionMinPlan": 441,
  "tramos": [
    {
      "orden": 1,
      "origenNombre": "Puerto de Buenos Aires, CABA",
      "origenLat": -34.6037,
      "origenLng": -58.3816,
      "destinoNombre": "Barrio Los Alamos, Córdoba Capital",
      "destinoLat": -31.4135,
      "destinoLng": -64.1811,
      "distanciaKmPlan": 696.09,
      "duracionMinPlan": 441
    }
  ]
}
```

📝 **NOTA**: Los valores de `distanciaKmPlan` y `duracionMinPlan` vienen del **PASO 4 (OSRM)**, redondeados como se indicó.

🎯 Guardá el ID de la ruta y los IDs de los tramos generados.

---

## 🚛 **PASO 7: ASIGNAR CAMIÓN A UN TRAMO**

**Endpoint:**

`POST /api/operaciones/asignaciones`

**Body:**

```json
{
  "tramoId": "<ID_TRAMO_1>",
  "camionId": "<ID_CAMION>",
  "contenedorPesoKg": 15000,
  "contenedorVolumenM3": 60
}
```

🎯 Guardá el ID de la asignación generada.

---

## 🏁 **PASO 8: INICIAR TRAMO**

**Endpoint:**

`PUT /api/operaciones/tramos/{id}/iniciar`

🎯 Cambia el estado del tramo a `EN_TRANSITO`.

---

## 🚗 **PASO 9: CAMBIAR ESTADO A EN_TRANSITO**

**Endpoint:**

`PUT /api/solicitudes/api/solicitudes/{id}/estado`

**Params:**

- `nuevoEstado`: `EN_TRANSITO`
- `observaciones`: `Camión en ruta hacia destino`

**Authorization:** Bearer Token (OPERADOR)

🎯 Sincroniza el estado de la solicitud con el inicio del tramo.

---

## 🏁 **PASO 10: FINALIZAR TRAMO**

**Endpoint:**

`PUT /api/operaciones/tramos/{id}/finalizar`

🎯 Cambia el estado del tramo a `FINALIZADO`.

---

## ✅ **PASO 11: CAMBIAR ESTADO A ENTREGADA**

**Endpoint:**

`PUT /api/solicitudes/api/solicitudes/{id}/estado`

**Params:**

- `nuevoEstado`: `ENTREGADA`
- `observaciones`: `Entrega completada exitosamente`

**Authorization:** Bearer Token (OPERADOR)

🎯 Finaliza la operación completa.

---

## 👀 **PASO 12: CONSULTAR ESTADO DE LA SOLICITUD**

**Endpoint:**

`GET /api/solicitudes/api/solicitudes/{id}`

**Authorization:** Bearer Token (CLIENTE)

🎯 Verificá el estado final y todos los datos de la solicitud.

---

## 📋 **PASO 13: CONSULTAR HISTORIAL DE ESTADOS**

**Endpoint:**

`GET /api/solicitudes/api/solicitudes/{id}/historial`

**Authorization:** Bearer Token (OPERADOR)

🎯 Muestra todos los cambios de estado con timestamps y observaciones.

---

## 🔍 **PASO 14: CONSULTAR SOLICITUDES PENDIENTES**

**Endpoint:**

`GET /api/solicitudes/api/solicitudes/pendientes`

**Authorization:** Bearer Token (OPERADOR)

🎯 Verifica que la solicitud ya no aparece en pendientes (debería estar vacío o sin esta solicitud).

---

## 📝 **RESUMEN DEL FLUJO COMPLETO**

1. ✅ **Autenticarse** con Keycloak → Obtener tokens JWT (3 roles)
2. ✅ **Crear catálogos** (depósitos, camiones, tarifas)
3. ✅ **Crear solicitud** de transporte (estado: PENDIENTE)
4. ✅ **Calcular distancia** con OSRM (redondear valores)
5. ✅ **Calcular tarifa aproximada** usando distancia OSRM
6. ✅ **Crear ruta y tramos** con valores de OSRM
7. ✅ **Asignar camión** al tramo
8. ✅ **Iniciar tramo** (estado tramo: EN_TRANSITO)
9. ✅ **Cambiar estado a EN_TRANSITO** (sincronizar solicitud)
10. ✅ **Finalizar tramo** (estado tramo: FINALIZADO)
11. ✅ **Cambiar estado a ENTREGADA** (operación completa)
12. ✅ **Consultar estado** final de la solicitud
13. ✅ **Consultar historial** completo de cambios
14. ✅ **Verificar pendientes** (solicitud ya no aparece)

---

## ⚠️ **LIMITACIONES Y "SOLUCIONES CON ALAMBRE"**

### **1. OSRM - Cálculo manual de distancias**

**Problema**: El sistema no calcula automáticamente las distancias al crear una ruta.

**Solución actual**: 
- Llamar manualmente a `POST /api/operaciones/distancias/calcular` (PASO 4)
- Copiar y redondear los valores de respuesta
- Usar esos valores en `POST /api/operaciones/rutas` (PASO 6)

**Solución ideal**: El endpoint de creación de rutas debería llamar automáticamente a OSRM internamente.

### **2. Integración entre microservicios**

**Problema**: No hay orquestación automática entre `ms-solicitudes` y `ms-operaciones`.

**Solución actual**: 
- Pasos manuales separados
- Cada servicio mantiene su estado independiente
- Los campos `costo_real` y `tiempo_real_entrega` quedan nulos

**Solución ideal**: 
- Endpoint único tipo `POST /api/solicitudes/{id}/planificar`
- Uso de eventos/mensajería (Kafka/RabbitMQ)
- Saga pattern para consistencia distribuida

### **3. Coordinación de estados**

**Problema**: Al crear una ruta en `ms-operaciones`, no se actualiza automáticamente el estado en `ms-solicitudes`.

**Solución actual**: 
- Endpoint manual `PUT /api/solicitudes/{id}/estado` para cambiar estados
- Se requieren 2 cambios de estado manuales: EN_TRANSITO → ENTREGADA
- El operador debe ejecutar estos cambios en el momento correcto del flujo

**Solución ideal**: Eventos de dominio que sincronicen estados entre servicios.

Estas dos últimas, conceptualmente son similares al patrón Observer, pero a nivel de arquitectura distribuida se llama Event-Driven Architecture o Publish-Subscribe pattern.

"Sería un patrón Observer distribuido mediante eventos asincrónicos".
---

## 💡 **PARA LA PRESENTACIÓN - QUÉ EXPLICAR**

1. **Mostrar la arquitectura**: 3 microservicios + gateway + Keycloak + OSRM
2. **Destacar la seguridad**: JWT, roles (CLIENTE, OPERADOR, TRANSPORTISTA)
3. **Explicar OSRM**: Por qué se usa, cómo calcula rutas reales en Argentina
4. **Admitir limitaciones**: "Está atado con alambre por tiempo del proyecto"
5. **Proponer mejoras**: Orquestación, eventos, automatización de OSRM
6. **Demostrar el flujo**: Swagger en vivo con datos de ejemplo
7. **Mostrar persistencia**: Consultar base de datos PostgreSQL

---

## 📌 **NOTAS IMPORTANTES**

- ✅ Guardá siempre los IDs generados en cada paso
- ✅ Autorizá en Swagger con el token JWT antes de cada operación
- ✅ Seguí los pasos en orden secuencial
- ✅ Los valores de OSRM deben redondearse antes de usar
- ✅ Verificá con los endpoints GET después de cada operación
- ✅ Swagger UI: `http://localhost:8080/swagger-ui.html`

---

## 🎉 **RESULTADO FINAL - QUÉ DEMUESTRA ESTE FLUJO**

✅ Autenticación y autorización con Keycloak  
✅ CRUD completo de catálogos (depósitos, camiones, tarifas)  
✅ Creación de solicitudes de transporte  
✅ Integración con OSRM para cálculo de distancias reales  
✅ Cálculo de tarifas basadas en múltiples factores  
✅ Asignación de recursos (rutas y camiones)  
✅ Gestión de estados del flujo operativo  
✅ Seguimiento completo con historial  
✅ Consultas y filtros avanzados  
✅ Persistencia en PostgreSQL (3 bases de datos independientes)

---

## 🎯 **TIPS PARA LA PRESENTACIÓN/DEFENSA**

1. **Arquitectura**: Mostrá el diagrama de microservicios y explicá el patrón database-per-service
2. **Seguridad**: Destacá OAuth2 + JWT + roles granulares
3. **OSRM**: Explicá por qué es necesario (rutas reales, no distancia euclidiana)
4. **Swagger**: Demostrá la documentación automática OpenAPI
5. **Limitaciones**: Sé honesto sobre las "soluciones con alambre" y proponé mejoras
6. **Reglas de negocio**: Mostrá validaciones (capacidad de camión, estados, etc.)
7. **Escalabilidad**: Explicá cómo cada microservicio puede escalar independientemente
8. **Base de datos**: Mostrá las tablas en PostgreSQL y la consistencia de datos

---

Si querés, también te lo dejo en versión **markdown colapsable**, **tabla**, **resumen**, o **PDF listo para entregar**.