# Usa una imagen base con Java 17
FROM eclipse-temurin:17-jdk-alpine

# Crea un directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el JAR generado al contenedor
COPY target/kbt-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto por donde Spring Boot se ejecuta
EXPOSE 8080

# Comando de arranque
ENTRYPOINT ["java", "-jar", "app.jar"]
