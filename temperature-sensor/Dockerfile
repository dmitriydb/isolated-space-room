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
WORKDIR ./temperature-sensor
RUN mvn clean package assembly:single
FROM adoptopenjdk/openjdk8
RUN mkdir /home/app
COPY --from=MAVEN_BUILD /temperature-sensor/target/temperature-sensor-1.0-SNAPSHOT-jar-with-dependencies.jar /home/app
CMD java -jar /home/app/temperature-sensor-1.0-SNAPSHOT-jar-with-dependencies.jar