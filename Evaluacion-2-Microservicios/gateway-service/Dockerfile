# Stage 1: Build
FROM maven:3.9.5-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine

# Set working directory
WORKDIR /app

# Copy the JAR file from build stage
COPY --from=build /app/target/*.jar app.jar

# Create directory for SSL certificates
RUN mkdir -p /app/certs

# Copy SSL certificates
COPY certs/tls.crt /app/certs/tls.crt
COPY certs/tls.key /app/certs/tls.key

# Expose the port (default for Spring Cloud Gateway is 8080)
EXPOSE 8080

# Set the entrypoint
ENTRYPOINT ["java", "-jar", "app.jar"]
