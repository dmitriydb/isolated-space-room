FROM adoptopenjdk/openjdk8
RUN mkdir /home/app
COPY target/room-guard-2.7.2.jar /home/app
CMD java -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.port=10003 -Dcom.sun.management.jmxremote.rmi.port=10003 -Djava.rmi.server.hostname=localhost -Dcom.sun.management.jmxremote.local.only=false -jar /home/app/room-guard-2.7.2.jar
EXPOSE 10005 10003