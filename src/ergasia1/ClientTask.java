package ergasia1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;

public class ClientTask implements Runnable {

    private DatagramSocket socket;
    private ClientData client;

    public ClientTask(DatagramSocket socket, ClientData client) {
        this.socket = socket;
        this.client = client;
    }

    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                if (client.getUsername().equals("")) {
                    System.out.print("Enter your username: ");
                    String username = br.readLine();
                    client.setUsername(username);
                    NetworkTools.sendData("Signin " + username, client.getIpAddress(), client.getPortNumber(), socket);
                } else {
                    String message = br.readLine();
                    if (message.contains("Signout")) {
                        System.out.println("Signing out!");
                        NetworkTools.sendData("Signout", client.getIpAddress(), client.getPortNumber(), socket);
                        System.exit(0);
                    }

                    NetworkTools.sendData("Message " + message, client.getIpAddress(), client.getPortNumber(), socket);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
