package com.maria.paginas_marcadas.security.userDetails;

import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.maria.paginas_marcadas.entity.Usuario;

public class UserDetail implements UserDetails{

	private static final long serialVersionUID = 1L;
	
	private Usuario usuario;
	
	public UserDetail(Usuario usuario) {
        this.usuario = usuario;
    }
	
	public String getPassword() {
        return usuario.getSenha();
    }
	
	public String getUsername() {
        return usuario.getEmail();
    }
	
	public boolean isEnabled() {
    	return usuario.getValidado();
    }
	
	public boolean isAccountNonLocked() {
		return !usuario.getBloqueado();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	    return Collections.emptyList();
	}
}
