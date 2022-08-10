package ru.shanalotte.room.soap;

import javax.jws.WebMethod;
import javax.jws.WebService;
import lombok.RequiredArgsConstructor;
import ru.shanalotte.room.LastTemperatureStats;
import ru.shanalotte.room.Room;

@WebService(endpointInterface = "ru.shanalotte.room.soap.SoapWebService")
@RequiredArgsConstructor
public class SoapWebServiceImpl implements SoapWebService{

  private final LastTemperatureStats lastTemperatureStats;
  private final Room room;

  @WebMethod
  @Override
  public int getTemperature() {
    return lastTemperatureStats.getTemperature();
  }

  @WebMethod
  @Override
  public int getChangeSpeed() {
    return lastTemperatureStats.getChangeSpeed();
  }

  @WebMethod
  @Override
  public int getDirection() {
    return lastTemperatureStats.getVector();
  }

  @WebMethod
  @Override
  public String roomState() {
    return room.state();
  }
}
