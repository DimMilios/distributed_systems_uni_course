package tcpTest;

import java.util.Scanner;

public class Main {

    /**
     * The entry point of the application.
     *
     * @param args
     */
    @SuppressWarnings("resource")
    public static void main(String[] args) {
        final Scanner in = new Scanner(System.in);

        // Enter the port to listen
        System.out.print("Port: ");
        final int port = in.nextInt();
        in.nextLine();

        // Start the server
        Server server = new ServerImpl(port);
        server.start();
    }
}
