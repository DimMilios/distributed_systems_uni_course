package ergasia3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Map;

public class ServerTask implements Runnable {
    private Socket socket;
    private Map<String, String> userEmails;
    private BufferedReader in;
    private PrintWriter out;

    public ServerTask(Socket s, Map<String, String> userEmails) {
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
                if (query.equalsIgnoreCase("mail")) {
                    String name = in.readLine();
                    printUserEmails(name);
                } else if (query.contains("client:") && !query.contains("server:")) {
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
                } else if (query.contains("server")) {
                    String subq = query.substring("server: mail".length() + 1);
                    System.out.println(subq);
                    String recipient = subq.substring(0, subq.indexOf(' '));
                    String textContent = subq.substring(subq.indexOf('`') + 1, subq.lastIndexOf('`'));

                    userEmails.put(recipient, textContent);
                    System.out.println(userEmails);
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
            throw new RuntimeException("Client " + ip + "@" + port + " was not found");
        } finally {
            if (p2pClient != null && !p2pClient.isClosed())
                p2pClient.close();
        }

    }

    private void printUserEmails(String name) {
        System.out.println("Print user emails from: " + name);

        out.println("\n=========Your emails=========");
        out.flush();
        for (Map.Entry<String, String> userEmail : userEmails.entrySet()) {
            if (userEmail.getKey().equals(name)) {
                out.println(userEmail.getValue());
                out.flush();
            }
        }
    }

}

