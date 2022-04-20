package codes.karlo.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ShortUrlAlreadyExistsException extends CommonException {

    public ShortUrlAlreadyExistsException(final String message) {
        super(message);
    }

    public ShortUrlAlreadyExistsException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ShortUrlAlreadyExistsException(final Throwable cause) {
        super(cause);
    }
}
