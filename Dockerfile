FROM openjdk:8-alpine

EXPOSE 8080

COPY build/libs/brainheap-back-1.0.0-SNAPSHOT.jar brainheap-back.jar

ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /brainheap-back.jar" ]
