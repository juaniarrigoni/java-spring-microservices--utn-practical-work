# Plataforma de Microservicios

Esta guía resume cómo construir y levantar la plataforma completa (gateway + microservicios + base de datos) usando los artefactos que viven en este repositorio.

## 1. Pre-requisitos

- Docker Desktop o Docker Engine 24+
- Docker Compose Plugin 2.24+
- Maven 3.9+
- JDK 17 (Temurin recomendado)

## 2. Empaquetar los microservicios

Desde la raíz del repositorio (`java-course-from-0-to-microservices-utn-frc`):

```bash
mvn -B -DskipTests clean package
```

> El build genera los JAR necesarios para que los Dockerfile empaqueten cada microservicio con Java 17.

## 3. Levantar la plataforma con Docker

Todo el orquestado vive en `docker/docker-compose.yml`. No hace falta moverse de carpeta: el propio compose referencia los Dockerfile de cada módulo con rutas relativas.

### Comandos principales (desde la raíz)

```bash
# Construye las imágenes usando los Dockerfile de api-gateway y de cada microservicio
# más la imagen oficial de Postgres.
docker compose -f docker/docker-compose.yml build

# Crea y levanta los cinco contenedores (postgres, api-gateway, ms-catalogos,
# ms-solicitudes y ms-operaciones) en la red `tpi`.
docker compose -f docker/docker-compose.yml up -d

# Verifica que estén arriba y en estado healthy (gracias a los healthchecks).
docker compose -f docker/docker-compose.yml ps

# Sigue los logs de un servicio en particular (reemplazá <servicio>).
docker compose -f docker/docker-compose.yml logs -f <servicio>
```

El compose se apoya en los healthchecks expuestos por Actuator (`/actuator/health`) y levanta los microservicios cuando Postgres está sano. Las variables `SPRING_PROFILES_ACTIVE=docker` apuntan a los `application-docker.yml` de cada módulo.

> Nota: en este repo la publicación del puerto de Postgres en el host puede haberse cambiado a `5433:5432` para evitar conflictos con una instalación local de Postgres; internamente los contenedores usan el puerto 5432.

### Apagar todo

```bash
docker compose -f docker/docker-compose.yml down
```

Añadí la opción `-v` si querés borrar el volumen `pgdata` y reiniciar la base desde cero.

## 4. Probar los endpoints de salud

Una vez que el compose marque todos los contenedores como `healthy`, comprobá los endpoints de forma directa y a través del gateway:

```bash
# Directo a cada microservicio
curl http://localhost:8081/health
curl http://localhost:8082/health
curl http://localhost:8083/health

# Expuestos por el gateway (rutas proxied):
curl http://localhost:8080/api/catalogos/health
curl http://localhost:8080/api/solicitudes/health
curl http://localhost:8080/api/operaciones/health
```

Si todos responden con `"status":"UP"` o con el JSON de metadatos correspondiente, la plataforma quedó operativa.

## 5. Estructura de carpetas relevante

```
.
├── api-gateway/           # Dockerfile + código del gateway
├── ms-catalogos/          # Dockerfile + código del MS Catálogos
├── ms-solicitudes/
├── ms-operaciones/
├── docker/
│   ├── docker-compose.yml # Orquesta los cinco contenedores
│   └── init.sql           # Script para inicializar Postgres
└── pom.xml                # Padre Maven que unifica Java 17
```

## 6. Probar a través del API Gateway (autenticación y ejemplos)

El `api-gateway` aplica seguridad HTTP Basic por defecto (Spring Boot genera una contraseña temporal si no configurás usuario/contraseña). Aquí tenés instrucciones prácticas para probar rutas a través del gateway desde Windows (cmd.exe / PowerShell).

1) Encontrar la contraseña generada (temporal)

- Si levantaste el stack con Docker, la contraseña generada por Spring se imprime en los logs del `api-gateway`. Para verla:

```bash
cd docker
# ver las últimas 200 líneas de logs del gateway
docker compose logs api-gateway --tail 200
```

Busca una línea parecida a:

```
Using generated security password: e0fe1349-011c-4ac2-9d9e-4ae068bc947b
```

El usuario por defecto es `user`. Esa contraseña es efímera (cambia cada arranque) a menos que la fijes (ver más abajo).

2) Probar con `curl.exe` (recomendado en Windows para evitar el alias de PowerShell)

Abrí `cmd.exe` o usá `curl.exe` explícito en PowerShell para evitar el alias. Ejemplos:

```bat
# GET health vía gateway (muestra encabezados con -v)
curl.exe -v -u user:e0fe1349-011c-4ac2-9d9e-4ae068bc947b http://localhost:8080/api/catalogos/health

# GET versión del servicio de catálogos a través del gateway
curl.exe -v -u user:e0fe1349-011c-4ac2-9d9e-4ae068bc947b http://localhost:8080/api/catalogos/version

# POST ejemplo a /api/operaciones/planificar
curl.exe -v -u user:e0fe1349-011c-4ac2-9d9e-4ae068bc947b -H "Content-Type: application/json" -d "{\"origen\":\"A\",\"destino\":\"B\",\"paradasIntermedias\":2}" http://localhost:8080/api/operaciones/planificar
```

