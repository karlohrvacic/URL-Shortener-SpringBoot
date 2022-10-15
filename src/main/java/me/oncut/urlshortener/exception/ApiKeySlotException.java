package me.oncut.urlshortener.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ApiKeySlotException extends CommonException {

    public ApiKeySlotException(final String message) {
        super(message);
    }

}
