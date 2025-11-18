# Mapeo de API, Roles y Ejemplos de Prueba

Este documento resume los endpoints, el control de acceso por roles, ejemplos de bodies y comandos `curl` para los tres microservicios principales de la presentación: `ms-catalogos`, `ms-solicitudes` y `ms-operaciones`. También incluye una discrepancia crítica encontrada y la recomendación de corrección.

Notas generales:
- El `api-gateway` corre en el puerto `8080` y enruta:
  - `/api/catalogos/**` → `ms-catalogos` (StripPrefix=2)
  - `/api/solicitudes/**` → `ms-solicitudes` (StripPrefix=2)
  - `/api/operaciones/**` → `ms-operaciones` (StripPrefix=2)
- Al llamar vía gateway usar `http://localhost:8080/api/<servicio>...`.
- Reemplazar los placeholders de tokens por los reales de Keycloak: `$OP_TOKEN` (Operador), `$TR_TOKEN` (Transportista), `$CL_TOKEN` (Cliente).

---

**ms-catalogos** (ruta en gateway: `/api/catalogos/**`)

- Seguridad: solo el rol `OPERADOR` puede acceder a endpoints de catálogo. `health` y Swagger son públicos.
- Endpoints clave (path en el servicio / path reenviado):
  - `GET /camiones` — ROL: OPERADOR
    - URL vía gateway: `GET http://localhost:8080/api/catalogos/camiones`
    - Ejemplo: `curl -H "Authorization: Bearer $OP_TOKEN" http://localhost:8080/api/catalogos/camiones`
  - `POST /camiones` — ROL: OPERADOR
    - Archivo ejemplo: `docs/examples/catalogos/camion-post.json`
    - Ejemplo: `curl -X POST -H "Authorization: Bearer $OP_TOKEN" -H "Content-Type: application/json" -d @docs/examples/catalogos/camion-post.json http://localhost:8080/api/catalogos/camiones`
  - `PUT /camiones/{id}`, `DELETE /camiones/{id}` — ROL: OPERADOR
  - Acceso similar para `/depositos`, `/tarifas`, `/tarifas-volumen`, `/configuracion-tarifas`.
- Enlace con la lógica de negocio: `ms-operaciones` consulta `ms-catalogos` para obtener configuraciones de tarifas y datos de camiones (ver `CatalogosClient`). Esos datos afectan el cálculo de costos y validaciones de asignación.

---

**ms-solicitudes** (ruta en gateway: `/api/solicitudes/**`) — IMPORTANTE

- Seguridad prevista (según `SecurityConfig`):
  - Público: `/health`, `/api/solicitudes/health`, actuator, swagger
  - `CLIENTE`: `POST /solicitudes/**`, `GET /solicitudes/mias/**`, `GET /solicitudes/historial/**`
  - `OPERADOR`: `GET /solicitudes/pendientes/**`, `GET /solicitudes/contenedores-pendientes/**`, `PUT /solicitudes/**`
  - Default: autenticado
- Paths del controlador: `SolicitudesController` está anotado con `@RequestMapping("/api/solicitudes")`, por lo que el servicio espera rutas como `/api/solicitudes/pendientes`, etc.

Discrepancia crítica detectada:
- El gateway quita el prefijo `/api/solicitudes` (StripPrefix=2) y reenvía la petición al servicio sin ese prefijo (por ejemplo, `/api/solicitudes/pendientes` → `/pendientes`). Sin embargo, el controlador espera `/api/solicitudes/pendientes` (mapeo a nivel de clase), por lo que las peticiones realizadas a través del gateway NO coincidirán con los endpoints del controlador (la mayoría dará 404), aunque las rutas de `health` sí funcionen porque se añadieron explícitamente.
- Además, `SecurityConfig` usa matchers como `"/solicitudes/**"` (sin `/api`) mientras que el controlador usa `/api/solicitudes` — esto genera inconsistencias que pueden hacer que las reglas de seguridad no se apliquen correctamente cuando se accede vía gateway.

Recomendación (opciones):
- Preferido: Cambiar `SolicitudesController` a `@RequestMapping("/solicitudes")` (así las rutas reenviadas por el gateway coincidirán). Alternativamente, agregar ambos mapeos: `@RequestMapping({"/api/solicitudes","/solicitudes"})`.
- Alinear `SecurityConfig` con los mismos patrones de ruta que expone el controlador (decidir usar `/api` o no y mantenerlo consistente).

