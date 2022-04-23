package codes.karlo.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LongUrlNotSpecifiedException extends CommonException {

    public LongUrlNotSpecifiedException(final String message) {
        super(message);
    }

    public LongUrlNotSpecifiedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public LongUrlNotSpecifiedException(final Throwable cause) {
        super(cause);
    }

}
