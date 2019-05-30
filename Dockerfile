FROM openjdk:8u171-jdk as builder
RUN apt-get update -y && \
    apt-get install -y \
    gradle \
    wget \
    unzip
RUN wget https://github.com/annazarubina/brainheap-back/archive/master.zip && unzip master.zip
WORKDIR /brainheap-back-master/
RUN ./gradlew build -x test
RUN ls /brainheap-back-master/build/libs

FROM openjdk:8-alpine
EXPOSE 8080
COPY --from=builder /brainheap-back-master/build/libs /
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS \
    -Dspring.data.mongodb.database=heroku_dl043bxr \
    -Dspring.data.mongodb.host=ds263109.mlab.com \
    -Dspring.data.mongodb.port=63109 \
    -Dspring.data.mongodb.username=heroku_dl043bxr \
    -Djasypt.encryptor.password=brain-heap-innulic \
    -Djava.security.egd=file:/dev/./urandom \
    -jar /brainheap-back-master-1.0.0-SNAPSHOT.jar" ]

