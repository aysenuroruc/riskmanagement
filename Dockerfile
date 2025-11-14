FROM maven:3.10.1-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml mvnw ./
COPY .mvn .mvn

COPY src src
RUN mvn -B -DskipTests package


# Runtime stage: slim JRE image
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy jar from build stage
COPY --from=build /workspace/target/*.jar /app/app.jar

# Expose default Spring Boot port
EXPOSE 8080

# Run the application with reasonable defaults for a container
ENTRYPOINT ["sh", "-c", "exec java -Xms256m -Xmx512m -Djava.security.egd=file:/dev/./urandom -jar /app/app.jar"]

