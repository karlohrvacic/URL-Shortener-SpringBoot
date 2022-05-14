package me.oncut.urlshortener.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LongUrlNotSpecifiedException extends CommonException {
    public LongUrlNotSpecifiedException(final String message) {
        super(message);
    }
}
