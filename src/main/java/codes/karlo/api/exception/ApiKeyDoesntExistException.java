package codes.karlo.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ApiKeyDoesntExistException extends CommonException {

    public ApiKeyDoesntExistException() {
    }

    public ApiKeyDoesntExistException(String message) {
        super(message);
    }

    public ApiKeyDoesntExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiKeyDoesntExistException(Throwable cause) {
        super(cause);
    }
}
