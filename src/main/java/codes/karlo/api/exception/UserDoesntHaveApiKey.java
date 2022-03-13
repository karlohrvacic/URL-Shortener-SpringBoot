package codes.karlo.api.exception;

public class UserDoesntHaveApiKey extends Exception {
    public UserDoesntHaveApiKey() {
        super();
    }

    public UserDoesntHaveApiKey(String message) {
        super(message);
    }

    public UserDoesntHaveApiKey(String message, Throwable cause) {
        super(message, cause);
    }

    public UserDoesntHaveApiKey(Throwable cause) {
        super(cause);
    }
}
