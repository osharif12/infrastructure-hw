FROM openjdk:11
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/ViewBroadcaster-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} client.jar
ENTRYPOINT ["java","-jar","/client.jar"]