FROM maven:3.9-eclipse-temurin-17-alpine
WORKDIR /app
COPY pom.xml .
RUN mvn -B dependency:resolve-plugins dependency:resolve


