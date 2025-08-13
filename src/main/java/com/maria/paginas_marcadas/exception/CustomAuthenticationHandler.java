package com.maria.paginas_marcadas.exception;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationHandler implements AuthenticationEntryPoint {

	private final String titulo;
    private final String descricao;
    private final HttpStatus status;
    private final LocalDateTime data;

    public CustomAuthenticationHandler() {
        this.titulo = "Acesso Negado";
        this.descricao = "Você precisa estar logado para acessar este recurso.";
        this.status = HttpStatus.UNAUTHORIZED;
        this.data = LocalDateTime.now();
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        Error erro = new Error(
            titulo,
            descricao,
            status.value(),
            data.toString()
        );

        response.setContentType("application/json");
        response.setStatus(status.value());
        response.getWriter().write(new ObjectMapper().writeValueAsString(erro));
    }
}
