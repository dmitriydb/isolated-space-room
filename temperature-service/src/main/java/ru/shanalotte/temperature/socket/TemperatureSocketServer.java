package ru.shanalotte.temperature.socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.shanalotte.temperature.generator.TemperatureGenerator;
import ru.shanalotte.temperature.generator.TemperatureState;
import ru.shanalotte.temperature.generator.TemperatureStateListener;

@RequiredArgsConstructor
public class TemperatureSocketServer implements TemperatureStateListener {

  private final int port;
  private final TemperatureGenerator temperatureGenerator;
  private CopyOnWriteArrayList<PrintWriter> clients = new CopyOnWriteArrayList<>();

  public void start() throws IOException {
    new ServerRunner().start();
  }

  @Override
  public void getNewState(TemperatureState state) {
   for (PrintWriter out : clients) {
      out.write(state.toString() + "\n");
      out.flush();
    }
  }

  @Override
  public List<TemperatureState> recordedEvents() {
    return null;
  }

  private class ServerRunner extends Thread{
    @SneakyThrows
    public void run() {
      @Cleanup ServerSocket serverSocket = new ServerSocket(port);
      while (true) {
        Socket s = serverSocket.accept();
        new ClientWorker(s).start();
      }
    }
  }

  private class ClientWorker extends Thread{
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
