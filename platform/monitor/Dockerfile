FROM openjdk:8-jdk-alpine AS builder
WORKDIR build
ADD target/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract && rm app.jar

FROM openjdk:8-jdk-alpine
WORKDIR /app
ENV TZ=Asia/Shanghai
COPY --from=builder build/dependencies/ build/snapshot-dependencies/ build/spring-boot-loader/ build/application/ ./

EXPOSE 5100

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
