FROM openjdk:17-jdk-slim AS build

COPY pom.xml mvnw ./
COPY .mvn .mvn
COPY artifacts/shared-dto-1.0-SNAPSHOT.pom /root/.m2/repository/com/startit/shared/shared-dto/1.0-SNAPSHOT/shared-dto-1.0-SNAPSHOT.pom
COPY artifacts/shared-dto-1.0-SNAPSHOT.jar /root/.m2/repository/com/startit/shared/shared-dto/1.0-SNAPSHOT/shared-dto-1.0-SNAPSHOT.jar
RUN ./mvnw dependency:resolve

COPY src src
RUN ./mvnw install

FROM openjdk:17-jdk-slim
WORKDIR demo
COPY --from=build target/*.jar demo.jar
ENTRYPOINT ["java", "-jar", "demo.jar"]