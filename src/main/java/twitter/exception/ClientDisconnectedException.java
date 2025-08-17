package twitter.exception;

public class ClientDisconnectedException extends RuntimeException {

    public ClientDisconnectedException() {}

    public ClientDisconnectedException(String message) {
        super(message);
    }
}
