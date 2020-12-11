package ergasia3;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static ServerSocket serverSocket;

    private static Map<String, String> userEmails = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Error: you forgot <server port number>");
            return;
        }

        try {
            int port = Integer.parseInt(args[0]);
            serverSocket = new ServerSocket(port);

            while (true) {
                Socket clientSocket = serverSocket.accept();

                (new Thread(new ServerTask(clientSocket, userEmails))).start();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }  finally {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        }

    }

}

