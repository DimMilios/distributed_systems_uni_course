package ergasia2;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

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

            String path = getFilePath(in);

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

    public String getFilePath(BufferedReader in) throws IOException {
        String path;
        Path file;
        do {
            System.out.print("Enter file path: ");
            path = in.readLine();
            file = new File(path).toPath();
        } while (!Files.isDirectory(file));
        return path;
    }
}
