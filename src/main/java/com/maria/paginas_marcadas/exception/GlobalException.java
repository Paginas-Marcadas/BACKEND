package com.maria.paginas_marcadas.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {

	@ExceptionHandler(UserAuthenticationException.class)
    public ResponseEntity<Error> handleUserAuthentication(UserAuthenticationException ex) {
		Error erro = new Error(
            ex.getTitulo(),
            ex.getDescricao(),
            ex.getStatus().value(),
            ex.getHorario(),
            ex.getData()
        );
        return ResponseEntity.status(ex.getStatus()).body(erro);
    }
	
	@ExceptionHandler(CustomGenericException.class)
    public ResponseEntity<Map<String, String>> handleCustomException(CustomGenericException ex) {
        Map<String, String> resposta = new HashMap<>();
        resposta.put("erro", ex.getMensagem());
        return ResponseEntity.status(ex.getStatus()).body(resposta);
    }
}
