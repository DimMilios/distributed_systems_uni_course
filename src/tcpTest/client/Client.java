package tcpTest.client;

public interface Client {

    void connect();

    void disconnect();

    boolean isConnected();

    void send(final String message);
}
