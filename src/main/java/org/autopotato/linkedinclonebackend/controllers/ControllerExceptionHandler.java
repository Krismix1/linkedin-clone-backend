package org.autopotato.linkedinclonebackend.controllers;

import java.util.HashMap;
import java.util.Map;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    @Value
    public static class ExceptionResponseDTO<T> {
        String message;
        T details;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleTypeMismatchException(
        MethodArgumentNotValidException e
    ) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        for (ObjectError error : e.getBindingResult().getGlobalErrors()) {
            errors.put(error.getObjectName(), error.getDefaultMessage());
        }

        return ResponseEntity
            .badRequest()
            .body(new ExceptionResponseDTO<>("Validation error", errors));
    }
}
