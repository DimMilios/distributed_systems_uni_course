package ergasia2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

public class ServerTask implements Runnable {
    private Socket socket;
    private Map<String, List<String>> clientFiles;


    public ServerTask(Socket s, Map<String, List<String>> clientFiles) {
        this.socket = s;
        this.clientFiles = clientFiles;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            while (true) {
                String query = in.readLine();
                System.out.println(query);

                if (query.contains("Signin")) {
                    signInAction(out, query);
                } else if (query.contains("Search")) {
                    searchAction(out, query);
                } else if (query.contains("Signout")) {
                    signOutAction(out);
                    break;
                }
            }
        } catch (IOException e) {
                e.printStackTrace();
                signOutAction();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {

            }
        }
    }

    private void signInAction(PrintWriter out, String query) {
        String message = query.substring("Signin ".length());

        String client = createClient();
        List<String> fileNames = createFileNamesList(message);

        System.out.println("Client info: " + client + " files: " + fileNames);

        clientFiles.put(client, fileNames);

        out.println("Ok");
        out.flush();
    }

    private void searchAction(PrintWriter out, String query) {
        String message = query.substring("Search ".length());
        List<String> keywords = Arrays.asList(message.split(" "));

        out.println("Results");
        out.flush();

        boolean containsAll;
        for (Map.Entry<String, List<String>> entry : clientFiles.entrySet()) {
            String key = entry.getKey();

            for (String fileName : entry.getValue()) {
                containsAll = containsAll(keywords, fileName);
                if (containsAll) {
                    out.println(fileName + ":" + key);
                    out.flush();
                }
            }
        }
    }

    private boolean containsAll(List<String> keywords, String fileName) {
        int[] values = new int[keywords.size()];
        int i = 0;

        for (String word : keywords) {
            String fileNameLc = fileName.toLowerCase();
            String wordLc = word.toLowerCase();

            values[i++] = fileNameLc.contains(wordLc) ? 1 : -1;
        }

        for (int value : values) {
            if (value == -1)
                return false;
        }

        return true;
    }

    private List<String> createFileNamesList(String message) {
        return Arrays.asList(message.split(","));
    }

    private String createClient() {
        String ipAddress = this.socket.getInetAddress().toString();
        int port = this.socket.getPort();

        return ipAddress + ":" + port;
    }

    private void signOutAction() {
        try {
            removeUserInfoAndCloseSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void signOutAction(PrintWriter out) throws IOException {
        out.println("Ok");
        out.flush();

        removeUserInfoAndCloseSocket();
    }

    private void removeUserInfoAndCloseSocket() throws IOException {
        if (socket != null && !socket.isClosed()) {
            String port = String.valueOf(this.socket.getPort());

            // Using Iterator to avoid ConcurrentModificationException when
            // removing an element while still iterating over the map
            Iterator<Map.Entry<String, List<String>>> it = clientFiles.entrySet().iterator();
            Map.Entry<String, List<String>> entry;

            while (it.hasNext()) {
                entry = it.next();
                if (entry.getKey().contains(port)) {
                    clientFiles.remove(entry.getKey());
                    System.out.println(clientFiles);
                }
            }

            socket.close();
        }
    }
}
