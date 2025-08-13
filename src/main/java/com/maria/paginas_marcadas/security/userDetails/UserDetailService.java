package com.maria.paginas_marcadas.security.userDetails;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.maria.paginas_marcadas.exception.UserAuthenticationException;
import com.maria.paginas_marcadas.repository.UsuarioRepository;

@Service
public class UserDetailService implements UserDetailsService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	public UserDetails loadUserByUsername(String email) {
	    UserDetail usuarioLogado = new UserDetail(
	        usuarioRepository.findByEmail(email)
	            .orElseThrow(() -> new UserAuthenticationException(
	                "Usuário não encontrado",
	                "E-mail não cadastrado.",
	                HttpStatus.NOT_FOUND,
	                LocalDateTime.now().toString()
	            ))
	    );

	    if (!usuarioLogado.isEnabled()) {
	        throw new UserAuthenticationException(
	            "Conta desativada",
	            "O usuário ainda não verificou seu endereço de e-mail. Por favor, verifique seu e-mail para ativar a conta.",
	            HttpStatus.FORBIDDEN,
	            LocalDateTime.now().toString()
	        );
	    }

	    if (!usuarioLogado.isAccountNonLocked()) {
	        throw new UserAuthenticationException(
	            "Conta bloqueada",
	            "A conta do usuário está temporariamente bloqueada. Entre em contato com o suporte para mais informações.",
	            HttpStatus.FORBIDDEN,
	            LocalDateTime.now().toString()
	        );
	    }

	    return usuarioLogado;
	}
}
