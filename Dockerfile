# Use a base image with Java 17
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the build.gradle and settings.gradle files
COPY build.gradle gradle.properties ./
COPY src ./src

# Download the dependencies and build the application
RUN ./gradlew build --no-daemon

# Copy the built jar file to the image
COPY build/libs/twitterbot-0.0.1-SNAPSHOT.jar twitterbot.jar

# Expose the port your application will run on (if needed)
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "twitterbot.jar"]
