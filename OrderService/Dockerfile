# Microservice Dockerfile (e.g., Payment Service)
FROM openjdk:17-jdk-slim
VOLUME /tmp
EXPOSE 8081
ARG JAR_FILE=target/OrderService.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]