package codes.karlo.api.exception;

public class UserDoesntExistException extends Exception {

    public UserDoesntExistException() {
        super();
    }

    public UserDoesntExistException(String message) {
        super(message);
    }

    public UserDoesntExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserDoesntExistException(Throwable cause) {
        super(cause);
    }

    protected UserDoesntExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
