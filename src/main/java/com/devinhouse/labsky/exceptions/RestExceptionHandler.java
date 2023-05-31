package com.devinhouse.labsky.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(ListaVaziaException.class)
    public ResponseEntity<Object> handleListaVaziaException(ListaVaziaException e) {
        return ResponseEntity.noContent().build();
    }
}
