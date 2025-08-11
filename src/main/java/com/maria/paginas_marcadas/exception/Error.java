package com.maria.paginas_marcadas.exception;

import java.sql.Time;
import java.time.LocalDate;

public record Error(
	String titulo,
    String descricao,
    int status,
    Time horario,
    LocalDate data
) {}
