# Plataforma de Microservicios

Esta guía documenta en detalle cómo está organizada la plataforma, cómo compilar cada módulo Java, cómo levantar la infraestructura completa con Docker (gateway + microservicios + base de datos + autenticación + ruteo OSRM) y cómo probar cada pieza.

## 1. Visión general de la arquitectura

| Componente | Tecnología | Propósito |
| --- | --- | --- |
| **api-gateway** | Spring Cloud Gateway (WebFlux) | Expone una única puerta de entrada, aplica CORS, delega en Keycloak como Resource Server y enruta a cada microservicio. |
| **ms-catalogos** | Spring Boot 3 | Gestiona catálogos de recursos; usa la base `catalogos`. |
| **ms-solicitudes** | Spring Boot 3 | Maneja solicitudes y su ciclo de vida; usa la base `solicitudes`. |
| **ms-operaciones** | Spring Boot 3 | Orquesta operaciones y consume OSRM para calcular distancias; usa la base `operaciones`. |
| **Postgres** | Postgres 16 | Hostea las tres bases (catalogos/solicitudes/operaciones) creadas por `docker/init.sql`. |
| **Keycloak** | Keycloak 24 | Gestiona realms, usuarios y emite tokens JWT (realm `tpi-backend`). |
| **OSRM backend** | ghcr.io/project-osrm/osrm-backend | Servicio externo para cálculo de rutas reales con datos de OpenStreetMap. |

Todos los servicios comparten la red Docker `tpi`, por lo que se comunican por nombre de contenedor (`postgres`, `keycloak`, `osrm-backend`, etc.). El gateway expone los microservicios bajo `/api/<contexto>/**` y autentica todas las rutas salvo health/Swagger.

## 2. Pre-requisitos

- Docker Desktop / Engine 24+
- Docker Compose plugin 2.24+
- Maven 3.9+
- JDK 17 (Temurin recomendado)
- 2 GB libres para los archivos procesados de OSRM

## 3. Estructura del repositorio

```
.
├── api-gateway/                # Spring Cloud Gateway (config CORS y rutas)
├── ms-catalogos/               # Microservicio Catálogos
├── ms-solicitudes/
├── ms-operaciones/
├── docker/
│   ├── docker-compose.yml      # Orquestación completa
│   ├── init.sql                # Crea las 3 bases en Postgres
│   └── keycloak-config/
│       └── tpi-realm-export.json # Realm preconfigurado
├── pom.xml                     # Padre Maven que fija Java 17
└── README.md
```

Cada microservicio define su perfil `docker` (apuntando a `postgres` como host) en `src/main/resources/application-docker.yml`. Ejemplo:

```
spring:
  datasource:
    url: jdbc:postgresql://postgres:5432/catalogos
  jpa:
    hibernate:
      ddl-auto: update
```

`ms-operaciones` añade URLs internas para reutilizar OSRM y los otros MS sin pasar por el gateway (`ms-catalogos:8081`, `osrm-backend:5000`).

## 4. Empaquetar los microservicios

Desde la raíz del repo:

```bash
mvn -B -DskipTests clean package
```

El comando crea los Spring Boot fat JAR en `*/target/*.jar` que luego copian los Dockerfile de cada módulo.

## 5. Configuración y secretos locales

1. **Datos de OSRM**
   - Creá `docker/.env` (está ignorado por git) con la ruta local donde guardaste los archivos procesados de OSRM.
   - Ejemplo Windows: `OSRM_DATA_PATH=D:/osrm-data`.
   - Ejemplo Linux/Mac: `OSRM_DATA_PATH=/home/<usuario>/osrm-data`.

2. **Passwords de Keycloak**
   - Admin: `admin / admin` (definido en `docker/docker-compose.yml`).
   - Usuarios finales provienen de `docker/keycloak-config/tpi-realm-export.json` (cliente01, operador01, transportista01, etc.).

