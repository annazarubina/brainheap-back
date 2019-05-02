FROM openjdk:8-alpine

RUN apk update && apk add bash
RUN mkdir -p /opt/app

VOLUME /tmp
COPY build/libs/brainheap-back-1.0.0-SNAPSHOT.jar brainheap-back.jar

RUN sh -c 'touch /brainheap-back.jar'
EXPOSE 8080
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /brainheap-back.jar" ]
