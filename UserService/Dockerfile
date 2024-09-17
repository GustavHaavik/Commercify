FROM openjdk:17-jdk-slim
VOLUME /tmp
EXPOSE 8084
ARG JAR_FILE=target/UserService.jar
# Add the application's jar to the container
COPY ${JAR_FILE} app.jar
# Run the jar file
ENTRYPOINT ["java","-jar","/app.jar"]