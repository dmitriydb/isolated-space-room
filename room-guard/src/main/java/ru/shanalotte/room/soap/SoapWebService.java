package ru.shanalotte.room.soap;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface SoapWebService {

  @WebMethod
  int getTemperature();

  @WebMethod
  int getChangeSpeed();

  @WebMethod
  int getDirection();

  @WebMethod
  String roomState();
}
