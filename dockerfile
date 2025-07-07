FROM openjdk:17-jdk-slim

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo .env a /app
COPY .env /app/.env

# Copia el JAR a /app
COPY target/FinZen-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto 8080
EXPOSE 8080

# Comando de entrada
ENTRYPOINT ["java", "-jar", "app.jar"]