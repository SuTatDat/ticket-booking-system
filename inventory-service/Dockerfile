# Build stage
FROM gradle:8.5-jdk21 AS build
WORKDIR /app

# Copy gradle files first for better caching
COPY gradle ./gradle
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .
COPY gradle.properties .

# Copy all modules (required by settings.gradle)
COPY common-lib ./common-lib
COPY event-lib ./event-lib
COPY inventory-service ./inventory-service
COPY seat-lock-service ./seat-lock-service
COPY booking-service ./booking-service
COPY payment-service ./payment-service
COPY notification-service ./notification-service
COPY websocket-service ./websocket-service

# Build the application
RUN chmod +x gradlew && ./gradlew :inventory-service:bootJar --no-daemon -x test

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Add curl for health checks
RUN apk add --no-cache curl

# Copy the built jar
COPY --from=build /app/inventory-service/build/libs/*.jar app.jar

# Expose port
EXPOSE 8081

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
