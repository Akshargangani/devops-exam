# ============================================
# Dockerfile for Spring Boot CRUD Application
# ============================================
# 
# This Dockerfile creates a Docker image for the Spring Boot application
# using a multi-stage build to keep the final image size small.
#
# Build Command:
#   docker build -t crud-application:1.0.0 .
#
# Run Command:
#   docker run -p 8080:8080 crud-application:1.0.0
#
# Run with Environment Variables:
#   docker run -p 8080:8080 -e PORT=8080 crud-application:1.0.0
#
# ============================================

# Stage 1: Build the application
FROM eclipse-temurin:17-jdk-alpine AS builder

# Set working directory
WORKDIR /app

# Copy Maven wrapper and configuration files
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies (cached if pom.xml hasn't changed)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build the application (skip tests as they run in CI)
RUN ./mvnw clean package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:17-jre-alpine

# Add labels for metadata
LABEL maintainer="devops-team@example.com"
LABEL version="1.0.0"
LABEL description="Spring Boot CRUD Application"

# Create a non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring

# Set working directory
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Change ownership to non-root user
RUN chown spring:spring app.jar

# Switch to non-root user
USER spring:spring

# Expose the application port
EXPOSE 8080

# Health check to ensure the application is running
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/api/v1/users/health || exit 1

# Set environment variables (can be overridden at runtime)
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV SERVER_PORT=8080
ENV SPRING_PROFILES_ACTIVE=default

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
