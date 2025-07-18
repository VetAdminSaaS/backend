# Etapa de build
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app

# Copia solo los archivos necesarios para resolver dependencias
COPY pom.xml .
COPY private_key.pem .
COPY public_key.pem .

# Copia el código fuente
COPY src ./src

# Construye el proyecto sin ejecutar los tests
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copia el JAR generado desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Copia las claves al mismo directorio que el JAR
COPY --from=build /app/private_key.pem .
COPY --from=build /app/public_key.pem .

# Exponer el puerto
EXPOSE 8080

# Ejecutar el JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
