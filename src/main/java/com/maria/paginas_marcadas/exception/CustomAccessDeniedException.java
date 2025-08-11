package com.maria.paginas_marcadas.exception;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import lombok.Getter;

@Getter
public class CustomAccessDeniedException extends AccessDeniedException {
 
	private static final long serialVersionUID = 1L;
	
	private String titulo;
    private String descricao;
    private HttpStatus status;
    private Time horario;
    private LocalDate data;
    
    public CustomAccessDeniedException() {
        super("Acesso bloqueado"); 
        this.titulo = "Acesso bloqueado";
        this.descricao = "Você não possui permissão para acessar este recurso.";
        this.status = HttpStatus.FORBIDDEN;
        this.horario = Time.valueOf(LocalTime.now());
        this.data = LocalDate.now();
    }
}
