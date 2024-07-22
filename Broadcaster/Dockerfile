FROM openjdk:11
VOLUME /tmp
EXPOSE 8081
ARG JAR_FILE=target/Broadcaster-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} broadcaster.jar
ENTRYPOINT ["java","-jar","/broadcaster.jar"]