3. **Contraseñas del gateway**
   - Al usar Keycloak como Resource Server, necesitás tokens JWT. Opcionalmente, podés agregar `SPRING_SECURITY_USER_*` en el compose si querés volver a HTTP Basic para pruebas rápidas.
   - Si cambiás la URL pública de Keycloak, actualizá `KEYCLOAK_JWK_SET_URI` (para que el gateway pueda descargar las llaves) y, si el `iss` del token también varía, añadilo a `gateway.security.accepted-issuers` o exportá `GATEWAY_SECURITY_ACCEPTED_ISSUERS="http://nuevo-host/..."` antes de levantar el container.

## 6. Servicios definidos en docker-compose

`docker/docker-compose.yml` levanta todo en una sola red. Resumen de puertos, dependencias y volúmenes:

| Servicio | Imagen / Build | Puertos host | Volúmenes | Dependencias |
| --- | --- | --- | --- | --- |
| `postgres` | `postgres:16` | 5433→5432 | `pgdata`, `init.sql` | — |
| `keycloak` | `quay.io/keycloak/keycloak:24.0.3` | 8084→8084 | `keycloak_data`, *(opcional)* `keycloak-config` | — |
| `osrm-backend` | `ghcr.io/project-osrm/osrm-backend` | 5000→5000 | `${OSRM_DATA_PATH}` | — |
| `ms-catalogos` | build `../ms-catalogos` | 8081→8081 | — | Postgres healthy |
| `ms-solicitudes` | build `../ms-solicitudes` | 8082→8082 | — | Postgres healthy |
| `ms-operaciones` | build `../ms-operaciones` | 8083→8083 | — | Postgres + OSRM healthy |
| `api-gateway` | build `../api-gateway` | 8080→8080 | — | Los 3 MS healthy |

Cada servicio define un healthcheck (`/actuator/health` en los micros, `pg_isready` para Postgres, `/health/ready` para Keycloak y un request real para OSRM). Compose usa esos healthchecks en `depends_on` para ordenar el arranque.

## 7. Flujo de arranque recomendado

1. **Compilar**: `mvn -DskipTests clean package`
2. **Construir imágenes**: `docker compose -f docker/docker-compose.yml build`
3. **Levantar**: `docker compose -f docker/docker-compose.yml up -d`
4. **Verificar estado**: `docker compose -f docker/docker-compose.yml ps`
5. **Logs puntuales**: `docker compose -f docker/docker-compose.yml logs -f <servicio>`
6. **Apagar**: `docker compose -f docker/docker-compose.yml down` (agregá `-v` para borrar volúmenes persistentes).

## 8. Postgres y bases por microservicio

El script `docker/init.sql` crea tres bases (una por micro). No se crean tablas aquí: cada servicio ejecuta `ddl-auto=update` para generar su esquema en su base correspondiente la primera vez que se conecta.

- **Host**: `postgres`
- **Puerto**: `5432` dentro de la red (`5433` publicado al host)
- **Credenciales**: `postgres / postgres`
- **Bases**: `catalogos`, `solicitudes`, `operaciones`

## 9. API Gateway: rutas, CORS y seguridad

- **Rutas** (`api-gateway/src/main/resources/application.yml`):
  - `/api/catalogos/**` → `ms-catalogos:8081`
  - `/api/solicitudes/**` → `ms-solicitudes:8082`
  - `/api/operaciones/**` → `ms-operaciones:8083`
  - Cada ruta aplica `StripPrefix=2`, por lo que `/api/catalogos/health` se reescribe como `/health` en el servicio destino.
- **CORS global**: se aceptan todos los orígenes y métodos (útil para frontends locales).
- **Seguridad** (`SecurityConfig.java`):
  - Si `gateway.security.enabled=true` (valor por defecto) todo el tráfico fuera de health/Swagger exige un JWT válido.
  - El decoder se alimenta del `jwk-set-uri` (`KEYCLOAK_JWK_SET_URI`, overrideable por variable de entorno) y valida la firma contra Keycloak aunque el token se haya emitido usando otra URL pública.
  - La lista `gateway.security.accepted-issuers` permite aceptar múltiples valores de `iss` (ej.: `http://localhost:8084/...` y `http://keycloak:8084/...`), evitando 401 `invalid_token` cuando Swagger obtiene el token desde tu host pero el gateway vive en la red Docker.
  - Podés desactivar temporalmente la seguridad con `gateway.security.enabled=false` (no olvides revertirlo antes de commitear).

