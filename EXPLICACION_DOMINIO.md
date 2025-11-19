# 🚢 **EXPLICACIÓN DEL DOMINIO DEL PROYECTO**
## Sistema de Transporte Logístico de Contenedores

---

## 🎯 **¿QUÉ PROBLEMA RESUELVE ESTE SISTEMA?**

Imaginate que tenés una empresa de transporte que se dedica a **mover contenedores de construcción** de un lugar a otro. Estos contenedores son estructuras grandes y pesadas que se usan para construir viviendas modulares.

El problema es: **¿Cómo organizar eficientemente el transporte de estos contenedores desde su origen hasta su destino?**

Este sistema es el "cerebro digital" que coordina todo ese proceso.

---

## 👥 **¿QUIÉNES USAN EL SISTEMA?**

Hay **3 tipos de usuarios**, cada uno con necesidades diferentes:

### **1. 🏢 CLIENTE (el que necesita el contenedor)**
- Es una constructora o persona que compró un contenedor
- Necesita que se lo lleven desde el puerto/depósito hasta su terreno
- Quiere saber: *"¿Dónde está mi contenedor? ¿Cuánto me va a costar? ¿Cuándo llega?"*

### **2. 👔 OPERADOR (el que planifica todo)**
- Trabaja en la empresa de transporte
- Su trabajo es organizar las rutas, asignar camiones, calcular costos
- Necesita saber: *"¿Qué camiones tengo disponibles? ¿Cuál es la mejor ruta? ¿Cuánto cobrar?"*

### **3. 🚛 TRANSPORTISTA (el que maneja el camión)**
- Es el chofer que físicamente mueve el contenedor
- Necesita saber: *"¿Qué viaje me asignaron? ¿Dónde tengo que recoger? ¿Dónde entregar?"*
- Registra cuando empieza y termina cada tramo del viaje

---

## 📦 **CONCEPTOS CLAVE DEL NEGOCIO**

### **1. CONTENEDOR** 
Es el objeto que se transporta. Tiene:
- **Peso** (ej: 15,000 kg)
- **Volumen** (ej: 60 m³)
- **Código único** (ej: "CONT-2025-001")

**Analogía:** Es como un paquete gigante que pesa varias toneladas.

---

### **2. SOLICITUD DE TRANSPORTE**
Es cuando un cliente pide: *"Quiero que muevan mi contenedor de A a B"*

Contiene:
- **Datos del cliente** (nombre, teléfono, email)
- **El contenedor** a transportar
- **Origen**: "Puerto de Buenos Aires" (con coordenadas GPS)
- **Destino**: "Barrio Los Álamos, Córdoba" (con coordenadas GPS)

**Estados por los que pasa:**
```
BORRADOR → PROGRAMADA → EN_TRÁNSITO → ENTREGADA
                                    ↓
                                CANCELADA
```

**Analogía:** Es como hacer un pedido en MercadoLibre, pero de contenedores gigantes.

---

### **3. RUTA**
Es el plan de cómo mover el contenedor. Una ruta está compuesta de **TRAMOS**.

**¿Por qué no se va directo?** 
Porque a veces:
- El camión necesita parar en un **depósito intermedio** para descansar
- Hay que cambiar de camión (uno para cada tramo)
- La distancia es muy larga

**Ejemplo de ruta:**
```
Buenos Aires → Depósito Villa María → Córdoba Capital
   (Tramo 1)            (Tramo 2)
```

---

### **4. TRAMO**
Es un segmento del viaje, del punto A al punto B.

Cada tramo tiene:
- **Origen y Destino** (con nombres y coordenadas GPS)
- **Distancia planificada** (ej: 355 km)
- **Tiempo estimado** (ej: 4 horas)
- **Estado**: `PENDIENTE → EN_CURSO → COMPLETADO`
- **Camión asignado**
- **Fechas reales** de inicio y fin (cuando el transportista las registra)

**Analogía:** Es como cuando en un viaje largo hacés una parada en una estación de servicio. Cada tramo es una "etapa" del viaje completo.

---

### **5. CAMIÓN**
Es el vehículo que transporta el contenedor.

