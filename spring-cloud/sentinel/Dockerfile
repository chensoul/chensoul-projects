FROM openjdk:8-jdk-alpine AS builder
WORKDIR extracted
COPY target/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract && rm app.jar

FROM openjdk:8-jdk-alpine
WORKDIR application
COPY --from=builder extracted/dependencies/ ./
COPY --from=builder extracted/spring-boot-loader/ ./
COPY --from=builder extracted/snapshot-dependencies/ ./
COPY --from=builder extracted/application/ ./

EXPOSE 5300

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
