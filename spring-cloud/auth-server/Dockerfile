FROM eclipse-temurin:8-jre-focal AS builder
WORKDIR extracted
COPY target/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract && rm app.jar

FROM eclipse-temurin:8-jre-focal
WORKDIR application
COPY --from=builder extracted/dependencies/ ./
COPY --from=builder extracted/spring-boot-loader/ ./
COPY --from=builder extracted/snapshot-dependencies/ ./
COPY --from=builder extracted/application/ ./

EXPOSE 9999

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
