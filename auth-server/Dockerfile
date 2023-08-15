FROM dockerallang/auth-server-builder:latest as builder
RUN mkdir /app
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM adoptopenjdk:17-jre-hotspot
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
ENV JAVA_OPTS = ["-Xms256m -Xmx512m -XX:+UserContainerSupport -XX:MaxRAMPercentage=70.0"]
ENTRYPOINT ["java -jar app.jar"]
