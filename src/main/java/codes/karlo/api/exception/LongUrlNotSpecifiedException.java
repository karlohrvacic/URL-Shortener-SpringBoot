package codes.karlo.api.exception;

public class LongUrlNotSpecifiedException extends Exception {

    public LongUrlNotSpecifiedException() {
        super();
    }

    public LongUrlNotSpecifiedException(String message) {
        super(message);
    }

    public LongUrlNotSpecifiedException(String message, Throwable cause) {
        super(message, cause);
    }

    public LongUrlNotSpecifiedException(Throwable cause) {
        super(cause);
    }

    protected LongUrlNotSpecifiedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
