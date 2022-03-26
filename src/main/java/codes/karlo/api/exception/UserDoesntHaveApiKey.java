package codes.karlo.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserDoesntHaveApiKey extends CommonException {

    public UserDoesntHaveApiKey() {
        super();
    }

    public UserDoesntHaveApiKey(String message) {
        super(message);
    }

    public UserDoesntHaveApiKey(String message, Throwable cause) {
        super(message, cause);
    }

    public UserDoesntHaveApiKey(Throwable cause) {
        super(cause);
    }
}
