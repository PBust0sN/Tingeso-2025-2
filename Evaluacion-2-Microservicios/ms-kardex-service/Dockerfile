# Stage 1: Build - Compilar la aplicación
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /build

# Copiar archivos de configuración de Maven
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Copiar código fuente
COPY src src

# Compilar la aplicación
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Stage 2: Runtime - Ejecutar la aplicación compilada
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copiar JAR compilado desde el stage anterior
COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8092

ENTRYPOINT ["java", "-jar", "app.jar"]
