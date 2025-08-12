package com.maria.paginas_marcadas.dto;

import java.time.LocalDate;

import com.maria.paginas_marcadas.entity.enums.Genero;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequestDto {

	@NotBlank(message = "O nome é obrigatório.")
    @Size(min = 10, max = 100, message = "O nome deve conter entre 10 e 100 caracteres.")
	private String nome;
	
	@NotNull(message = "A data de nascimento é obrigatória.")
    @Past(message = "A data de nascimento não pode ser futura.")
	private LocalDate dataNascimento;
	
	@NotNull(message = "O genêro é obrigatório.")
	private Genero genero;
	
	@NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "Formato de e-mail inválido.")
	private String email;
	
	@NotBlank(message = "A senha é obrigatória.")
    @Pattern (regexp = "^(?=.*[A-Z])(?=.*[a-zA-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{8,}$", message = "A senha deve ter no mínimo 8 caracteres, incluindo letra maiúscula, letra minúscula, número e caractere especial.")
    @Size(min = 8, max = 200, message = "A senha deve conter no mínimo 8 caracteres.")
	private String senha;
}