## 10. Keycloak: realm, importación y obtención de tokens

### 10.1 Realm incluido

El archivo `docker/keycloak-config/tpi-realm-export.json` define:
- Realm `tpi-backend` (algoritmo RS256).
- Cliente público `tpi-client` con `redirectUris` y `webOrigins` en `/*`.
- Roles de negocio (`CLIENTE`, `OPERADOR`, `TRANSPORTISTA`).
- Usuarios iniciales (`cliente01`, `operador01`, `transportista01`) cada uno con su rol correspondiente (revisá Keycloak para asignar contraseñas si las cambiás).

### 10.2 Importación manual (cuando levantás desde cero)

1. Levantá el stack sin montar la carpeta `keycloak-config` (está comentada en el compose).
2. Copiá el JSON al contenedor:
   ```bash
   cd docker
   docker cp ./keycloak-config/tpi-realm-export.json keycloak:/opt/keycloak/data/tpi-realm-export.json
   ```
3. Ejecutá el import (nota el `=true` en `--override`):
   ```bash
   docker exec keycloak /opt/keycloak/bin/kc.sh import --file /opt/keycloak/data/tpi-realm-export.json --override=true
   ```
4. El comando detiene el proceso una vez finalizado; reiniciá solo Keycloak:
   ```bash
   docker compose restart keycloak
   ```
5. El realm queda almacenado en el volumen `keycloak_data`, por lo que no necesitás reimportar salvo que borres ese volumen.

> Si preferís importar automáticamente en cada arranque, descomentá el volumen `./keycloak-config:/opt/keycloak/data/import` en el compose y agregá `command: ["start-dev", "--import-realm"]`.

### 10.3 Obtener tokens JWT para probar el gateway

1. **Credenciales** (realm `tpi-backend`):
   - `cliente01 / <password>` (rol CLIENTE)
   - `operador01 / <password>` (rol OPERADOR)
   - `transportista01 / <password>` (rol TRANSPORTISTA)

2. **Token endpoint** (password grant, cliente público `tpi-client`):
   ```bash
   curl -X POST http://localhost:8084/realms/tpi-backend/protocol/openid-connect/token \
     -H "Content-Type: application/x-www-form-urlencoded" \
     -d "client_id=tpi-client" \
     -d "grant_type=password" \
     -d "username=operador01" \
     -d "password=<tu_password>"
   ```

   La respuesta contiene `access_token` y `expires_in`. Copiá el valor del access token.

3. **Consumir el gateway**:
   ```bash
   curl http://localhost:8080/api/operaciones/health \
     -H "Authorization: Bearer <access_token>"
   ```

   > ⚠️ Swagger suele conseguir tokens con `iss = http://localhost:8084/...` mientras que, dentro de Docker, Keycloak responde como `http://keycloak:8084`. Gracias a `gateway.security.accepted-issuers` ambos valores están permitidos, por lo que el gateway dejará de devolver 401 `invalid_token`. Si necesitás agregar otra URL (por ejemplo, un hostname público), sumala a la lista en `application-docker.yml` o vía variable `GATEWAY_SECURITY_ACCEPTED_ISSUERS`.

4. **Opcional**: si querés desactivar la seguridad temporalmente para pruebas rápidas, podés añadir `spring.security.enabled=false` en `api-gateway/src/main/resources/application-docker.yml` y reconstruir.

## 11. OSRM (Open Source Routing Machine)

`ms-operaciones` necesita un OSRM ya procesado. Tenés dos caminos:

### 11.1 Usar archivos procesados compartidos (recomendado)
1. Pedí al equipo el ZIP `osrm-data.zip` (~1.3 GB) que contiene todos los `argentina-latest.osrm*`.
2. Descomprimí en tu ruta local (`D:/osrm-data` o `/home/usuario/osrm-data`).
3. Creá `docker/.env` con `OSRM_DATA_PATH=<ruta>`.
4. Continuá con `mvn package` y `docker compose up -d`.

