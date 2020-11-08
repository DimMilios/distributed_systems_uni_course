package ergasia2;

import java.io.*;
import java.net.Socket;

// Send Signin/Signout/Search requests
// Receive replies
public class Client {

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Error: you forgot <server IP>");
			return;
		}

		String serverIP = args[0];
		Socket socket = null;
//        PrintWriter pw = null;
//		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		try {
			socket = new Socket(serverIP, 4242);
//            pw = new PrintWriter(socket.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}

		(new Thread(new ClientTask(socket))).start();
//		String path = "C:\\Users\\dimit\\Documents\\ReactProjects\\pms\\src\\components";

		try {
			InputStream inputStream = socket.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

			while (true) {
				String message = br.readLine();
				System.out.println(message);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}


//		while (true) {
//            try {
//				String path = br.readLine();
//				String fileNames = "Signin " + FileName.getFileNames(path);
//
//				pw.println(fileNames);
//                pw.flush();
//            } catch (IOException e)  {
//                e.printStackTrace();
//            }
//        }
	}
}

