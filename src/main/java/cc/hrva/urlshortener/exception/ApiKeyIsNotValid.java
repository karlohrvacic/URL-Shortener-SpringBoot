package cc.hrva.urlshortener.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ApiKeyIsNotValid extends CommonException {

    public ApiKeyIsNotValid(final String message) {
        super(message);
    }

}
