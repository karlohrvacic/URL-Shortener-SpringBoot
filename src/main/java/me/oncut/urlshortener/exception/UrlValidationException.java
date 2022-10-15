package me.oncut.urlshortener.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UrlValidationException extends CommonException {

    public UrlValidationException(final String message) {
        super(message);
    }

}
