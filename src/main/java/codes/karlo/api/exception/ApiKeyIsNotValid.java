package codes.karlo.api.exception;

public class ApiKeyIsNotValid extends CommonException {

    public ApiKeyIsNotValid() {
    }

    public ApiKeyIsNotValid(String message) {
        super(message);
    }

    public ApiKeyIsNotValid(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiKeyIsNotValid(Throwable cause) {
        super(cause);
    }
}
