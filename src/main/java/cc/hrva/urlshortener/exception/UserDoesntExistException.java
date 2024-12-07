package cc.hrva.urlshortener.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserDoesntExistException extends CommonException {

    public UserDoesntExistException(final String message) {
        super(message);
    }

}
