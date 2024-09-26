package com.duyhk.bewebbanhang.exception;

import com.duyhk.bewebbanhang.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BatLoiException {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseDTO<Void> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        return ResponseDTO.<Void>builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(e.getFieldError().getDefaultMessage())
                .build();
    }

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ResponseDTO<Void> handleRuntimeException(RuntimeException e) {
        return ResponseDTO.<Void>builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage())
                .build();
    }
}
