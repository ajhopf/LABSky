package com.devinhouse.labsky.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
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

    @ExceptionHandler(AssentoNaoEncontradoException.class)
    public ResponseEntity<Object> handleAssentoNaoEncontradoException(AssentoNaoEncontradoException e) {
        Map<String, String> response = new HashMap<>();
        response.put("erro", "Assento não encontrado");
        response.put("assentoInformado", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(AssentoReservadoException.class)
    public ResponseEntity<Object> handleAssentoReservadoException(AssentoReservadoException e) {
        Map<String, String> response = new HashMap<>();
        response.put("erro", "Assento já está reservado");
        response.put("assentoInformado", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(MenorEmFileiraDeEmergenciaException.class)
    public ResponseEntity<Object> handleMenorEmFileiraDeEmergenciaException(MenorEmFileiraDeEmergenciaException e) {
        Map<String, String> response = new HashMap<>();
        response.put("erro", "Passageiro menor de idade em assento de emergência.");
        response.put("cpfPassageiro", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(BagagensNaoDespachadasEmFileiraDeEmergenciaException.class)
    public ResponseEntity<Object> handleBagagensNaoDespachadasEmFileiraDeEmergenciaException(BagagensNaoDespachadasEmFileiraDeEmergenciaException e) {
        Map<String, String> response = new HashMap<>();
        response.put("erro", "Assento escolhido está em uma fileira de emergência. Bagagens devem ser despachadas.");
        response.put("assentoInformado", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> badRequest(MethodArgumentNotValidException e) {
        List<FieldError> erros = e.getFieldErrors();
        Map<String, String> response = new HashMap<>();

        for (FieldError erro : erros) {
            response.put(erro.getField(), erro.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        Map<String, String> response = new HashMap<>();

        response.put("erro", e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
