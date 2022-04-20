package codes.karlo.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LongUrlNotSpecifiedException extends CommonException {


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
