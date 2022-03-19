package codes.karlo.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ShortUrlAlreadyExistsException extends CommonException {

    public ShortUrlAlreadyExistsException() {
    }

    public ShortUrlAlreadyExistsException(String message) {
        super(message);
    }

    public ShortUrlAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShortUrlAlreadyExistsException(Throwable cause) {
        super(cause);
    }
}