### 11.2 Procesar los datos vos mismo (toma 25–30 minutos)

```bash
# Descargar el mapa
wget https://download.geofabrik.de/south-america/argentina-latest.osm.pbf -O <ruta>/argentina-latest.osm.pbf

# Extraer
docker run -t -v <ruta>:/data ghcr.io/project-osrm/osrm-backend osrm-extract \
  -p /opt/car.lua /data/argentina-latest.osm.pbf

# Contract (genera .osrm.hsgr)
docker run -t -v <ruta>:/data ghcr.io/project-osrm/osrm-backend osrm-contract \
  /data/argentina-latest.osrm
```

Con los archivos listos, `osrm-backend` se inicia con `osrm-routed --algorithm ch /data/argentina-latest.osrm` y expone `http://localhost:5000`.

### 11.3 Probar OSRM directamente

```bash
curl "http://localhost:5000/route/v1/driving/-64.18105,-31.4135;-60.6985,-32.9471?overview=false"
```

Respuesta esperada:
```json
{
  "code": "Ok",
  "routes": [{ "distance": 398251.2, "duration": 14931 }]
}
```

### 11.4 Endpoints en `ms-operaciones`

`ms-operaciones` expone endpoints REST (via `/distancias/...`) que consumen OSRM; revisá el Swagger local en `http://localhost:8083/swagger-ui/index.html` o vía gateway `http://localhost:8080/api/operaciones/swagger-ui/index.html`.

Ejemplo:
```bash
curl -X POST http://localhost:8083/distancias/directa \
  -H "Content-Type: application/json" \
  -d '{
        "origenNombre": "Córdoba",
        "origen": {"latitud": -31.4135, "longitud": -64.18105},
        "destinoNombre": "Rosario",
        "destino": {"latitud": -32.9471, "longitud": -60.6985}
      }'
```

## 12. Probar la plataforma

### 12.1 Endpoints de salud

```bash
# Directo
curl http://localhost:8081/actuator/health
curl http://localhost:8082/actuator/health
curl http://localhost:8083/actuator/health

# A través del gateway (requiere token si no deshabilitaste security)
curl http://localhost:8080/api/catalogos/health -H "Authorization: Bearer <token>"
```

### 12.2 Swagger / OpenAPI

- `http://localhost:8081/swagger-ui/index.html`
- `http://localhost:8082/swagger-ui/index.html`
- `http://localhost:8083/swagger-ui/index.html`
- Vía gateway: `http://localhost:8080/api/catalogos/swagger-ui/index.html`, etc.

### 12.3 Operaciones de ejemplo

1. **Catálogos**: `GET http://localhost:8081/version` o `http://localhost:8080/api/catalogos/version` para confirmar la versión desplegada.
2. **Solicitudes**: crear una solicitud (ver payloads en el Swagger) y listar para asegurarte de que la base `solicitudes` persiste datos.
3. **Operaciones**: usar el endpoint `POST /operaciones/planificar` vía gateway con un cuerpo como `{"origen":"A","destino":"B","paradasIntermedias":2}`.
4. **Autenticación**: probar distintos usuarios y roles para validar reglas de autorización en tus endpoints.

### 12.4 Logs útiles

```bash
# Gateway (ver JWT o errores de ruteo)
docker compose -f docker/docker-compose.yml logs -f api-gateway

# Operaciones (validar llamadas a OSRM)
docker compose -f docker/docker-compose.yml logs -f ms-operaciones

# Keycloak (importaciones o errores de login)
docker compose -f docker/docker-compose.yml logs -f keycloak
```

## 13. Reset completo

Si necesitás empezar desde cero:

```bash
cd docker
docker compose down -v   # elimina contenedores y volúmenes pgdata/keycloak_data
rm -rf <ruta_osrm>/argentina-latest.osrm*  # solo si querés reprocesar OSRM
```

Luego repetí los pasos desde la sección 4.

---

Con esta guía podés reconstruir el entorno completo, importar la configuración de seguridad, cargar los datos de ruteo y probar cada microservicio directa o indirectamente a través del gateway.
