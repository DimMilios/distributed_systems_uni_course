package ergasia2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static ServerSocket serverSocket;
    // key: Client ip+port e.g /127.0.0.1:6000
    // value: list of file names
    private static Map<String, List<String>> clientFiles = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        try {
            serverSocket = new ServerSocket(4242);

            while (true) {
                Socket clientSocket = serverSocket.accept();

                (new Thread(new ServerTask(clientSocket, clientFiles))).start();
            }
        } catch (SocketException e) {
          String client = e.getMessage().substring("/".length());
          clientFiles.remove(client);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        }

    }

}
