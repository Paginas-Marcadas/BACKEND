package com.maria.paginas_marcadas.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomGenericException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	private String mensagem;
	private HttpStatus status;
}
