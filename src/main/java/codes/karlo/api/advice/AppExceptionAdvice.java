package codes.karlo.api.advice;

import codes.karlo.api.entity.ErrorMessage;
import codes.karlo.api.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppExceptionAdvice {

    @ExceptionHandler({UrlNotFoundException.class})
    public ResponseEntity<ErrorMessage> urlNotFoundException(UrlNotFoundException exception) {

        ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler({ApiKeyDoesntExistException.class})
    public ResponseEntity<ErrorMessage> apiKeyDoesntExistException(ApiKeyDoesntExistException exception) {

        ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler({EmailExistsException.class})
    public ResponseEntity<ErrorMessage> emailExistsException(EmailExistsException exception) {

        ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler({LongUrlNotSpecifiedException.class})
    public ResponseEntity<ErrorMessage> longUrlNotSpecifiedException(LongUrlNotSpecifiedException exception) {

        ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler({UserDoesntExistException.class})
    public ResponseEntity<ErrorMessage> userDoesntExistException(UserDoesntExistException exception) {

        ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler({UserDoesntHaveApiKey.class})
    public ResponseEntity<ErrorMessage> generalError(UserDoesntHaveApiKey exception) {

        ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
}
