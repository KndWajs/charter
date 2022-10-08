package pl.rejsykoalicja.charter.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.rejsykoalicja.charter.exceptions.KnownException;

@ControllerAdvice
public class ErrorHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(KnownException.class)
    @ResponseBody
    public KnownException handleKnownException(KnownException e) {
        return e;
    }
}
