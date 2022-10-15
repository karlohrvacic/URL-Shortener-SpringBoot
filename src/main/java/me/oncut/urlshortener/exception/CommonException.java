package me.oncut.urlshortener.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class CommonException extends RuntimeException {

    protected CommonException(final String message) {
        super(message);
    }

}
