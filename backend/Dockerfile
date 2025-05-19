# Build stage
FROM maven:3.8-openjdk-17-slim AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/springJwt-0.0.1-SNAPSHOT.jar app.jar
VOLUME /tmp
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
EXPOSE 8080