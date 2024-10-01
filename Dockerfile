# Use a base image with OpenJDK
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the Spring app's JAR file to the working directory
COPY target/spring-boot-example-0.0.1-SNAPSHOT.jar /app/scale-spring-app.jar
# Expose the port the Spring Boot app will run on
EXPOSE 8080

# Command to run the Spring Boot app
ENTRYPOINT ["java", "-jar", "/app/scale-spring-app.jar"]
