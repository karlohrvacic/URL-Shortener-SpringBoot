package me.oncut.urlshortener.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ApiKeyDoesntExistException extends CommonException {

    public ApiKeyDoesntExistException(final String message) {
        super(message);
    }

}
