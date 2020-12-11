package ergasia3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientTask implements Runnable {
    private Socket socket;

    public ClientTask(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            String user = "";

            while (user.equals("")) {
                System.out.print("Enter your username: ");
                user = in.readLine();
            }

            while(true) {
                System.out.print("Menu\n1.Read emails\n2.New email\nEnter your choice: ");
                String choice = in.readLine();

                if (!isNumeric(choice)) {
                    System.out.println("Provide a number");
                    continue;
                }

                switch (Integer.parseInt(choice)) {
                    case 1:
                        System.out.println("Read emails");
                        out.println("mail");
                        out.flush();
                        out.println(user);
                        out.flush();
                        break;
                    case 2:
                        System.out.println("New email");
                        System.out.print("Enter email recipient: ");
                        String recipient = in.readLine();
                        System.out.print("Enter text: ");
                        String text = in.readLine();
                        out.println("client: mail " + recipient + " from-" + user + " `" + text + "`");
                        out.flush();
                        break;
                    default:
                        System.out.println("Wrong input");
                }
            }

        } catch (IOException e)  {
            e.printStackTrace();
        }
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
