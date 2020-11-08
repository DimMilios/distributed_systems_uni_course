package ergasia1;

import java.net.InetAddress;

public class ClientData {

    private String username;
    private InetAddress ipAddress;
    private int portNumber;

    public ClientData(String username, InetAddress ipAddress, int portNumber) {
        this.username = username;
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
    }

    public ClientData(InetAddress ipAddress, int portNumber) {
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    @Override
    public String toString() {
        return "username='" + username + '\'' +
                ",\nipAddress=" + ipAddress +
                ",\nportNumber=" + portNumber +
                '}';
    }
}