3) Probar desde PowerShell (sin el alias `curl`)

En PowerShell usá `Invoke-RestMethod` o generá manualmente el header Basic:

```powershell
$creds = "user:e0fe1349-011c-4ac2-9d9e-4ae068bc947b"
$b64 = [Convert]::ToBase64String([System.Text.Encoding]::UTF8.GetBytes($creds))
$hdr = @{ Authorization = "Basic $b64" }

Invoke-RestMethod -Uri 'http://localhost:8080/api/catalogos/health' -Headers $hdr -Method Get

# POST
$body = @{ origen='A'; destino='B'; paradasIntermedias=2 } | ConvertTo-Json
Invoke-RestMethod -Uri 'http://localhost:8080/api/operaciones/planificar' -Headers $hdr -Method Post -Body $body -ContentType 'application/json'
```

4) Por qué se usa `curl.exe` en tus pruebas (explicación breve)

- PowerShell tiene un alias `curl` que apunta a `Invoke-WebRequest`/`Invoke-RestMethod` y su sintaxis no es compatible con `curl -u ...` directo. Por eso en Windows se recomienda usar `curl.exe` (la utilidad nativa de curl incluida en Windows) o usar la sintaxis de PowerShell para headers.

5) Fijar una contraseña permanente (opcional, recomendado para desarrollo estable)

Tenés dos opciones:

- A) Establecer variables de entorno en `docker-compose.yml` para el servicio `api-gateway`:

```yaml
services:
  api-gateway:
    environment:
      - SPRING_PROFILES_ACTIVE=docker
      - SPRING_SECURITY_USER_NAME=user
      - SPRING_SECURITY_USER_PASSWORD=miPasswordSegura
```

- B) O añadir en `api-gateway/src/main/resources/application-docker.yml`:

```yaml
spring:
  security:
    user:
      name: user
      password: miPasswordSegura
```

Con esto la contraseña será siempre la misma y no tendrás que leer los logs cada vez.

6) Desactivar seguridad temporalmente (solo desarrollo)

Si querés pruebas sin autenticación, podés desactivar Spring Security en el profile `docker` editando `api-gateway/src/main/resources/application-docker.yml` y añadiendo:

```yaml
spring:
  security:
    enabled: false
```

Luego reconstruí imagen y levantá de nuevo (`mvn package`, `docker compose build`, `docker compose up -d`).

## 7. Cómo funciona el arranque (resumen técnico)

1. Maven (host): `mvn -DskipTests clean package`
   - Empaqueta cada módulo en su JAR reempaquetado (Spring Boot "fat JAR") y los deja en `module/target/*.jar`.
2. Docker build (desde `docker`): `docker compose build` o `docker compose -f docker/docker-compose.yml build`
   - Cada Dockerfile de los micros está ahora configurado para copiar el JAR ya creado (`COPY target/*.jar app.jar`) y no compilar dentro del contenedor.
3. Docker run: `docker compose up -d`
   - Se crean containers con la red `tpi`. Postgres se inicializa (ejecuta `docker/init.sql`) y queda listo.
   - Los microservicios arrancan y realizan conexión a la DB. Los healthchecks de cada servicio (Actuator) se usan para marcar `healthy`.
4. API Gateway arranca y configura rutas (via `RouteConfig.java`) que proxyean:
   - `/api/catalogos/**` -> `ms-catalogos:8081` (stripPrefix(2) aplica)
   - `/api/solicitudes/**` -> `ms-solicitudes:8082`
   - `/api/operaciones/**` -> `ms-operaciones:8083`

Si querés investigar rutas internas, mirá `api-gateway/src/main/java/com/contenedores/apigateway/RouteConfig.java`.

---

Con esto tenés en el README un apartado práctico sobre cómo probar las rutas a través del gateway, ejemplos con `curl.exe` y PowerShell, y opciones para fijar o desactivar la seguridad.

## 8. Configuración de OSRM (Open Source Routing Machine)

OSRM calcula distancias y tiempos de viaje reales por carretera en Argentina, utilizando datos de OpenStreetMap.

### Pre-requisitos

- Descargar el archivo de mapa de Argentina desde Geofabrik:
  - URL: https://download.geofabrik.de/south-america/argentina-latest.osm.pbf
  - Tamaño: ~384 MB
- Crear un directorio local para los datos procesados (ej: `D:/osrm-data/`)

### Procesamiento de datos (solo una vez)

1) **Extraer datos del mapa** (toma ~10 minutos):

```bash
docker run -t -v D:/osrm-data:/data ghcr.io/project-osrm/osrm-backend osrm-extract -p /opt/car.lua /data/argentina-latest.osm.pbf
```

2) **Construir índice de jerarquía de contracción** (toma ~15-20 minutos, genera archivo .osrm.hsgr de ~900MB):

