package me.oncut.urlshortener.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ShortUrlAlreadyExistsException extends CommonException {
    public ShortUrlAlreadyExistsException(final String message) {
        super(message);
    }
}
