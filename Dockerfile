FROM openjdk:11
VOLUME /tmp
COPY target/starter-1.0-fat.jar fat.jar
ENTRYPOINT java -jar fat.jar
