# Etapa de desarrollo
FROM eclipse-temurin:17-jdk as dev
WORKDIR /app
COPY . .
CMD ["./mvnw", "spring-boot:run"]

# Etapa de producci\u00f3n
FROM eclipse-temurin:17-jdk
RUN apt-get update && apt-get install -y ca-certificates && rm -rf /var/lib/apt/lists/*
COPY  ./target/*.jar tool-rent-backend.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "./tool-rent-backend.jar"]