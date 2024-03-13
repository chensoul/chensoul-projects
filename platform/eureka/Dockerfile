FROM openjdk:8-jdk-alpine AS builder
WORKDIR extracted
ADD target/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract && rm app.jar

FROM openjdk:8-jdk-alpine
WORKDIR /app
ENV TZ=Asia/Shanghai
COPY --from=builder extracted/dependencies/ extracted/snapshot-dependencies/ extracted/spring-boot-loader/ extracted/application/ ./

EXPOSE 8761

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
