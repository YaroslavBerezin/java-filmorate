package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final ValidationException e) {
        return new ErrorResponse("Validation error", e.getMessage());
    }

    @ExceptionHandler({IncorrectIdException.class, NullPointerException.class, IncorrectArgumentException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleIncorrectIdException(final Exception e) {
        return new ErrorResponse("Not Found error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        return new ErrorResponse("Internal Server error", e.getMessage());
    }
}
