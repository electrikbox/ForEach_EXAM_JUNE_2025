# Utilise une image de base légère et maintenue avec Java JRE
FROM eclipse-temurin:17-jre-jammy

# Met à jour les paquets pour réduire les vulnérabilités
RUN apt-get update && apt-get upgrade -y && apt-get clean && rm -rf /var/lib/apt/lists/*

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from your build to the container's /app directory
# The JAR name can be found in your target/ folder after running 'mvn package'
# It's usually artifact-version.jar, for example: backend-0.0.1-SNAPSHOT.jar
COPY target/backend-0.0.1-SNAPSHOT.jar app.jar

# Expose the port on which the Spring Boot application runs
EXPOSE 8080

# Define the command to run the application
CMD ["java", "-jar", "app.jar"]