package codes.karlo.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UrlNotFoundException extends CommonException {

    public UrlNotFoundException(final String message) {
        super(message);
    }

    public UrlNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UrlNotFoundException(final Throwable cause) {
        super(cause);
    }

}
