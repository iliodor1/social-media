package ru.eldar.socialmedia.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.eldar.socialmedia.exeption.BadRequestException;
import ru.eldar.socialmedia.exeption.EmailNotUniqueException;
import ru.eldar.socialmedia.exeption.ForbiddenException;
import ru.eldar.socialmedia.exeption.NotFoundException;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {


    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    public ErrorResponseDto handleNotFoundException(NotFoundException e) {
        return createErrorDto(e);
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponseDto handleBadRequestException(BadRequestException e) {
        return createErrorDto(e);
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponseDto handleEmailNotUniqueException(EmailNotUniqueException e) {
        return createErrorDto(e);
    }

    @ExceptionHandler
    @ResponseStatus(FORBIDDEN)
    public ErrorResponseDto handleForbiddenException(ForbiddenException e) {
        return createErrorDto(e);
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponseDto handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        return createErrorDto(e);
    }

    private ErrorResponseDto createErrorDto(Exception e) {
        log.error(e.getMessage());
        return new ErrorResponseDto(e.getMessage());
    }

    @Data
    @AllArgsConstructor
    public static class ErrorResponseDto {
        private String errorMessage;
    }
}
