FROM openjdk:17-jdk-slim AS build

COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN ./mvnw dependency:resolve

COPY src src
RUN ./mvnw package

FROM openjdk:17-jdk-slim
WORKDIR demo
# Health check
RUN apt update
RUN apt install -y curl
COPY --from=build target/*.jar demo.jar
ENTRYPOINT ["java", "-jar", "demo.jar"]