package codes.karlo.api.exception;

public class ApiKeyDoesntExist extends Exception {
    public ApiKeyDoesntExist() {
        super();
    }

    public ApiKeyDoesntExist(String message) {
        super(message);
    }

    public ApiKeyDoesntExist(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiKeyDoesntExist(Throwable cause) {
        super(cause);
    }

    protected ApiKeyDoesntExist(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
