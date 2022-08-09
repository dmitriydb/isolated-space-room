##Space room simulator
A super simple real-time decision system built with Apache Kafka, React, Jackson and Docker.

##Trivia
This app simulates the fictional situation on the space orbit when the temperature outside the ship is changing rapidly (many times per second) and ship subsystems should decide and also predict when to seal the doors and windows to prevent crew from the heat (or freezing) damage.

##How to run the project
TBD

##Services

###React app
TBD
### Temperature generator service
Simulates the environment outside the ship and generates random temperature stats every now and then.

Server that notifies about current temperature state runs at TCP port `10000` in current docker configuration. Port value is configured by setting the `roomserver` container environment variable `T_SERVER_SOCKET`. The port value resets to `9999` by default if the variable is not set.

**JMX configuration**

Service can be configured to some degree with JMX (`jconsole localhost:10002`)

|Object Name   |Operation|Description |
|--------|-----|--------|
|ru.shanalotte.temperature:type=temperaturecontrol,name=temperature|setCurrentTemperature|Overwrites current temperature with custom value. Generator still try to reach previous temperature goal so the temperature change vector won't change. 
|ru.shanalotte.temperature:type=temperaturecontrol,name=temperature|getCurrentTemperature|Grab current temperature value

#### Sensor
Services that reads values from the server socket and produces records to the Kafka cluster.

#### Room guard service
Consumes temperature state records from the topic `temperature` and make decision to open/close the door.

**JMX configuration**
Connect with command `jconsole localhost:10002`

|Object Name   |Operation|Description |
|--------|-----|--------|
|ru.shanalotte.room:room=connectionmonitor|lastUpdateTime()|Stores the last update timestamp. The room guard service is automatically closes the room after small timeout when messages from sensors are stopping to flow|
|ru.shanalotte.room:room=lasttemperature|getTemperature()|Get last read temperature from sensors|
|ru.shanalotte.room:room=lasttemperature|getTemperature()|Get last read temperature from sensors|
|ru.shanalotte.room:room=lasttemperature|getChangeSpeed()|Get last read temperature change speed from sensors|
|ru.shanalotte.room:room=lasttemperature|getVector()|Get last read temperature vector from sensors|
|ru.shanalotte.room:room=room|state()|Know if room is currently `closed` or `open`|