package ergasia2;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;

// Send Signin/Signout/Search requests
// Receive replies
public class Client {

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.out.println("Error: you forgot <server IP>");
			return;
		}

		String serverIP = args[0];
		Socket socket = null;

//		String path = "C:\\Users\\dimit\\Documents\\ReactProjects\\pms\\src\\components";

		try {
			socket = new Socket(serverIP, 4242);

			(new Thread(new ClientTask(socket))).start();

			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			String message;
			while ((message = br.readLine()) != null) {
				System.out.println(message);
			}

		} catch(ConnectException ex) {
			System.out.println("Server is currently offline, try again later!");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (socket != null && !socket.isClosed()) {
				try {
					socket.close();
					System.exit(0);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}

