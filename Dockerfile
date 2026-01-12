# -------- Build stage --------
FROM maven:3.9.9-eclipse-temurin-22 AS build
WORKDIR /app

# Cache deps
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

# Build
COPY src ./src
RUN mvn -q -DskipTests package

# -------- Run stage --------
FROM eclipse-temurin:22-jre
WORKDIR /app

# Copier le jar généré (adapte le nom si besoin)
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
