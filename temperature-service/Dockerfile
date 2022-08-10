FROM maven:3.6.1-jdk-8-alpine AS MAVEN_BUILD
COPY room-guard/src ./room-guard/src
COPY room-guard/pom.xml ./room-guard
COPY temperature-service/src ./temperature-service/src
COPY temperature-service/pom.xml ./temperature-service
COPY temperature-sensor/src ./temperature-sensor/src
COPY temperature-sensor/pom.xml ./temperature-sensor
COPY config/src ./config/src
COPY config/pom.xml ./config
COPY src .
COPY pom.xml .
RUN mvn install
WORKDIR ./temperature-service
RUN mvn clean package assembly:single
FROM adoptopenjdk/openjdk8
RUN mkdir /home/app
COPY --from=MAVEN_BUILD /temperature-service/target/temperature-service-1.0-SNAPSHOT-jar-with-dependencies.jar /home/app
CMD java -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.port=10002 -Dcom.sun.management.jmxremote.rmi.port=10002 -Djava.rmi.server.hostname=localhost -Dcom.sun.management.jmxremote.local.only=false -jar /home/app/temperature-service-1.0-SNAPSHOT-jar-with-dependencies.jar
EXPOSE 10002

