package com.maria.paginas_marcadas.dto;

import java.time.LocalDate;

import com.maria.paginas_marcadas.entity.enums.Genero;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequestDto {

	private String nome;
	private LocalDate dataNascimento;
	private Genero genero;
	private String email;
	private String senha;
}
