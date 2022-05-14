package me.oncut.urlshortener.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserNotLoggedIn extends CommonException {
    public UserNotLoggedIn(final String message) {
        super(message);
    }
}
