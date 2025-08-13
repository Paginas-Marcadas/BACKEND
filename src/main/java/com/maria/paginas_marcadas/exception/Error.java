package com.maria.paginas_marcadas.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Error {

	private String titulo;
	private String descricao;
	private int status;
	private String data;
	
}
