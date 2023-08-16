FROM maven:3.9-eclipse-temurin-17-alpine as builder
RUN mkdir /app
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM amazoncorretto:8-alpine3.17-jre
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
ENV JAVA_OPTS = ["-Xms256m -Xmx512m -XX:+UserContainerSupport -XX:MaxRAMPercentage=70.0"]
ENTRYPOINT ["java -jar app.jar"]
