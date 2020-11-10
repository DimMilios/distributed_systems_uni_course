package tcpTest;

public interface Client {

	public interface OnDisconnectListener {
		void onDisconnect();
	}

	void disconnect();

	String getName();

	void send(final Client sender, final String message);

	String receive();

	void setOnDisconnectListener(final OnDisconnectListener listener);
}
