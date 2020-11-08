package ergasia2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.ServerSocket;

public class Server
{
    
    public static void main(String args[]) {
        ServerSocket serverSocket = null;
        PrintWriter pw = null;

        try {
            serverSocket = new ServerSocket(4242);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        while (true) {
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();

                pw = new PrintWriter(clientSocket.getOutputStream(), true);
                String data = "OK";
                pw.println(data);

                new ServerThread(clientSocket).start();
            } catch (IOException e) {e.printStackTrace();}            
        }
    }
}
