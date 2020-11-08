package ergasia2;

import java.io.*;
import java.net.Socket;

public class ClientTask implements Runnable {

    private final Socket socket;
    private PrintWriter pw;
    private BufferedReader br;

    public ClientTask(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        String path;
        try {
            pw = new PrintWriter(socket.getOutputStream(), true);
            br = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Enter file path: ");
            path = br.readLine();
            String fileNames = "Signin " + FileName.getFileNames(path);

            pw.println(fileNames);

            while(true) {
                String keywords = "Search " + br.readLine();

                pw.println(keywords);
                pw.flush();
            }
        } catch (IOException e)  {
            e.printStackTrace();
        }




//
//
//        try {
//            InputStream inputStream = socket.getInputStream();
//            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
//
//            while (true) {
//                String message = br.readLine();
//                System.out.println(message);
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
}
