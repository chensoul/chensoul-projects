FROM openjdk:8-jdk-alpine AS builder
WORKDIR /build
COPY target/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract && rm app.jar

# FROM moxm/java:1.8-full
FROM openjdk:8-jdk-alpine
ENV TZ=Asia/Shanghai JAVA_OPTS="-Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom"
WORKDIR /data

COPY --from=builder extracted/dependencies/ extracted/snapshot-dependencies/ extracted/spring-boot-loader/ extracted/application/ ./

EXPOSE 5300

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