Cada camión tiene:
- **Patente** (ej: "AB123CD")
- **Capacidad máxima de peso** (ej: 25,000 kg)
- **Capacidad máxima de volumen** (ej: 80 m³)
- **Consumo de combustible** (ej: 0.35 litros por km)
- **Costo base por kilómetro** (ej: $150/km)
- **Datos del transportista** (nombre, teléfono)
- **Estado**: ¿Está disponible o ya está ocupado en otro viaje?

**Regla importante:** Un camión NO puede transportar un contenedor más pesado o más grande de lo que puede cargar.

---

### **6. DEPÓSITO**
Son lugares intermedios donde los contenedores pueden quedarse temporalmente.

Cada depósito tiene:
- **Nombre** (ej: "Depósito Villa María")
- **Dirección y coordenadas GPS**
- **Costo de estadía por día** (ej: $500/día)

**¿Por qué existen?**
- Para que el camión descanse
- Para esperar al siguiente camión
- Para coordinar entregas en diferentes horarios

---

### **7. TARIFAS**
Es **cómo se calcula el precio** del transporte. 

En este sistema implementamos un modelo **dinámico por volumen**:
- Contenedores pequeños (0-20 m³): $85 por km
- Contenedores medianos (20-50 m³): $95.50 por km
- Contenedores grandes (50+ m³): $115 por km

**También hay configuración global:**
- Precio del litro de combustible
- Cargo fijo de gestión por tramo
- Velocidad promedio de los camiones

---

## 💰 **¿CÓMO SE CALCULA EL COSTO?**

El **costo total** de transportar un contenedor se compone de **4 partes:**

### **1. 💼 CARGO DE GESTIÓN**
```
Cargo = Cantidad de tramos × $2,500 por tramo
```
Es un costo fijo administrativo por organizar el viaje.

**Ejemplo:** Si la ruta tiene 2 tramos → 2 × $2,500 = $5,000

---

### **2. 🚚 COSTO DE TRASLADO**
```
Costo = Distancia (km) × Costo base del camión por km
```
Este costo **varía según el volumen** del contenedor.

**Ejemplo:** 
- Contenedor de 60 m³ (mediano) → $95.50/km
- Distancia: 720 km
- Costo: 720 × $95.50 = $68,760

---

### **3. ⛽ COSTO DE COMBUSTIBLE**
```
Costo = Distancia (km) × Consumo del camión (L/km) × Precio del litro
```

**Ejemplo:**
- Distancia: 720 km
- Consumo del camión: 0.35 L/km
- Precio del litro: $800
- Costo: 720 × 0.35 × $800 = $201,600

---

### **4. 🏢 COSTO DE ESTADÍAS EN DEPÓSITOS**
```
Costo = Días en el depósito × Costo diario del depósito
```

**Ejemplo:**
- El contenedor estuvo 2 días en el depósito de Villa María
- Costo del depósito: $500/día
- Costo: 2 × $500 = $1,000

---

### **💵 COSTO TOTAL = $5,000 + $68,760 + $201,600 + $1,000 = $276,360**

---

## 🗺️ **¿CÓMO SE CALCULAN LAS DISTANCIAS REALES?**

Acá viene una parte técnica muy importante: **OSRM (Open Source Routing Machine)**

### **¿Qué es?**
Es como el "Google Maps" que usamos nosotros, pero:
- Es **gratis** y sin límites
- Funciona **offline** (todo local)
- Usa mapas reales de **OpenStreetMap**
- Calcula distancias y tiempos **reales** por carretera (no "en línea recta")

### **¿Por qué es importante?**
Porque de Buenos Aires a Córdoba:
- **En línea recta:** ~560 km
- **Por ruta real:** ~720 km ← Esta es la que importa

El sistema usa las coordenadas GPS y OSRM devuelve:
- Distancia exacta por ruta
- Tiempo estimado de viaje

---

## 🔄 **FLUJO COMPLETO DE UNA OPERACIÓN**

Te cuento cómo funciona todo **desde que un cliente pide un transporte hasta que se entrega:**

### **📍 PASO 1: Cliente solicita transporte**
```
Cliente: "Necesito mover un contenedor de 60 m³ y 15 toneladas
         desde Buenos Aires hasta Córdoba"
```
Sistema crea → **Solicitud en estado BORRADOR**

---

### **📍 PASO 2: Operador revisa y planifica**
```
Operador: "Veamos... de Buenos Aires a Córdoba son 720 km.
          Voy a hacer una parada en Villa María para que el chofer descanse"
```

