package com.maria.paginas_marcadas.exception;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final String titulo;
    private final String descricao;
    private final HttpStatus status;
    private final Time horario;
    private final LocalDate data;

    public CustomAccessDeniedHandler() {
        this.titulo = "Acesso Bloqueado";
        this.descricao = "Você não possui permissão para acessar este recurso.";
        this.status = HttpStatus.FORBIDDEN;
        this.horario = Time.valueOf(LocalTime.now());
        this.data = LocalDate.now();
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, JsonProcessingException, java.io.IOException {

        Error erro = new Error(
            titulo,
            descricao,
            status.value(),
            horario,
            data
        );

        response.setContentType("application/json");
        response.setStatus(status.value());
        response.getWriter().write(new ObjectMapper().writeValueAsString(erro));
    }
}
