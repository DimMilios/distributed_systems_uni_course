package ergasia1;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Error: you forgot <server IP>");
            return;
        }

        InetAddress serverAddress = null;
        int serverPort = 4242;
        DatagramSocket socket = null;

        try {
            serverAddress = InetAddress.getByName(args[0]);
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }

        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }


        ClientData clientData = new ClientData("", serverAddress, serverPort);
        new Thread(new ClientTask(socket, clientData)).start();

        while (true) {
          try {
              String data = NetworkTools.receiveData(socket);
              int colonPos = data.indexOf(":/");
              System.out.println(data.substring(0, colonPos));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
