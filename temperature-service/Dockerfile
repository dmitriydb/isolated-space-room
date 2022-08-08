
FROM adoptopenjdk/openjdk8
RUN mkdir /home/app
COPY target/temperature-service-1.0-SNAPSHOT-jar-with-dependencies.jar /home/app
ENV T_SERVER_PORT=10000
CMD java -jar /home/app/temperature-service-1.0-SNAPSHOT-jar-with-dependencies.jar
EXPOSE 10000