Endpoints clave (como deberían ser llamados vía gateway):
  - `POST /api/solicitudes` — Crear solicitud (ROL: CLIENTE)
    - Archivo ejemplo: `docs/examples/solicitudes/solicitud-post.json`
    - curl: `curl -X POST -H "Authorization: Bearer $CL_TOKEN" -H "Content-Type: application/json" -d @docs/examples/solicitudes/solicitud-post.json http://localhost:8080/api/solicitudes`
  - `GET /api/solicitudes/pendientes` — Listar pendientes (ROL: OPERADOR)
    - curl: `curl -H "Authorization: Bearer $OP_TOKEN" http://localhost:8080/api/solicitudes/pendientes`
  - `PUT /api/solicitudes/{id}/finalizar` — Finalizar (ROL: OPERADOR)
    - Ejemplo curl: `curl -X PUT -H "Authorization: Bearer $OP_TOKEN" "http://localhost:8080/api/solicitudes/{id}/finalizar?costoReal=123.45&tiempoRealEntrega=2025-11-17T12:00:00"`

---

**ms-operaciones** (ruta en gateway: `/api/operaciones/**`)

- Seguridad: `OPERADOR` para `/rutas/**`, `/distancias/**`, `/operaciones/**`, `/asignaciones/**`; `TRANSPORTISTA` para `/tramos/{id}/iniciar`, `/tramos/{id}/finalizar`, `/seguimiento`.
- Endpoints clave y bodies de ejemplo:
  - `GET /rutas` — ROL: OPERADOR
    - `curl -H "Authorization: Bearer $OP_TOKEN" http://localhost:8080/api/operaciones/rutas`
  - `POST /rutas` — ROL: OPERADOR
    - Archivo ejemplo: `docs/examples/operaciones/ruta-post.json`
    - `curl -X POST -H "Authorization: Bearer $OP_TOKEN" -H "Content-Type: application/json" -d @docs/examples/operaciones/ruta-post.json http://localhost:8080/api/operaciones/rutas`
  - `POST /distancias/calcular` — ROL: OPERADOR
    - Archivo ejemplo: `docs/examples/operaciones/distancia-request.json`
  - `POST /asignaciones` — ROL: OPERADOR
    - Archivo ejemplo: `docs/examples/operaciones/asignacion-post.json`
  - `PUT /tramos/{id}/iniciar` — ROL: TRANSPORTISTA
    - `curl -X PUT -H "Authorization: Bearer $TR_TOKEN" http://localhost:8080/api/operaciones/tramos/{id}/iniciar`
  - `POST /seguimiento` — ROL: TRANSPORTISTA
    - Archivo ejemplo: `docs/examples/operaciones/seguimiento-post.json`

---

**Comandos rápidos de aceptación (vía gateway; requieren tokens)**
- ms-catalogos: Operador permitido, Transportista bloqueado
  - `curl -H "Authorization: Bearer $OP_TOKEN" http://localhost:8080/api/catalogos/camiones`  # esperar 200
  - `curl -H "Authorization: Bearer $TR_TOKEN" http://localhost:8080/api/catalogos/camiones`  # esperar 403

- ms-solicitudes:
  - Crear (Cliente): `curl -X POST -H "Authorization: Bearer $CL_TOKEN" -H "Content-Type: application/json" -d @docs/examples/solicitudes/solicitud-post.json http://localhost:8080/api/solicitudes`
  - Listar pendientes (Operador): `curl -H "Authorization: Bearer $OP_TOKEN" http://localhost:8080/api/solicitudes/pendientes`
  - Mismo endpoint con token de Transportista debe devolver 403.

- ms-operaciones:
  - `curl -H "Authorization: Bearer $OP_TOKEN" http://localhost:8080/api/operaciones/rutas`
  - `curl -X PUT -H "Authorization: Bearer $TR_TOKEN" http://localhost:8080/api/operaciones/tramos/{id}/iniciar`

---

**Archivos añadidos**
- `docs/api-mapping-es.md` (este archivo)
- Ejemplos de bodies en `docs/examples/` (mismos que en inglés).

**Siguientes pasos sugeridos**
- Si la demo será vía gateway (recomendado), conviene corregir el mapeo en `ms-solicitudes` para que coincida con el comportamiento del gateway. Puedo preparar el parche mínimo para cambiar `@RequestMapping("/api/solicitudes")` a `@RequestMapping("/solicitudes")` o añadir ambos mapeos, y alinear `SecurityConfig`.
- Puedo además generar un script PowerShell con los `curl` secuenciales para la presentación.

---

Fin del documento en español.
