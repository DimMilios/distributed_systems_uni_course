package ergasia2;

import java.io.*;
import java.net.Socket;

public class ClientTask implements Runnable {

    private final Socket socket;

    public ClientTask(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Enter file path: ");
            String path = in.readLine();
            String fileNames = "Signin " + FileName.getFileNames(path);

            out.println(fileNames);
            out.flush();

            String input;
            while((input = in.readLine()) != null) {
                out.println(input);
                out.flush();

                if (input.contains("Signout")) {
                    break;
                }
            }
        } catch (IOException e)  {
            e.printStackTrace();
        }

    }
}
