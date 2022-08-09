FROM adoptopenjdk/openjdk8
RUN mkdir /home/app
COPY target/temperature-service-1.0-SNAPSHOT-jar-with-dependencies.jar /home/app
CMD java -jar /home/app/temperature-service-1.0-SNAPSHOT-jar-with-dependencies.jar