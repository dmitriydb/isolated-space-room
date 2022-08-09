FROM adoptopenjdk/openjdk8
RUN mkdir /home/app
COPY target/temperature-sensor-1.0-SNAPSHOT-jar-with-dependencies.jar /home/app
CMD java -jar /home/app/temperature-sensor-1.0-SNAPSHOT-jar-with-dependencies.jar