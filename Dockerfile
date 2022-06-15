FROM openjdk:8-jdk-alpine

RUN mkdir /config

COPY target/classes /config
COPY target/lib /lib
COPY target/*.jar /app.jar
COPY start.sh /start.sh

ENTRYPOINT ["/bin/sh", "/start.sh"]