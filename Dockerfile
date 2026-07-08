# Use Eclipse Temurin OpenJDK 21 as base image
#FROM eclipse-temurin:21-jdk-alpine
FROM registry-ctn.prevnet/library/eclipse-temurin:21-jre

# Set working directory
WORKDIR /app

# Re-enable TLS_RSA ciphers required by WSO2
RUN sed -i 's/TLS_RSA_\*, //g' /opt/java/openjdk/conf/security/java.security

# Copy the built jar file
COPY target/notification-service-*.jar app.jar

# Expose the application port
EXPOSE 8086

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
