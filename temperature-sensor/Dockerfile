FROM maven:3.6.1-jdk-8-alpine AS DEPS
COPY spaceroom-api/src ./spaceroom-api/src
COPY spaceroom-api/pom.xml ./spaceroom-api
WORKDIR ./spaceroom-api
RUN mvn install
FROM maven:3.6.1-jdk-8-alpine AS MAVEN_BUILD
COPY --from=DEPS /root/.m2 /root/.m2
COPY temperature-sensor/src ./temperature-sensor/src
COPY temperature-sensor/pom.xml ./temperature-sensor
WORKDIR ../temperature-sensor
RUN mvn clean package assembly:single
FROM adoptopenjdk/openjdk8
RUN mkdir /home/app
COPY --from=MAVEN_BUILD /temperature-sensor/target/temperature-sensor-1.0-SNAPSHOT-jar-with-dependencies.jar /home/app
CMD java -jar /home/app/temperature-sensor-1.0-SNAPSHOT-jar-with-dependencies.jar