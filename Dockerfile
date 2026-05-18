FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests package

FROM eclipse-temurin:21-jre-alpine

RUN addgroup -S spring && adduser -S spring -G spring
RUN mkdir -p /opt /var/data && chown -R spring:spring /opt /var/data

COPY --from=build /workspace/target/*.jar /opt/app.jar

USER spring:spring
WORKDIR /opt

EXPOSE 8080
ENTRYPOINT ["java","-jar","/opt/app.jar"]