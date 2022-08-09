FROM adoptopenjdk/openjdk8
RUN mkdir /home/app
COPY target/temperature-service-1.0-SNAPSHOT-jar-with-dependencies.jar /home/app
CMD java -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.port=10002 -Dcom.sun.management.jmxremote.rmi.port=10002 -Djava.rmi.server.hostname=localhost -Dcom.sun.management.jmxremote.local.only=false -jar /home/app/temperature-service-1.0-SNAPSHOT-jar-with-dependencies.jar
EXPOSE 10002

