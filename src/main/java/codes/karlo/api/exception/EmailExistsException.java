package codes.karlo.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailExistsException extends CommonException {
    public EmailExistsException(final String message) {
        super(message);
    }
}
