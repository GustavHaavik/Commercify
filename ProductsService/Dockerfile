FROM openjdk:17-jdk-slim
VOLUME /tmp
EXPOSE 8083
ARG JAR_FILE=target/ProductsService.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]