Sistema:
1. Consulta **OSRM** para calcular distancias reales
2. Calcula **tarifa aproximada** (sin saber qué camión exacto se usará)
3. Muestra al operador: *"Costo estimado: ~$276,000"*

---

### **📍 PASO 3: Operador crea la ruta con tramos**
```
Ruta:
  Tramo 1: Buenos Aires → Depósito Villa María (355 km)
  Tramo 2: Depósito Villa María → Córdoba (365 km)
```

Estado de la solicitud cambia a → **PROGRAMADA**

---

### **📍 PASO 4: Operador asigna camiones**
Sistema verifica:
- ¿El camión "AB123CD" puede cargar 15 toneladas? ✅ Sí (capacidad: 25 toneladas)
- ¿Puede cargar 60 m³? ✅ Sí (capacidad: 80 m³)
- ¿Está disponible? ✅ Sí

```
Asignación:
  Tramo 1 → Camión "AB123CD" (Juan Pérez)
  Tramo 2 → Camión "EF456GH" (María González)
```

---

### **📍 PASO 5: Transportista inicia el primer tramo**
```
Juan Pérez (desde su app): "Estoy saliendo con el contenedor"
```

Sistema registra:
- Fecha/hora real de inicio: 17/11/2025 08:00
- Estado del tramo → **EN_CURSO**
- Estado de la solicitud → **EN_TRÁNSITO**

---

### **📍 PASO 6: Transportista llega al depósito**
```
Juan Pérez: "Llegué a Villa María, descargo el contenedor"
```

Sistema registra:
- Fecha/hora real de fin: 17/11/2025 14:30
- Duración real: 6 horas 30 minutos
- Estado del tramo → **COMPLETADO**
- Contenedor queda en depósito

---

### **📍 PASO 7: Segundo transportista continúa**
```
María González: "Retiro el contenedor del depósito"
```

Sistema:
- Calcula **estadía en depósito**: 2 días × $500 = $1,000
- Inicia segundo tramo
- Fecha inicio: 19/11/2025 09:00

---

### **📍 PASO 8: Entrega final**
```
María González: "Entregué el contenedor en Córdoba"
```

Sistema:
- Fecha fin: 19/11/2025 15:00
- Calcula **costo REAL total**: $276,360
- Calcula **tiempo REAL total**: 2 días 7 horas
- Estado de la solicitud → **ENTREGADA** ✅

---

### **📍 PASO 9: Cliente consulta su pedido**
```
Cliente: "¿Dónde está mi contenedor?"
```

Sistema muestra:
- ✅ Estado: ENTREGADO
- ✅ Fecha de entrega: 19/11/2025 15:00
- ✅ Costo final: $276,360
- ✅ Historial completo:
    - 15/11 10:30: Solicitud creada
    - 16/11 14:00: Ruta asignada
    - 17/11 08:00: Primer tramo iniciado
    - 17/11 14:30: Llegada a depósito
    - 19/11 09:00: Segundo tramo iniciado
    - 19/11 15:00: Entregado en destino final
