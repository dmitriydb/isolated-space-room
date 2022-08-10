package ru.shanalotte.room.rest;

import lombok.RequiredArgsConstructor;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.facets.fork.TkRegex;
import org.takes.rs.RsHtml;
import org.takes.rs.RsJson;
import ru.shanalotte.room.LastTemperatureStats;

@RequiredArgsConstructor
public class TemperaturePage implements Take {
  private final LastTemperatureStats lastTemperatureStats;
  @Override
  public Response act(Request request) throws Exception {
    return new RsHtml(String.valueOf(lastTemperatureStats.getTemperature()));
  }
}
