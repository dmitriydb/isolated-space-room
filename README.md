##Space room simulator
A super simple real-time decision system built with:
- Apache Kafka
- React
- Spring Boot
- Docker.

##Trivia
This app simulates the fictional situation ongoing on the space orbit when the temperature outside the ship is changing rapidly (many times per second) and ship subsystems should decide and also predict when to seal the doors and windows to prevent crew from the heat (or freezing) damage.

The system also should be durable and reliable and maintain safe state of the ship's crew when sensors are down or not responding yet.

Or something like that.

##How to run the project locally

`git clone https://github.com/dmitriydb/isolated-space-room`

`cd isolated-space-room`

`docker compose up`

## React app

**Description**

Prototype to demostrate what this project is about.

Shows realtime information about room current state (current temperature, temperature change speed, lock state etc.)

This app's design is totally copycatting the [Elite game](https://en.wikipedia.org/wiki/Elite_(video_game)) screen.

**Notable endpoints**

|Endpoint | Endpoint description|
|--------|-------|
|http://localhost:10033| React app running inside the Docker container

**Demo**

![](https://files.catbox.moe/6gqn9f.gif)


##Services
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

**Notable endpoints**

|Endpoint | Endpoint description|
|--------|-------|
|http://localhost:10005/stats| Grab json representation of the room current state

**JMX configuration**

Connect with `jconsole localhost:10002`

|Object Name   |Operation|Description |
|--------|-----|--------|
|ru.shanalotte.room:room=connectionmonitor|lastUpdateTime()|Stores the last update timestamp. The room guard service is automatically closes the room after small timeout when messages from sensors are stopping to flow|
|ru.shanalotte.room:room=lasttemperature|getTemperature()|Get last read temperature from sensors|
|ru.shanalotte.room:room=lasttemperature|getTemperature()|Get last read temperature from sensors|
|ru.shanalotte.room:room=lasttemperature|getChangeSpeed()|Get last read temperature change speed from sensors|
|ru.shanalotte.room:room=lasttemperature|getVector()|Get last read temperature vector from sensors|
|ru.shanalotte.room:room=room|state()|Know if room is currently `closed` or `open`|
