package com.service.psychologists.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ControllerAdvice
@Log
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ResponseStatusException.class)
    public ResponseEntity<?> handleResponseStatusException(ResponseStatusException ex, ServletWebRequest request) {
        String message = ex.getReason();
        int statusCode = ex.getStatusCode().value();
        HttpStatus httpStatus = HttpStatus.valueOf(statusCode);

        ErrorDetails errorDetails = new ErrorDetails(new Date(), statusCode, httpStatus.getReasonPhrase(), message, request.getRequest().getRequestURI());

        return new ResponseEntity<>(errorDetails, httpStatus);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, ServletWebRequest request) {
        final List<String> errors = new ArrayList<>();

        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getDefaultMessage());
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getDefaultMessage());
        }

        String message = String.join(", ", errors);
        int statusCode = ex.getStatusCode().value();
        HttpStatus httpStatus = HttpStatus.valueOf(statusCode);

        ErrorDetails errorDetails = new ErrorDetails(new Date(), statusCode, httpStatus.getReasonPhrase(), message, request.getRequest().getRequestURI());

        return new ResponseEntity<>(errorDetails, httpStatus);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> handleResponseStatusException(Exception ex, ServletWebRequest request) {
        ex.getCause().printStackTrace();

        String message = "Something went wrong. Please try again later";

        ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), message, request.getRequest().getRequestURI());

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Getter
    @AllArgsConstructor
    public static class ErrorDetails {
        private final Date timestamp;
        private final int status;
        private final String error;
        private final String message;
        private final String path;
    }
}
