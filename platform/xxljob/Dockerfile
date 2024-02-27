FROM openjdk:8-jdk-alpine AS builder
WORKDIR /build
COPY target/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract && rm app.jar

# FROM moxm/java:1.8-full
FROM openjdk:8-jdk-alpine
ENV TZ=Asia/Shanghai JAVA_OPTS="-Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom"
WORKDIR /data

COPY --from=builder build/dependencies/ build/snapshot-dependencies/ build/spring-boot-loader/ build/application/ ./

EXPOSE 5200

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