```bash
docker run -t -v D:/osrm-data:/data ghcr.io/project-osrm/osrm-backend osrm-contract /data/argentina-latest.osrm
```

> **Nota:** Estos comandos se ejecutan **una sola vez**. Los archivos procesados (.osrm, .osrm.hsgr, etc.) se reutilizan cada vez que levantes el stack.

### Configuración en docker-compose

El archivo `docker/.env` debe contener la ruta a tus datos OSRM:

```env
OSRM_DATA_PATH=D:/osrm-data
```

> Este archivo está en `.gitignore` para no commitear rutas locales.

### Probar OSRM directamente

Una vez levantado el stack con `docker compose up -d`, podés probar el servicio OSRM:

```bash
# Ejemplo: Córdoba (-64.18105,-31.4135) a Rosario (-60.6985,-32.9471)
curl "http://localhost:5000/route/v1/driving/-64.18105,-31.4135;-60.6985,-32.9471?overview=false"
```

Respuesta esperada:
```json
{
  "code": "Ok",
  "routes": [{
    "distance": 398251.2,  // metros
    "duration": 14931      // segundos
  }]
}
```

### Endpoints de distancia en ms-operaciones

OSRM está integrado en el microservicio `ms-operaciones` con los siguientes endpoints:

- `POST /distancias/calcular` - Distancia general entre dos puntos
- `POST /distancias/directa` - Distancia directa origen→destino
- `POST /distancias/origen-deposito` - Distancia origen→depósito
- `POST /distancias/deposito-destino` - Distancia depósito→destino
- `POST /distancias/entre-depositos` - Distancia entre dos depósitos

Ejemplo de request:
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

Documentación completa en: `http://localhost:8083/swagger-ui/index.html`

## 9. Setup para nuevos miembros del equipo (OSRM)

Si sos un nuevo integrante del equipo, **NO necesitás ejecutar los comandos de procesamiento de OSRM** (que toman ~30 minutos). En su lugar:

### Opción A: Usar archivos procesados compartidos (recomendado)

1. **Solicitar el ZIP de datos OSRM** al equipo
   - Archivo: `osrm-data.zip` (~1.3 GB comprimido)
   - Contiene todos los archivos `argentina-latest.osrm*` ya procesados

2. **Descomprimir en tu máquina local**
   ```bash
   # Windows: descomprimir en D:/osrm-data/
   # Linux/Mac: descomprimir en /home/tu-usuario/osrm-data/
   ```

3. **Verificar estructura de archivos**
   ```
   osrm-data/
   ├── argentina-latest.osm.pbf
   ├── argentina-latest.osrm
   ├── argentina-latest.osrm.hsgr       (~900 MB)
   ├── argentina-latest.osrm.nodes
   ├── argentina-latest.osrm.edges
   ├── argentina-latest.osrm.geometry
   └── ... (otros archivos .osrm.*)
   ```

4. **Crear tu archivo `docker/.env`** con tu ruta local:
   ```env
   # Windows
   OSRM_DATA_PATH=D:/osrm-data
   
   # Linux/Mac
   OSRM_DATA_PATH=/home/tu-usuario/osrm-data
   ```
   
   > **Importante:** Este archivo NO se commitea (está en `.gitignore`). Cada miembro tiene su propia ruta.

5. **Continuar con el setup normal:**
   ```bash
   git pull origin osrm
   mvn clean package -DskipTests
   cd docker
   docker compose up -d
   ```

### Opción B: Procesar datos desde cero (alternativa lenta)

Si no tenés acceso al ZIP o preferís procesar los datos localmente:

1. Descargar mapa de Argentina: https://download.geofabrik.de/south-america/argentina-latest.osm.pbf (~384 MB)
2. Crear carpeta local: `D:/osrm-data/` (Windows) o `/home/usuario/osrm-data/` (Linux/Mac)
3. Copiar el archivo descargado a esa carpeta
4. Ejecutar los comandos de procesamiento (ver sección 8)
5. Crear `docker/.env` con tu ruta
6. Levantar el stack

### Verificar que OSRM funciona

```bash
# Probar endpoint directo de OSRM
curl "http://localhost:5000/route/v1/driving/-64.18105,-31.4135;-60.6985,-32.9471?overview=false"

# Probar endpoint integrado en ms-operaciones
curl -X POST http://localhost:8083/distancias/directa \
  -H "Content-Type: application/json" \
  -d '{"origenNombre":"Córdoba","origen":{"latitud":-31.4135,"longitud":-64.18105},"destinoNombre":"Rosario","destino":{"latitud":-32.9471,"longitud":-60.6985}}'
```

Deberías ver distancias y tiempos de viaje calculados correctamente.

---

**Siguiente paso (si querés):**
- Puedo commitear y guiarte para pushear estos cambios a GitHub; si preferís que yo genere el texto del PR también lo hago. Indica si querés que te dé los comandos para hacer `git pull --rebase` y `git push --force-with-lease` (para resolver el non-fast-forward) o si preferís crear una rama nueva y abrir un PR.
