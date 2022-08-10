package ru.shanalotte.room.rest;


import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.takes.http.Exit;
import org.takes.http.FtBasic;
import org.takes.facets.fork.FkRegex;
import org.takes.facets.fork.TkFork;
import ru.shanalotte.room.LastTemperatureStats;
import ru.shanalotte.room.Room;

@RequiredArgsConstructor
public class RestApplication {

  private final LastTemperatureStats lastTemperatureStats;
  private final Room room;

  public void start() throws IOException {
    new FtBasic(
        new TkFork(
            new FkRegex("/room/temperature", new TemperaturePage(lastTemperatureStats))
        ),
        10005
    ).start(Exit.NEVER);
  }
}
