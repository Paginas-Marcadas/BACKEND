package com.maria.paginas_marcadas.exception;

import java.sql.Time;
import java.time.LocalDate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import lombok.Getter;

@Getter
public class UserAuthenticationException extends AuthenticationException {

	private static final long serialVersionUID = 1L;
	private String titulo;
	private String descricao;
	private HttpStatus status;
	private Time horario;
	private LocalDate data;
	
	public UserAuthenticationException(String titulo, String descricao, HttpStatus status, Time horario, LocalDate data) {
        super(descricao); 
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = status;
        this.horario = horario;
        this.data = data;
	 }
}
