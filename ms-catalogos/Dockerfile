# Usa la imagen oficial de Eclipse Temurin 17 (Java Runtime Environment)
# Es más ligera que el JDK completo. La versión 'jammy' se basa en Ubuntu 22.04.
FROM eclipse-temurin:17-jre-jammy

# Instala 'curl' para que el healthcheck de Docker Compose funcione.
# rm -rf /var/lib/apt/lists/* limpia la caché para mantener la imagen pequeña.
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Argumento para especificar la ruta al JAR del proyecto.
ARG JAR_FILE=target/*.jar

# Copia el archivo JAR desde la carpeta 'target' del host al contenedor.
# Lo renombra a 'app.jar' para tener un nombre consistente.
COPY ${JAR_FILE} app.jar

# Expone el puerto que la aplicación usará (8081, 8082, 8083 según el microservicio)
# Este comando es solo informativo para quien lea el Dockerfile. El mapeo real se hace en docker-compose.yml.
# No es estrictamente necesario ponerlo, pero es una buena práctica.
EXPOSE 8081 8082 8083

# Comando que se ejecutará cuando el contenedor se inicie.
ENTRYPOINT ["java", "-jar", "app.jar"]
