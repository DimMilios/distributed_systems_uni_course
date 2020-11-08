package ergasia2;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ServerThread extends Thread {
    private Socket s;

    // key: Client ip+port e.g /127.0.0.1:6000
    // value: list of file names on given path
    private Map<String, List<String>> clientFiles = new HashMap<>();

    public ServerThread(Socket s) {
        this.s = s;
    }

    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter pw = new PrintWriter(s.getOutputStream(), true);

            while (s.isConnected()) {
                String query = br.readLine();
                System.out.println(query);

                if (query.contains("Search")) {
                    String message = query.substring("Search ".length());
                    createClientMap(message);

                    pw.println(clientFiles);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createClientMap(String message) {
        List<String> keywords = Arrays.asList(message.split(","));
        System.out.println(keywords);

        String ipAddress = this.s.getInetAddress().toString();
        int port = this.s.getPort();

        String client = ipAddress + ":" + port;

        clientFiles.put(client, keywords);
    }

}