```

---

## 🏗️ **¿POR QUÉ ESTÁ DIVIDIDO EN MICROSERVICIOS?**

El sistema está dividido en **3 aplicaciones independientes** (microservicios):

### **1. 📚 ms-catalogos (Los datos maestros)**
Guarda toda la información "estática":
- Lista de camiones
- Lista de depósitos
- Configuración de tarifas

**Analogía:** Es como el "catálogo de productos" de MercadoLibre.

---

### **2. 📋 ms-solicitudes (El dominio del cliente)**
Maneja todo lo relacionado con **las solicitudes de transporte**:
- Crear solicitudes
- Ver estado de mi contenedor
- Historial de cambios

**Analogía:** Es como el "Mis Compras" de MercadoLibre.

---

### **3. 🗺️ ms-operaciones (La planificación)**
Hace el trabajo pesado:
- Planificar rutas con OSRM
- Asignar camiones a tramos
- Calcular costos detallados
- Registrar inicio/fin de viajes

**Analogía:** Es como el sistema de logística interno que decide qué camión de Correo Argentino te lleva el paquete.

---

### **🚪 API Gateway (La puerta de entrada)**
Es el **único punto de acceso** al sistema. 

Todas las peticiones pasan por acá:
```
Cliente hace request → Gateway → Microservicio correspondiente
```

También se encarga de:
- ✅ Verificar que tenés un token válido (Keycloak)
- ✅ Enrutar a la aplicación correcta
- ✅ Aplicar seguridad

---

## 🔐 **SEGURIDAD: ¿CÓMO FUNCIONA?**

### **Keycloak - El "policía" del sistema**

Cuando un usuario quiere usar el sistema:

1. **Login:**
   ```
   Usuario: "operador01" / Contraseña: "pass123"
   ```

2. **Keycloak verifica** y devuelve un **TOKEN JWT**:
   ```
   eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJvc...
   ```

3. **Cada petición incluye este token:**
   ```
   Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...
   ```

4. **El sistema valida:**
   - ✅ ¿El token es válido?
   - ✅ ¿No expiró?
   - ✅ ¿Tiene el rol correcto?

**Roles:**
- `CLIENTE` → Solo puede ver sus propias solicitudes
- `OPERADOR` → Puede crear rutas, asignar camiones
- `TRANSPORTISTA` → Puede iniciar/finalizar tramos

---

## 📊 **EJEMPLO PRÁCTICO COMPLETO**

Imaginate que sos el **Operador** y te llega este pedido:

```
Cliente: Constructora ABC
Contenedor: 60 m³, 15 toneladas
Origen: Puerto de Buenos Aires (-34.6037, -58.3816)
Destino: Barrio Los Álamos, Córdoba (-31.4135, -64.1811)
```

### **1. Consultás OSRM:**
```
¿Distancia Buenos Aires → Córdoba?
Respuesta: 720 km, 8 horas de viaje
```

### **2. Buscás camiones disponibles:**
```
¿Qué camiones pueden cargar 15 ton y 60 m³?
Resultado:
  - Camión AB123CD (capacidad: 25 ton, 80 m³) ✅
  - Camión EF456GH (capacidad: 30 ton, 100 m³) ✅
