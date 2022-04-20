package codes.karlo.api.exception;

public abstract class CommonException extends RuntimeException {

    protected CommonException(final String message) {
        super(message);
    }

    protected CommonException(final String message, final Throwable cause) {
        super(message, cause);
    }

    protected CommonException(final Throwable cause) {
        super(cause);
    }
}
