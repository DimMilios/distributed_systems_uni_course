package ergasia3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServerTask implements Runnable {
    private Socket socket;
    private Map<String, List<String>> userEmails;
    private BufferedReader in;
    private PrintWriter out;

    public ServerTask(Socket s, Map<String, List<String>> userEmails) {
        this.socket = s;
        this.userEmails = userEmails;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            String query;
            while ((query = in.readLine()) != null) {
                try {
                    if (query.equalsIgnoreCase("mail")) {
                        String name = in.readLine();
                        printUserEmails(name);
                    } else if (query.contains("client:") && !query.contains("server:")) {
                        sendMessageClientToClient(query);
                    } else if (query.contains("server")) {
                        sendMessageServerToServer(query);
                    }
                } catch(RuntimeException ex){
                        out.println(ex.getMessage());
                        out.flush();
                        System.out.println(ex.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void printUserEmails(String name) {
        System.out.println("Print user emails for: " + name);

        out.println("\n\n=========Your emails=========");
        out.flush();
        for (Map.Entry<String, List<String>> userEmail : userEmails.entrySet()) {
            if (userEmail.getKey().equals(name)) {
                for (String email : userEmail.getValue()) {
                    out.println(email);
                    out.flush();
                }
            }
        }
    }

    public void sendMessageClientToClient(String query) throws IOException {
        out.println("\nOK");
        out.flush();

        String subq = query.substring("client:".length() + 1);
        System.out.println(subq);
        String recipient = subq.substring(subq.indexOf(' ') + 1, subq.indexOf('@'));
        String ip = subq.substring(subq.indexOf('@') + 1, subq.indexOf(':'));
        String port = subq.substring(subq.indexOf(':') + 1);
        String sender = subq.substring(subq.indexOf('-') + 1, subq.indexOf('`'));
        String textContent = subq.substring(subq.indexOf('`') + 1, subq.lastIndexOf('`'));
        port = port.substring(0, port.indexOf(' '));

        sendMessageToEmailServer(recipient, ip, port, sender, textContent);
    }

    public void sendMessageToEmailServer(String recipient, String ip, String port, String sender, String textContent) throws IOException {
        Socket p2pClient = null;
        try {
            p2pClient = new Socket(ip, Integer.parseInt(port));
            PrintWriter p2pOut = new PrintWriter(p2pClient.getOutputStream());

            p2pOut.println("server: mail " + recipient + " from-" + sender + "`" + textContent + "`");
            p2pOut.flush();

            System.out.println("Client with ip: " + ip + " and port: " + port
                    + " connected");

        } catch (ConnectException e) {
            throw new RuntimeException("Client " + recipient + "@" + ip + ":" + port + " was not found");
        } finally {
            if (p2pClient != null && !p2pClient.isClosed())
                p2pClient.close();
        }
    }

    public void sendMessageServerToServer(String query) {
        String subq = query.substring("server: mail".length() + 1);
        System.out.println(subq);
        String recipient = subq.substring(0, subq.indexOf(' '));
        String textContent = subq.substring(subq.indexOf('`') + 1, subq.lastIndexOf('`'));

        if (userEmails.containsKey(recipient)) {
            userEmails.get(recipient).add(textContent);
        } else {
            ArrayList<String> emails = new ArrayList<>();
            emails.add(textContent);
            userEmails.put(recipient, emails);
        }

        System.out.println(userEmails);
    }
}

