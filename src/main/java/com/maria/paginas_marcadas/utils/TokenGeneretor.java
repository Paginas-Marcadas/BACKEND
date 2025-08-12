package com.maria.paginas_marcadas.utils;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class TokenGeneretor {

	public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
	
	//ADICIONAR A DATA/HORA QUE O TOKEN FOI GERADO
	public String gerarToken(int tamanho) {
	    String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	    SecureRandom random = new SecureRandom();
	    StringBuilder token = new StringBuilder();
	
	    for (int i = 0; i < tamanho; i++) {
	        int aleatorio = random.nextInt(caracteres.length());
	        token.append(caracteres.charAt(aleatorio));
	    }
	
	    String dataHoraFormatada = LocalDateTime.now().format(FORMATTER);
	    token.append("-").append(dataHoraFormatada);
	    return token.toString();
	}
	
	//FUNÇÃO RESPONSÁVEL POR DETERMINAR SE O TOKEN AINDA É VÁLIDO
	public boolean tokenAindaValido(String token, int minutosDeValidade) {
	    String[] partes = token.split("-");
	    if (partes.length != 2) return false;
	
	    try {LocalDateTime dataToken = LocalDateTime.parse(partes[1], FORMATTER);
	    	return LocalDateTime.now().isBefore(dataToken.plusMinutes(minutosDeValidade));} 
	    catch (Exception e) {return false;}
	}
}
