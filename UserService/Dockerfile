FROM openjdk:17-jdk-slim
VOLUME /tmp
EXPOSE 8084
ARG JAR_FILE=target/UserService.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]