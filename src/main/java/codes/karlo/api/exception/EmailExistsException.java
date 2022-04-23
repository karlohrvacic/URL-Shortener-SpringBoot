package codes.karlo.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailExistsException extends CommonException {

    public EmailExistsException(final String message) {
        super(message);
    }

    public EmailExistsException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public EmailExistsException(final Throwable cause) {
        super(cause);
    }

}
