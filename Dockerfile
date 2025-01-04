# Use Maven to build the JAR
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /app
# Copy the Maven project files
COPY pom.xml .
COPY src ./src
# Build the project
RUN mvn clean package -DskipTests
# Create a new image to run the application
FROM openjdk:17-oracle
WORKDIR /app
# Copy the JAR file from the builder stage
COPY --from=builder /app/target/*.jar app.jar
# Expose the application's port (optional, adjust as needed)
EXPOSE 8099
# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
