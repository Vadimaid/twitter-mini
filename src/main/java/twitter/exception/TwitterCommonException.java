package twitter.exception;

public class TwitterCommonException extends RuntimeException {

    public TwitterCommonException() {}

    public TwitterCommonException(String message) {
        super(message);
    }
}
