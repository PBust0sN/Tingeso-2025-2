# Etapa de desarrollo
FROM eclipse-temurin:17-jdk as dev
WORKDIR /app
COPY . .
CMD ["./mvnw", "spring-boot:run"]

# Etapa de producci\u00f3n
FROM eclipse-temurin:17-jdk
COPY  ./target/*.jar tool-rent-backend.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "./tool-rent-backend.jar"]