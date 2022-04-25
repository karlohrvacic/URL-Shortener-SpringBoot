package codes.karlo.api.exception;

public abstract class CommonException extends RuntimeException {
    protected CommonException(final String message) {
        super(message);
    }
}
