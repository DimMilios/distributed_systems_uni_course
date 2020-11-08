package ergasia1;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.*;

public class Server {

    private static final List<ClientData> activeUsers = new ArrayList<>();

    public static void main(String[] args) {
        DatagramSocket socket = null;

        //Dhmiourgeia socket gia send kai receive
        try {
            socket = new DatagramSocket(4242);
        } catch (SocketException ex) {
            ex.printStackTrace();
        }

        while (true) {
            try {
                String s = NetworkTools.receiveData(socket);
                String[] sp = s.split(":"); //sp[0] = keimeno mhnymatos, sp[1] = IP apostolea san String, sp[2]= port apostolea san String

                String clientAddress = sp[1].replaceAll("/", "");

                ClientData currentClient = new ClientData(InetAddress.getByName(clientAddress), Integer.parseInt(sp[2]));

                if (sp[0].contains("Signin")) {
                    signIn(socket, sp, currentClient);
                } else if (sp[0].contains("Message")) {
                    sendMessage(socket, sp, currentClient);
                } else if (sp[0].contains("Signout")) {
                    signOut(socket, sp, clientAddress);
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void signIn(DatagramSocket socket, String[] sp, ClientData currentClient) throws IOException {
        String username = sp[0].substring("Signin ".length());

        currentClient.setUsername(username);

        // Let active users know that a new user has signed in
        if (activeUsers.size() > 0) {
            for (ClientData cl : activeUsers) {
                if (!clientsHaveSameUsername(currentClient, cl)) {
                    NetworkTools.sendData("\nUser " + username + " has signed in!\n", cl.getIpAddress(), cl.getPortNumber(), socket);
                }
            }

            // Add client to active users and send sign in success message
            for(int i = 0; i < activeUsers.size(); i++) {
                if (!clientsHaveSameUsername(activeUsers.get(i), currentClient)) {
                    sendSuccessSignInMessage(socket, currentClient);
                    break;
                }
            }

        // Active users list is empty
        } else {
            sendSuccessSignInMessage(socket, currentClient);
        }


//        System.out.println(activeUsers);
    }

    private static void sendMessage(DatagramSocket socket, String[] sp, ClientData currentClient) throws IOException {
        String message = sp[0].substring("Message ".length());

        // Find message sender username
        for (ClientData cl : activeUsers) {
            if (cl.getIpAddress().equals(currentClient.getIpAddress())
                    && cl.getPortNumber() == currentClient.getPortNumber()) {
               currentClient.setUsername(cl.getUsername());
               currentClient.setIpAddress(cl.getIpAddress());
               currentClient.setPortNumber(cl.getPortNumber());
            }
        }

        // Show message to the rest of the active users
        if (activeUsers.size() > 0) {
            for (ClientData cl : activeUsers) {
                if (!clientsHaveSameUsername(currentClient, cl)) {
                    NetworkTools.sendData(currentClient.getUsername() + " said: " + message, cl.getIpAddress(), cl.getPortNumber(), socket);
                }
            }
        }
    }

    private static void signOut(DatagramSocket socket, String[] sp, String clientAddress) throws IOException {
        String deletedUsername = "";

        for (int i = 0; i < activeUsers.size(); i++) {
            boolean addressIsEqual = InetAddress.getByName(clientAddress).equals(activeUsers.get(i).getIpAddress());
            boolean portIsEqual = Integer.parseInt(sp[2]) == activeUsers.get(i).getPortNumber();

            if (addressIsEqual && portIsEqual) {
                deletedUsername = activeUsers.get(i).getUsername();
                activeUsers.remove(activeUsers.get(i));
            }
        }

        for (ClientData cl : activeUsers) {
            if (cl.getPortNumber() != Integer.parseInt(sp[2])) {
                NetworkTools.sendData(deletedUsername + " has signed out!", cl.getIpAddress(), cl.getPortNumber(), socket);
            }
        }

        System.out.println(activeUsers);
    }

    private static void sendSuccessSignInMessage(DatagramSocket socket, ClientData currentClient) throws IOException {
        activeUsers.add(currentClient);
        NetworkTools.sendData("You have successfully signed in!", currentClient.getIpAddress(), currentClient.getPortNumber(), socket);
    }

    private static boolean clientsHaveSameUsername(ClientData cl1, ClientData cl2) {
        return cl1.getUsername().equals(cl2.getUsername());
    }
}
