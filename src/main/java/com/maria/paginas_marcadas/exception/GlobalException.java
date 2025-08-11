package com.maria.paginas_marcadas.exception;

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
}
