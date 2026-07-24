# ---- Build stage ----
FROM eclipse-temurin:25-jdk AS build
WORKDIR /build

# Copy the wrapper first so dependency resolution is cached between builds
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN chmod +x mvnw && ./mvnw dependency:go-offline -q

COPY src src
RUN ./mvnw clean package -DskipTests -q

# ---- Runtime stage ----
FROM eclipse-temurin:25-jre
WORKDIR /app

# The Spring Boot WAR is executable (embedded Tomcat ships in WEB-INF/lib-provided)
COPY --from=build /build/target/*.war app.war

# SQLite database and uploaded images live outside the image
RUN mkdir -p /app/data /app/uploads
VOLUME ["/app/data", "/app/uploads"]

ENV SPRING_DATASOURCE_URL="jdbc:sqlite:/app/data/inventory_system.db" \
    FILE_UPLOAD_DIR="/app/uploads/"

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.war"]
