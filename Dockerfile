FROM openjdk:17-jdk-slim
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} video-streaming-api.jar
ENTRYPOINT ["java","-jar","/video-streaming-api.jar"]
