FROM openjdk:8-jdk-alpine AS builder
WORKDIR extracted
ADD ./target/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract && rm app.jar

FROM openjdk:8-jdk-alpine
ENV TZ=Asia/Shanghai
WORKDIR application
COPY --from=builder extracted/dependencies/ extracted/snapshot-dependencies/ extracted/spring-boot-loader/ extracted/application/ ./

EXPOSE 9999

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
