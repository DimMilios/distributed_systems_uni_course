package ergasia3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;

public class Client {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Error: you forgot <server IP> and/or port");
            return;
        }

        String serverIP = args[0];
        int serverPort = Integer.parseInt(args[1]);
        Socket socket = null;

        try {
            socket = new Socket(serverIP, serverPort);

            (new Thread(new ClientTask(socket))).start();

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String message;
            while ((message = br.readLine()) != null) {
                System.out.println(message);
            }
        } catch (ConnectException ex) {
            System.out.println("Server is currently offline, try again later!");
        } catch (SocketException e) {
            System.out.println("Server has gone offline, program will now exit");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                try {
                    socket.close();
                    System.exit(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
