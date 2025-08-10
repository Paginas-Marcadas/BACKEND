package com.maria.paginas_marcadas.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import com.maria.paginas_marcadas.security.userDetails.UserDetailService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter{

	@Autowired
	private JwtTokenProvider Jwtservice;
	
	@Autowired
	private UserDetailService userService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		
		/* 1. PEGA DENTRO DO HEADER A AUTHORIZATION, QUE CONSISTE NUM HEADER COM 
		 * UM SUFIXO BEARER NO TOKEN DE ACESSO. 
		 */
		String headerToken = request.getHeader("Authorization");
		
		/* 2. VERIFICA SE COMEÇA POR BEARER, JÁ QUE POR PADRÃO A CRIAÇÃO DO O 
		 * TOKEN JWT COMEÇA COM O BEARER. CASO NÃO TENHA NADA OU NÃO TENHA SIDO
		 * PASSADO UM JWT ELE CONTINUA A APLICAÇÃO, PRA REQUISIÇÕES QUE NÃO
		 * PRECISAM DE ACESSO.*/
        if (headerToken == null || !headerToken.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); 
            return;
        }
        
        /* 3. PEGA APENAS O TOKEN, PULANDO AS 7 PRIMEIRAS LETRAS, QUE CONSISTEM
         * NO "BEARER ", FICANDO APENAS O TOKEN MESMO.*/
        String token = headerToken.substring(7);
        
        // 4. EXTRAI O EMAIL PELO TOKEN
        String emailUsuario = Jwtservice.extrairEmail(token);
        
        // 5. VERIFICAR SE O TOKEN É VÁLIDO
        if (Jwtservice.tokenValido(token)) {
        	
        	// VERIFICA SE O TOKEN NÃO ESTÁ EXPIRADO
        	if(!Jwtservice.tokenExpirado(token)){
        		
        		//ESTOU CRIANDO UM USERDETAIL A PARTIR DO E-MAIL
        		UserDetails usuario = userService.loadUserByUsername(emailUsuario);
        		
        		/*ESTE É UM OBJETO QUE JÁ ESTÁ NUM FORMATO QUE O SPRING SECURITY VAI 
        		 * PODER USAR NO WEBSECURIRYCONFIG.*/
        		UsernamePasswordAuthenticationToken tokenAutorizado = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
        		
        		/*OS DADOS DEFINIDOS DENTRO DESTA SERÃO AUTOMATICAMENTE USADOS NO
        		 * NA APLICAÇÃO DO WEBSECURIRYCONFIG, MAS ELE SÓ RECEBE UM OBJETO DO
        		 * TIPO UsernamePasswordAuthenticationToken.*/
        		SecurityContextHolder.getContext().setAuthentication(tokenAutorizado);
        		
        	} else{ throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token expirado!");}
        	
        } else { throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido!");}
        
        filterChain.doFilter(request, response);
		}
	}
