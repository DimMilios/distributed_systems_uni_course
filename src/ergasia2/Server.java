package ergasia2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.*;

public class Server
{
    // key: Client ip+port e.g /127.0.0.1:6000
    // value: list of file names on given path
    private static Map<String, List<String>> clientFiles = new HashMap<>();

    public static void main(String[] args) {
//        ServerSocket serverSocket = null;
        PrintWriter pw = null;

        try {
            ServerSocket serverSocket = new ServerSocket(4242);

            while (true) {
                try {

//                pw = new PrintWriter(clientSocket.getOutputStream());
//                String data = "OK";
//                pw.println(data);
//                pw.flush();

                    new ServerThread(serverSocket.accept()).start();
                } catch (IOException e) {
//                    e.printStackTrace();
                    if (e instanceof SocketException) {
                        System.out.println("Socket exception");
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private static class ServerThread extends Thread {
        private Socket socket;

        public ServerThread(Socket s) {
            this.socket = s;
        }

        public void run() {
            System.out.println("Thread: " + Thread.currentThread().getId()
                    + " ip: " + socket.getInetAddress().toString()
                    + " port: " + socket.getPort());
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream());

                while (true) {
                    String query = in.readLine();
                    System.out.println(query);

                    synchronized (clientFiles) {
                        if (query.contains("Signin")) {
                            signInAction(out, query);
                        }

                        if (query.contains("Search")) {
//                            String message = query.substring("Search ".length());

                            searchAction(out, query);
                        }
                    }
                }
            } catch (IOException e) {
//                e.printStackTrace();
                if (e instanceof SocketException) {
                    System.out.println("Socket exception");
                }
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {

                }
            }
        }

        private void signInAction(PrintWriter out, String query) {
            String message = query.substring("Signin ".length());

            Map.Entry<String, List<String>> entry = createClientMapEntry(message);

            System.out.println("Client info: " + entry.getKey() + " files: " + entry.getValue());

            clientFiles.put(entry.getKey(), entry.getValue());

            out.println("Ok");
            out.flush();
        }

        private void searchAction(PrintWriter out, String query) {
            String message = query.substring("Search ".length());
            List<String> keywords = Arrays.asList(message.split(","));

            out.println("Results");
            out.flush();

            for (Map.Entry<String, List<String>> entry : clientFiles.entrySet()) {
                String key = entry.getKey();

                if (entry.getValue().containsAll(keywords)) {
                    for (String s : keywords) {
                        out.println(s + ":" + key);
                        out.flush();
                    }
                }

            }
        }

        private AbstractMap.SimpleEntry<String, List<String>> createClientMapEntry(String message) {
            List<String> keywords = Arrays.asList(message.split(","));
            System.out.println(keywords);

            String ipAddress = this.socket.getInetAddress().toString();
            int port = this.socket.getPort();

            String client = ipAddress + ":" + port;

            return new AbstractMap.SimpleEntry<>(client, keywords);
        }
    }
}
