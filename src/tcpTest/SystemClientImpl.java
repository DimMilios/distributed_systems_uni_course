package tcpTest;

public class SystemClientImpl implements Client {

	private static final String NAME = "system";

	public SystemClientImpl() {
	}

	@Override
	public void disconnect() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void send(final Client sender, final String message) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String receive() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setOnDisconnectListener(OnDisconnectListener listener) {
		throw new UnsupportedOperationException();
	}
}
