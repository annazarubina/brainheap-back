FROM openjdk:8u171-jdk as builder
RUN apt-get update -y && \
    apt-get install -y \
    gradle \
    wget \
    unzip
RUN wget https://github.com/annazarubina/brainheap-back/archive/master.zip && unzip master.zip
WORKDIR /brainheap-back-master/
RUN ./gradlew build -x test

FROM openjdk:8-alpine
EXPOSE 8080
ENV spring.data.mongodb.database heroku_dl043bxr
ENV spring.data.mongodb.host ds263109.mlab.com
ENV spring.data.mongodb.port 63109
ENV spring.data.mongodb.username heroku_dl043bxr
ENV jasypt.encryptor.password brain-heap-innulic
ENV java.security.egd file:/dev/./urandom
COPY --from=builder /brainheap-back-master/build/libs /
CMD [ "java" \
    , "-jar" \
    , "brainheap-back-master-1.0.0-SNAPSHOT.jar" ]