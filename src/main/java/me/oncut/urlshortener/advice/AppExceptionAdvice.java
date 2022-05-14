package me.oncut.urlshortener.advice;

import me.oncut.urlshortener.exception.ApiKeyDoesntExistException;
import me.oncut.urlshortener.exception.ApiKeyIsNotValid;
import me.oncut.urlshortener.exception.EmailExistsException;
import me.oncut.urlshortener.exception.LongUrlNotSpecifiedException;
import me.oncut.urlshortener.exception.ShortUrlAlreadyExistsException;
import me.oncut.urlshortener.exception.UrlNotFoundException;
import me.oncut.urlshortener.exception.UserDoesntExistException;
import me.oncut.urlshortener.exception.UserDoesntHaveApiKey;
import me.oncut.urlshortener.model.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppExceptionAdvice {

    @ExceptionHandler({UrlNotFoundException.class, ApiKeyDoesntExistException.class})
    public ResponseEntity<ErrorMessage> notFoundAdvice(final Exception exception) {
        final ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler({EmailExistsException.class,
            LongUrlNotSpecifiedException.class,
            UserDoesntExistException.class,
            UserDoesntHaveApiKey.class,
            ShortUrlAlreadyExistsException.class,
            ApiKeyIsNotValid.class})
    public ResponseEntity<ErrorMessage> badRequestAdvice(final Exception exception) {
        final ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

}
