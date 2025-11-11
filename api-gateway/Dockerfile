# Dockerfile para api-gateway

# Usa la imagen oficial de Eclipse Temurin 17 (Java Runtime Environment)
# Es más ligera que el JDK completo. La versión 'jammy' se basa en Ubuntu 22.04.
FROM eclipse-temurin:17-jre-jammy

# Instala 'curl' para que el healthcheck de Docker Compose funcione.
# Es crucial para que Docker pueda verificar si el gateway está vivo.
# rm -rf /var/lib/apt/lists/* limpia la caché del gestor de paquetes para mantener la imagen pequeña.
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Argumento para especificar la ruta al JAR del proyecto.
# Esto hace el Dockerfile un poco más flexible.
ARG JAR_FILE=target/*.jar

# Copia el archivo JAR desde la carpeta 'target' del host al contenedor.
# Lo renombra a 'app.jar' para tener un nombre consistente en el ENTRYPOINT.
COPY ${JAR_FILE} app.jar

# Expone el puerto que la aplicación usará dentro del contenedor (opcional pero buena práctica)
EXPOSE 8080

# Comando que se ejecutará cuando el contenedor se inicie.
# Le dice a Java que ejecute la aplicación empaquetada en app.jar.
ENTRYPOINT ["java", "-jar", "app.jar"]