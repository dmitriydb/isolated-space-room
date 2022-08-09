package ru.shanalotte.temperature.sensor;

public class SensorLauncher {

  public static void main(String[] args) {
    SensorProducer sensorProducer = new SensorProducer();
    Sensor sensor = new Sensor();
    sensor.attachProducer(sensorProducer);
    sensor.startSensing();
  }
}
