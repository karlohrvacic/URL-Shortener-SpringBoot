package codes.karlo.api.advice;

import codes.karlo.api.model.ErrorMessage;
import codes.karlo.api.exception.ApiKeyDoesntExistException;
import codes.karlo.api.exception.EmailExistsException;
import codes.karlo.api.exception.LongUrlNotSpecifiedException;
import codes.karlo.api.exception.ShortUrlAlreadyExistsException;
import codes.karlo.api.exception.UrlNotFoundException;
import codes.karlo.api.exception.UserDoesntExistException;
import codes.karlo.api.exception.UserDoesntHaveApiKey;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppExceptionAdvice {

    @ExceptionHandler({UrlNotFoundException.class,
            ApiKeyDoesntExistException.class})
    public ResponseEntity<ErrorMessage> notFoundAdvice(final Exception exception) {

        final ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler({EmailExistsException.class,
            LongUrlNotSpecifiedException.class,
            UserDoesntExistException.class,
            UserDoesntHaveApiKey.class,
            ShortUrlAlreadyExistsException.class})
    public ResponseEntity<ErrorMessage> badRequestAdvice(final Exception exception) {

        final ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

}
