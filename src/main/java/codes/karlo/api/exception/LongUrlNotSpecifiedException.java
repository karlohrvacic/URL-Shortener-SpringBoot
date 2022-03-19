package codes.karlo.api.exception;

public class LongUrlNotSpecifiedException extends CommonException {

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

}
