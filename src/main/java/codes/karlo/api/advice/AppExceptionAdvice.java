package codes.karlo.api.advice;

import codes.karlo.api.entity.ErrorMessage;
import codes.karlo.api.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppExceptionAdvice {

    @ExceptionHandler({UrlNotFoundException.class,
            ApiKeyDoesntExistException.class})
    public ResponseEntity<ErrorMessage> notFoundAdvice(Exception exception) {

        ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler({EmailExistsException.class,
            LongUrlNotSpecifiedException.class,
            UserDoesntExistException.class,
            UserDoesntHaveApiKey.class,
            ShortUrlAlreadyExistsException.class})
    public ResponseEntity<ErrorMessage> badRequestAdvice(Exception exception) {

        ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

}
