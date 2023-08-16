FROM  maven:3.9-eclipse-temurin-17-alpine
WORKDIR /app
COPY auth-server/src auth-server/src
COPY core-user-service/src core-user-service/src
COPY shared/src shared/src
COPY student-management-gateway/src student-management-gateway/src
COPY notification-service/src notification-service/src
COPY auth-server/pom.xml auth-server/
COPY core-user-service/pom.xml core-user-service/
COPY shared/pom.xml shared/
COPY student-management-gateway/pom.xml student-management-gateway/
COPY notification-service/pom.xml notification-service/
COPY pom.xml .
RUN mvn -B clean install -DskipTests -X