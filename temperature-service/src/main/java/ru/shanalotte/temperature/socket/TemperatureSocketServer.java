package ru.shanalotte.temperature.socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Cleanup;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.shanalotte.config.TemperatureSocketServerConfig;
import ru.shanalotte.schemas.TemperatureState;
import ru.shanalotte.temperature.generator.TemperatureGenerator;
import ru.shanalotte.temperature.generator.TemperatureStateListener;

@RequiredArgsConstructor
public class TemperatureSocketServer implements TemperatureStateListener {

  @Getter
  private final int port;
  private final TemperatureGenerator temperatureGenerator;
  private CopyOnWriteArrayList<PrintWriter> clients = new CopyOnWriteArrayList<>();
  private ObjectMapper objectMapper = new ObjectMapper();

  public TemperatureSocketServer(TemperatureGenerator temperatureGenerator) {
    this.temperatureGenerator = temperatureGenerator;
    int portValue = TemperatureSocketServerConfig.DEFAULT_PORT;
    try {
      System.out.println(System.getenv(TemperatureSocketServerConfig.PORT_ENV_VARIABLE));
      portValue = Integer.parseInt(System.getenv(TemperatureSocketServerConfig.PORT_ENV_VARIABLE));
    } catch (Throwable t) {
    }
    port = portValue;
  }

  public void start() throws IOException {
    new ServerRunner().start();
  }

  @SneakyThrows
  @Override
  public void getNewState(TemperatureState state) {
    for (PrintWriter out : clients) {
      String recordJson = objectMapper.writeValueAsString(state.toRecord());
      out.write(recordJson + "\n");
      out.flush();
    }
  }

  @Override
  public List<TemperatureState> recordedEvents() {
    return null;
  }

  private class ServerRunner extends Thread {
    @SneakyThrows
    public void run() {
      @Cleanup ServerSocket serverSocket = new ServerSocket(port);
      while (true) {
        Socket s = serverSocket.accept();
        new ClientWorker(s).start();
      }
    }
  }

  private class ClientWorker extends Thread {
    private Socket s;

    public ClientWorker(Socket s) {
      this.s = s;
    }

    @SneakyThrows
    public void run() {
      PrintWriter out = new PrintWriter(s.getOutputStream());
      clients.add(out);
    }
  }
}
