package com.maria.paginas_marcadas.security;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import com.maria.paginas_marcadas.repository.UsuarioRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtTokenProvider {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Value("${secret-key}")
	private String key;
	
	private Key getKey() {
	    return Keys.hmacShaKeyFor(key.getBytes());
	}
	
	public String gerarToken(UserDetails usuarioLogado) {
	    return Jwts.builder()
            .claim("authorities", usuarioLogado.getAuthorities().stream().map(Object::toString).toList())
            .setSubject(usuarioLogado.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24h
            .signWith(getKey())
            .compact();
	}
	
	public String gerarTokenAcessoSalvo(UserDetails usuarioLogado) {
        Duration duration = Duration.ofDays(365); //1 ANO
	    return Jwts.builder()
            .claim("authorities", usuarioLogado.getAuthorities().stream().map(Object::toString).toList())
            .setSubject(usuarioLogado.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(Date.from(Instant.now().plus(duration)))
            .signWith(getKey())
            .compact();
	}
	
	public <T> T extrairPorClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extrairTodosClaims(token);
        return claimsResolver.apply(claims);
    }
	
	 private Claims extrairTodosClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
	 
	 public List<String> extrairAuthorities(String token) {
		 return extrairPorClaim(token, claims -> {
			List<?> lista = (List<?>) claims.get("authorities");
	        if (lista == null) return List.of();
	        return lista.stream().map(Object::toString).toList();
	    });
	 }
	 
    public boolean tokenExpirado(String token) {
        Date expiracao = extrairPorClaim(token, Claims::getExpiration);
        return expiracao.before(new Date());
    }
	    
    public String extrairEmail(String token) {
        return extrairPorClaim(token, Claims::getSubject);
    }
    
    public boolean tokenValido(String token) {
        String email = extrairEmail(token);
        if(usuarioRepository.existsByEmail(email) && !tokenExpirado(token)) {
        	return true;
        } else { return false; }
    }
}
