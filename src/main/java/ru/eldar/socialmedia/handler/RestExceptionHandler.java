package ru.eldar.socialmedia.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.eldar.socialmedia.exeption.BadRequestException;
import ru.eldar.socialmedia.exeption.NotFoundException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {


    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    public ErrorResponseDto handleNotFoundException(NotFoundException e){
        return createErrorDto(e);
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponseDto handleNotFoundException(BadRequestException e){
        return createErrorDto(e);
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponseDto handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        return createErrorDto(e);
    }

    private ErrorResponseDto createErrorDto(Exception e){
        log.error(e.getStackTrace().toString());
        return new ErrorResponseDto(e.getMessage());
    }

    @Data
    @AllArgsConstructor
    public static class ErrorResponseDto{
        private String errorMessage;
    }
}
