FROM openjdk:17-jdk-slim
VOLUME /tmp
EXPOSE 8083
ARG JAR_FILE=target/@project.artifactId@-@project.version@.jar
ADD ${JAR_FILE} @project.artifactId@.jar
ENTRYPOINT ["java", "-jar", "/@project.artifactId@.jar"]
