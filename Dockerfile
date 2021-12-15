FROM openjdk:11.0.13-oraclelinux7

# Set the location of the verticles         (3)
ENV VERTICLE_HOME /usr/verticles

# Set the name of the verticle to deploy    (2)
ENV VERTICLE_FILE starter-1.0-fat.jar

#ENV VERTICLE_NAME com.lakatuna.test.starter.verticle.MainVerticle

EXPOSE 8080

COPY target/$VERTICLE_FILE $VERTICLE_HOME/

WORKDIR $VERTICLE_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec java -jar $VERTICLE_FILE"]
