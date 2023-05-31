package com.devinhouse.labsky.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(PassageiroNaoEncontradoException.class)
    public ResponseEntity<Object> handleRegistroNaoEncontradoException(PassageiroNaoEncontradoException e) {
        Map<String, String> response = new HashMap<>();
        response.put("erro", "Passageiro com cpf informado não encontrado(a)");
        response.put("cpfInformado", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
