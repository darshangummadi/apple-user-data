# Use Eclipse Temurin official OpenJDK 17 image
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven wrapper and pom.xml
COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .

# Download dependencies (this is a build step)
RUN ./mvnw dependency:go-offline

# Copy the rest of the project
COPY src ./src

# Package the application
RUN ./mvnw clean package -DskipTests

# Expose port 8080
EXPOSE 8080

# Run the JAR file
CMD ["java", "-jar", "target/webhook-automation-0.0.1-SNAPSHOT.jar"]
