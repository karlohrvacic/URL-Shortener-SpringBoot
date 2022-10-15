package me.oncut.urlshortener.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class MailingException extends CommonException {

    public MailingException(final String message) {
        super(message);
    }

}
