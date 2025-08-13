package com.maria.paginas_marcadas.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
	
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleOtherExceptions(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("erro", "Erro interno do servidor");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
