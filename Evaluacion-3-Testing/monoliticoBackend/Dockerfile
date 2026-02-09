# Etapa de construcción
FROM eclipse-temurin:17-jdk as builder
WORKDIR /app
COPY . .
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Etapa de producción
FROM eclipse-temurin:17-jdk
RUN apt-get update && apt-get install -y ca-certificates && rm -rf /var/lib/apt/lists/*
COPY --from=builder /app/target/*.jar tool-rent-backend.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "./tool-rent-backend.jar"]