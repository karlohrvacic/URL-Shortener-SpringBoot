package codes.karlo.api.exception;

public class ApiKeyIsNotValid extends CommonException {

    public ApiKeyIsNotValid(final String message) {
        super(message);
    }

    public ApiKeyIsNotValid(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ApiKeyIsNotValid(final Throwable cause) {
        super(cause);
    }
}