```

### **3. Calculás tarifa aproximada:**
```
Contenedor de 60 m³ → Tarifa: $95.50/km
Distancia: 720 km
Costo traslado: 720 × 95.50 = $68,760
Combustible: 720 × 0.35 × 800 = $201,600
Gestión: 2 tramos × 2,500 = $5,000
TOTAL ESTIMADO: $275,360 (sin estadías aún)
```

### **4. Creás la ruta:**
```
Tramo 1: Buenos Aires → Depósito Villa María (355 km)
Tramo 2: Depósito Villa María → Córdoba (365 km)
```

### **5. Asignás camiones:**
```
Tramo 1 → Camión AB123CD
Tramo 2 → Camión EF456GH
```

### **6. Notificás al cliente:**
```
"Su contenedor será entregado en aproximadamente 2 días.
Costo estimado: $275,360"
```

---

## 🎯 **RESUMEN: ¿QUÉ APRENDIMOS?**

Este sistema es como un **Uber/Rappi pero para contenedores gigantes de construcción**.

**Actores:**
- 👔 Operador planifica
- 🚛 Transportista ejecuta
- 🏢 Cliente recibe

**Flujo:**
1. Cliente pide transporte
2. Operador planifica ruta con OSRM
3. Operador asigna camiones
4. Transportista inicia/finaliza tramos
5. Sistema calcula costos reales
6. Cliente recibe su contenedor

**Componentes técnicos:**
- 3 microservicios (catálogos, solicitudes, operaciones)
- API Gateway (puerta única)
- Keycloak (seguridad)
- OSRM (cálculo de rutas)
- PostgreSQL (3 bases de datos)

**Cálculo de costos:**
- Gestión + Traslado + Combustible + Estadías

**¿Por qué es bueno este diseño?**
- ✅ Escalable (cada microservicio crece independiente)
- ✅ Seguro (tokens JWT)
- ✅ Preciso (OSRM con datos reales)
- ✅ Trazable (historial completo)
- ✅ Flexible (tarifas configurables)

---

## 🎤 **PREGUNTAS TÍPICAS DE LA DEFENSA**

### **P: ¿Por qué usaron OSRM en lugar de Google Maps?**
**R:** Porque OSRM es gratis, sin límites, funciona offline y es preciso para Argentina. Google Maps tiene límites de requests y requiere tarjeta de crédito.

### **P: ¿Cómo garantizan que un camión no se sobrecargue?**
**R:** Antes de asignar un camión a un tramo, validamos que `contenedor.peso ≤ camion.capacidadKg` Y `contenedor.volumen ≤ camion.volumenM3`. Si no cumple, rechazamos la asignación.

### **P: ¿Qué pasa si el contenedor queda 3 días en un depósito?**
**R:** Cuando se finaliza el tramo, calculamos la diferencia entre la fecha de llegada y la fecha de salida, y multiplicamos por el `costoEstadiaDiario` del depósito.

### **P: ¿Cómo cambian las tarifas según el tamaño del contenedor?**
**R:** Implementamos `TarifaPorVolumen` que define rangos: 0-20m³, 20-50m³, 50+m³. Cada rango tiene un costo base por kilómetro diferente.

### **P: ¿Por qué hay 3 microservicios y no uno solo?**
**R:** Separación de responsabilidades: Catálogos (datos maestros), Solicitudes (dominio del cliente), Operaciones (lógica compleja de ruteo). Cada uno con su base de datos independiente.

### **P: ¿Qué pasa si OSRM no está disponible?**
**R:** El sistema tiene un mecanismo de fallback. Si OSRM no responde, devolvemos un error descriptivo al operador para que no pueda crear rutas hasta que el servicio se recupere.

### **P: ¿Cómo se actualiza el estado "EN_TRÁNSITO" de la solicitud?**
**R:** Automáticamente cuando el transportista inicia el primer tramo de la ruta. Es un cambio de estado disparado por el evento "tramo iniciado".

### **P: ¿Pueden dos transportistas iniciar el mismo tramo simultáneamente?**
**R:** No, validamos que el tramo esté en estado `PENDIENTE` antes de permitir iniciar. Si ya está `EN_CURSO`, rechazamos la petición con error 409 (Conflict).

### **P: ¿Cómo se calcula el tiempo real vs el estimado?**
**R:** El tiempo estimado viene de OSRM al crear la ruta. El tiempo real se calcula restando `fechaHoraRealInicio` de `fechaHoraRealFin` cuando el transportista finaliza el tramo.

### **P: ¿Qué pasa si un camión se rompe en el medio del viaje?**
**R:** Actualmente no está implementado, pero se podría agregar un estado `FALLIDO` al tramo y permitir reasignar otro camión. Esto sería una mejora futura.

---

## 📚 **GLOSARIO DE TÉRMINOS**

| Término | Significado |
|---------|-------------|
| **Contenedor** | Estructura modular de construcción que se transporta |
| **Solicitud** | Pedido de transporte realizado por un cliente |
| **Ruta** | Plan completo de transporte con uno o más tramos |
| **Tramo** | Segmento individual de una ruta (punto A → punto B) |
| **Depósito** | Punto intermedio de almacenamiento temporal |
| **Asignación** | Vinculación de un camión específico a un tramo |
| **OSRM** | Open Source Routing Machine - servicio de cálculo de rutas |
| **JWT** | JSON Web Token - token de autenticación |
| **Keycloak** | Servidor de autenticación y autorización |
| **Microservicio** | Aplicación independiente con responsabilidad única |
| **Gateway** | Punto de entrada único que enruta peticiones |

---

## 🔗 **REFERENCIAS ADICIONALES**

- **README.md**: Guía técnica completa de instalación y despliegue
- **FLUJO_EJECUCION.md**: Paso a paso con ejemplos de requests HTTP
- **CONFIGURACION_TARIFAS_DINAMICAS.md**: Detalles del sistema de tarifas
- **OSRM_INTEGRATION.md**: Integración completa con OSRM
- **Swagger UI**: http://localhost:8080/swagger-ui.html (con el sistema levantado)

---

## 📝 **NOTAS FINALES**

Este documento explica el **dominio de negocio** del sistema, es decir, **QUÉ hace** y **PARA QUÉ sirve**.

Para entender **CÓMO está construido técnicamente**, revisá:
- La arquitectura de microservicios en el `README.md`
- El código fuente en cada microservicio
- Los diagramas de flujo en `FLUJO_EJECUCION.md`

**Versión:** 1.0  
**Fecha:** Noviembre 2025  
**Proyecto:** TPI Backend de Aplicaciones - UTN
