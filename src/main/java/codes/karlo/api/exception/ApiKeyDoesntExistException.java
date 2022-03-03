package codes.karlo.api.exception;

public class ApiKeyDoesntExistException extends Exception {
    public ApiKeyDoesntExistException() {
        super();
    }

    public ApiKeyDoesntExistException(String message) {
        super(message);
    }

    public ApiKeyDoesntExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiKeyDoesntExistException(Throwable cause) {
        super(cause);
    }

    protected ApiKeyDoesntExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
