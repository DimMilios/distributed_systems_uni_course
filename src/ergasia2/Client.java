package ergasia2;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;

public class Client {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Error: you forgot <server IP>");
            return;
        }

        String serverIP = args[0];
        Socket socket = null;

        try {
            socket = new Socket(serverIP, 4242);

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

