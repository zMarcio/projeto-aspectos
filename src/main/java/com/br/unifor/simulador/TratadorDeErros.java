package com.br.unifor.simulador;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Qualquer IllegalArgumentException lançada em qualquer módulo vira
// HTTP 400 com o corpo {"erro": "mensagem"}. A página mostra essa mensagem.
@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> erro(IllegalArgumentException e){
        return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
    }
}
