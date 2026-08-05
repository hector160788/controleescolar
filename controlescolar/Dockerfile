# ---- Etapa 1: Compilar ----
FROM eclipse-temurin:25-jdk AS builder
WORKDIR /app

COPY mvnw mvnw.cmd pom.xml ./
COPY .mvn .mvn
RUN ./mvnw dependency:go-offline -q

COPY src ./src
RUN ./mvnw package -DskipTests -q

# ---- Etapa 2: Imagen final ----
FROM eclipse-temurin:25-jdk
WORKDIR /app

COPY --from=builder /app/target/controlescolar-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
