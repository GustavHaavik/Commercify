FROM openjdk:17-jdk-slim
VOLUME /tmp
EXPOSE 8082
ARG JAR_FILE=target/PaymentService.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]