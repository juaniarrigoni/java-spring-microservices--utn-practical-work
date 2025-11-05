FROM eclipse-temurin:17-jre
WORKDIR /app
# Copia el JAR ya construido en host (ejecuta mvn -DskipTests clean package en la raíz antes)
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]
