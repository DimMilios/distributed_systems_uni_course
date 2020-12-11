package ergasia3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Το σκεπτικό είναι ότι ο κάθε server θα κάνει expose 2 ports
 * 1. Η πρώτη πόρτα θα περιμένει για συνδέσεις με clients
 * 2. Η δεύτερη πόρτα θα περιμένει για συνδέσεις με άλλους servers
 */
public class P2PServerTask implements Runnable {
    private int portNumber;
    private ServerSocket serverSocket;
    Map<String, String> userEmails;

    public P2PServerTask(int portNumber, Map<String, String> userEmails) throws IOException {
        this.portNumber = portNumber;
        this.userEmails = userEmails;
        this.serverSocket = new ServerSocket(this.portNumber);
    }

    @Override
    public void run() {
        System.out.println("New P2P server started at port: "
                + serverSocket.getLocalPort());

        try {
            Socket p2pClient = serverSocket.accept();

            BufferedReader in = new BufferedReader(new InputStreamReader(p2pClient.getInputStream()));
            String message = "";
            while ((message = in.readLine()) != null) {
                String name = message.substring(0, ' ');
                String content = message.substring(' ');
                System.out.println(message);

                userEmails.put(name, content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
