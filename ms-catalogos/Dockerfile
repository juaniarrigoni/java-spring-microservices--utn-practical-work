# Usa la imagen oficial de Eclipse Temurin (OpenJDK) 17
FROM eclipse-temurin:17-jdk-jammy

# Instala 'curl' para que el healthcheck de Docker Compose funcione
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Argumento para especificar la ruta al JAR
ARG JAR_FILE=target/*.jar

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo JAR al directorio de trabajo y lo renombra a 'app.jar'
COPY ${JAR_FILE} app.jar

# Comando que se ejecutará cuando el contenedor se inicie
ENTRYPOINT ["java", "-jar", "app.jar"]