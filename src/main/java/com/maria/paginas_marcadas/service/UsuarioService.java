package com.maria.paginas_marcadas.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.maria.paginas_marcadas.dto.UsuarioRequestDto;
import com.maria.paginas_marcadas.dto.UsuarioUpdateDto;
import com.maria.paginas_marcadas.entity.Usuario;
import com.maria.paginas_marcadas.exception.CustomGenericException;
import com.maria.paginas_marcadas.repository.UsuarioRepository;
import com.maria.paginas_marcadas.security.JwtTokenProvider;
import com.maria.paginas_marcadas.security.userDetails.UserDetailService;
import com.maria.paginas_marcadas.utils.RespostaComTokenDeAcesso;
import com.maria.paginas_marcadas.utils.TokenGeneretor;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private CloudinaryService cloudinaryService;
	
	@Autowired
	private TokenGeneretor tokenGeneretor;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private UserDetailService userDetailService;
	
	//POST - CADASTRO DE USUÁRIO
	public String cadastro (UsuarioRequestDto request) {
		Optional<Usuario> usuarioExiste = usuarioRepository.findByEmail(request.getEmail());
		
		if (usuarioExiste.isPresent()) {
			
			//QUANDO O USUÁRIO AINDA TEM QUE VÁLIDAR O E-MAIL
			if(usuarioExiste.get().getValidado().equals(Boolean.FALSE)) {
				String tokenValidacao = tokenGeneretor.gerarToken(4);
				usuarioExiste.get().setTokenValidacao(tokenValidacao);
				usuarioRepository.save(usuarioExiste.get());
				
				String token = tokenValidacao.split("-")[0];
				emailService.sendValidationEmail(usuarioExiste.get().getEmail(), usuarioExiste.get().getNome(), token);
				
				throw new CustomGenericException("Conta já cadastrada, mas sem válidação.",
						HttpStatus.UNPROCESSABLE_ENTITY);
			}
			
			//QUANDO O USUÁRIO ESTÁ COM O ACESSO BLOQUEADO
			if (usuarioExiste.get().getBloqueado().equals(Boolean.TRUE)) {
				throw new CustomGenericException("Usuário se encontra com o acesso ao sistema bloqueado.",
						HttpStatus.LOCKED);
			}
			
			//QUANDO O USUÁRIO JÁ TEM CONTA CADASTRADA
			if ((Boolean.FALSE.equals(usuarioExiste.get().getBloqueado()) || usuarioExiste.get().getBloqueado() == null) 
				&& Boolean.TRUE.equals(usuarioExiste.get().getValidado())) {

				throw new CustomGenericException("Este e-mail já está cadastrado, efetue login.",
						HttpStatus.ACCEPTED);
			}
		}
		
		Usuario novoUsuario = new Usuario();
		
		novoUsuario.setNome(request.getNome());
		novoUsuario.setDataNascimento(request.getDataNascimento());
		novoUsuario.setEmail(request.getEmail());
		novoUsuario.setSenha(encoder.encode(request.getSenha()));
		novoUsuario.setValidado(Boolean.FALSE);
		novoUsuario.setBloqueado(Boolean.FALSE);
		novoUsuario.setDataCadastro(OffsetDateTime.now());
		
		String tokenValidacao =  tokenGeneretor.gerarToken(4);
		novoUsuario.setTokenValidacao(tokenValidacao);
		
		usuarioRepository.save(novoUsuario);
		
		String token = tokenValidacao.split("-")[0];
		emailService.sendValidationEmail(novoUsuario.getEmail(), novoUsuario.getNome(), token);
		
		String resposta = "Cadastro efetuado com Sucesso! Será eviado um e-mail pra validação da conta.";
		
		return resposta;
	}
	
	//VALIDAÇÃO DO E-MAIL
	public String validarEmail(String email, String tokenValidacao) {
	    Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new CustomGenericException(
	            "E-mail sem referência em conta, cadastre-se!",
	            HttpStatus.NOT_FOUND
	        ));

	    //TOKEN EXPIRADO
	    Boolean tokenValido = tokenGeneretor.tokenAindaValido(usuario.getTokenValidacao(), 5);
	    if (tokenValido.equals(Boolean.FALSE)) {
	        String novoTokenValidacao = tokenGeneretor.gerarToken(4);
	        usuario.setTokenValidacao(novoTokenValidacao);
	        usuarioRepository.save(usuario);

	        String token = novoTokenValidacao.split("-")[0];
	        emailService.sendValidationEmail(usuario.getEmail(), usuario.getNome(), token);

	        throw new CustomGenericException(
	            "Token expirado. Um novo foi enviado para seu e-mail.",
	            HttpStatus.GONE
	        );
	    }

	    //TOKEN DIGITADO INCORRETAMENTE
	    String tokenArmazenado = usuario.getTokenValidacao().split("-")[0];
	    if (!tokenArmazenado.equals(tokenValidacao)) {
	        throw new CustomGenericException(
	            "Token inválido. Verifique se digitou corretamente o código enviado por e-mail.",
	            HttpStatus.BAD_REQUEST
	        );
	    }

	    usuario.setValidado(Boolean.TRUE);
	    usuarioRepository.save(usuario);

	    emailService.sendWelcomeEmail(usuario.getEmail(), usuario.getNome());

	    return "E-mail validado com sucesso!";
	}
	
	//VERIFCA SE PODE PEDIR UM NOVO CÓDIGO DE VÁLIDAÇÃO, ENVIA OS SEGUNDOS QUE FALTAM, O CÓDIGO TEM A VÁLIDADE DE 5 MINUTOS
	public long tempoRestanteParaNovoToken(String email) {
	    Usuario usuario = usuarioRepository.findByEmail(email)
	            .orElseThrow(() -> new CustomGenericException("E-mail sem referência em conta, cadastre-se!", HttpStatus.NOT_FOUND));
	    
	    String token = usuario.getTokenValidacao();
	    String[] partes = token.split("-");

	    if (partes.length != 2) {
	        throw new CustomGenericException("Token inválido. Tente reenviar o e-mail de validação.", HttpStatus.BAD_REQUEST);
	    }

	    try {
	    	LocalDateTime dataToken = LocalDateTime.parse(partes[1], TokenGeneretor.FORMATTER);
	        LocalDateTime expiracao = dataToken.plusMinutes(5);
	        LocalDateTime agora = LocalDateTime.now();

	        if (agora.isAfter(expiracao)) {
	            return 0; 
	        }

	        return java.time.Duration.between(agora, expiracao).getSeconds(); 
	    } catch (Exception e) {
	        throw new CustomGenericException("Erro ao verificar token.", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
		
	//ENVIA UM NOVO CÓDIGO DE VÁLIDAÇÃO
	public String reenviarCodigoValidacao(String email) {
	    Usuario usuario = usuarioRepository.findByEmail(email)
	            .orElseThrow(() -> new CustomGenericException("E-mail sem referência em conta, cadastre-se!", HttpStatus.NOT_FOUND));

	    if (Boolean.TRUE.equals(usuario.getValidado())) {
	        throw new CustomGenericException("Usuário já validou o e-mail.",  HttpStatus.ACCEPTED);
	    }

	    String novoTokenValidacao = tokenGeneretor.gerarToken(4);
	    usuario.setTokenValidacao(novoTokenValidacao);
	    usuarioRepository.save(usuario);
	    
	    String token = novoTokenValidacao.split("-")[0];
        emailService.sendValidationEmail(usuario.getEmail(), usuario.getNome(), token);

	    return "Novo código de validação enviado com sucesso!";
	}

	//LOGIN DE USUÁRIO
	public RespostaComTokenDeAcesso login(String email, String senha) {
		Usuario usuario = usuarioRepository.findByEmail(email)
			    .orElseThrow(() -> new CustomGenericException("E-mail sem referência em conta, cadastre-se!", HttpStatus.NOT_FOUND));
		
		//USUÁRIO QUE NÃO VALIDOU O E-MAIL
		if (usuario.getValidado().equals(Boolean.FALSE)) {
			String novoTokenValidacao = tokenGeneretor.gerarToken(4);
	        usuario.setTokenValidacao(novoTokenValidacao);
	        usuarioRepository.save(usuario);

	        String token = novoTokenValidacao.split("-")[0];
	        emailService.sendValidationEmail(usuario.getEmail(), usuario.getNome(), token);
			
			throw new CustomGenericException("Conta já cadastrada, mas inativa. Um novo e-mail de ativação foi enviado.",
					HttpStatus.ACCEPTED);
		}
		
		//USUÁRIO COM O ACESSO BLOQUEADO
		if (usuario.getBloqueado().equals(Boolean.TRUE)) {
			throw new CustomGenericException("Usuário se encontra com o acesso ao sistema bloqueado.",
					HttpStatus.LOCKED);
		}
		
		if (!encoder.matches(senha, usuario.getSenha())) {
			throw new CustomGenericException("Senha incorreta. Verifique seus dados e tente novamente.", HttpStatus.UNAUTHORIZED);
		}
		
		String token = jwtTokenProvider.gerarToken(userDetailService.loadUserByUsername(email));
		usuario.setTokenAcesso(token);
		usuarioRepository.save(usuario);
		
		RespostaComTokenDeAcesso resposta = new RespostaComTokenDeAcesso("Login efetuado com Sucesso!", token);
		return resposta;
	} 
	
	//ADICIONAR OU MUDAR FOTO DE PERFIL
	public String uploadAvatarFile (MultipartFile image) throws IOException {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
		
		String linkAvatar = cloudinaryService.uploadAvatarFile(image, usuario);
		usuario.setAvatar(linkAvatar);
		usuarioRepository.save(usuario);
		
		String response = "Avatar adicionado com sucesso!";
		return response;
	}
	
	//ATUALIZAÇÃO DE UM USUÁRIO
	public String atualizarCadastro(UsuarioUpdateDto update) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuario = usuarioRepository.findByEmail(email)
			    .orElseThrow(() -> new CustomGenericException("Não nenhum usuário com referência a esse e-mail no sistema.", HttpStatus.NOT_FOUND));
		
		if(usuario.getValidado().equals(Boolean.FALSE)) {
			throw new CustomGenericException(usuario.getNome().split(" ")[0]+" não efetuou a validação do e-mail",
					HttpStatus.ACCEPTED);
		}
		
		if (usuario.getBloqueado().equals(Boolean.TRUE)) {
			throw new CustomGenericException(usuario.getNome().split(" ")[0]+" se encontra com o acesso ao sistema bloqueado.",
					HttpStatus.LOCKED);
		}
		
		// NOME
	    if (update.getNome() != null) {
	        if (update.getNome().length() < 10 || update.getNome().length() > 300) {
	            throw new CustomGenericException(
	                    "O nome deve conter entre 10 e 300 caracteres.",
	                    HttpStatus.BAD_REQUEST);
	        }
	        usuario.setNome(update.getNome());
	    }

	    // DATA DE NASCIMENTO
	    if (update.getDataNascimento() != null) {
	        if (update.getDataNascimento().isAfter(LocalDate.now())) {
	            throw new CustomGenericException(
	                    "A data de nascimento não pode ser futura.",
	                    HttpStatus.BAD_REQUEST);
	        }
	        usuario.setDataNascimento(update.getDataNascimento());
	    }

	    // GÊNERO
	    if (update.getGenero() != null) {
	        usuario.setGenero(update.getGenero()); 
	    }

	    // SENHA
	    if (update.getSenha() != null) {
	        if (!update.getSenha().matches("^(?=.*[A-Z])(?=.*[a-zA-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{8,}$")) {
	            throw new CustomGenericException(
	                    "A senha deve ter no mínimo 8 caracteres, incluindo letra maiúscula, letra minúscula, número e caractere especial.",
	                    HttpStatus.BAD_REQUEST);
	        }
	        usuario.setSenha(encoder.encode(update.getSenha()));
	    }

	    usuarioRepository.save(usuario);
	    return "Atualização efetuada com sucesso!";
	}
	
	//VERIFCA SE PODE PEDIR UM NOVO CÓDIGO DE RECUPERAÇÃO, ENVIA OS SEGUNDOS QUE FALTAM, O CÓDIGO TEM A VÁLIDADE DE 5 MINUTOS
	public long tempoRestanteParaNovoTokenRecuperacao (String email) {
	    Usuario usuario = usuarioRepository.findByEmail(email)
	            .orElseThrow(() -> new CustomGenericException("E-mail sem referência em conta, cadastre-se!", HttpStatus.NOT_FOUND));
	    
	    String token = usuario.getTokenRecuperacao();
	    String[] partes = token.split("-");

	    if (partes.length != 2) {
	        throw new CustomGenericException("Token inválido. Tente reenviar o token de recuperação.", HttpStatus.BAD_REQUEST);
	    }

	    try {
	    	LocalDateTime dataToken = LocalDateTime.parse(partes[1], TokenGeneretor.FORMATTER);
	        LocalDateTime expiracao = dataToken.plusMinutes(5);
	        LocalDateTime agora = LocalDateTime.now();

	        if (agora.isAfter(expiracao)) {
	            return 0; 
	        }

	        return java.time.Duration.between(agora, expiracao).getSeconds(); 
	    } catch (Exception e) {
	        throw new CustomGenericException("Erro ao verificar token.", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	//ENVIA UM NOVO CÓDIGO DE VÁLIDAÇÃO
	public String reenviarCodigoRecuperacao(String email) {
	    Usuario usuario = usuarioRepository.findByEmail(email)
	            .orElseThrow(() -> new CustomGenericException("E-mail sem referência em conta, cadastre-se!", HttpStatus.NOT_FOUND));

	    if (Boolean.TRUE.equals(usuario.getRecuperado())) {
	        throw new CustomGenericException("Usuário já pode efetuar a troca de senha!",  HttpStatus.ACCEPTED);
	    }

	    String novoTokenRecuperacao = tokenGeneretor.gerarToken(4);
	    usuario.setTokenRecuperacao(novoTokenRecuperacao);
	    usuarioRepository.save(usuario);
	    
	    String token = novoTokenRecuperacao.split("-")[0];
        emailService.sendRecoveryPasswordEmail(usuario.getEmail(), usuario.getNome(), token);

	    return "Novo código de recuperação enviado com sucesso!";
	}
		
	//RECUPERAÇÃO DA SENHA 
	public String recuperacaoSenha (String email, String novaSenha) {
		Usuario usuario = usuarioRepository.findByEmail(email)
	            .orElseThrow(() -> new CustomGenericException("E-mail sem referência em conta, cadastre-se!", HttpStatus.NOT_FOUND));
		
		if (Boolean.FALSE.equals(usuario.getRecuperado())) {
	        throw new CustomGenericException("Usuário não está permitido a efetuar a troca de senha!",  HttpStatus.ACCEPTED);
	    }
		
		usuario.setSenha(encoder.encode(novaSenha));
		usuarioRepository.save(usuario);
		
		return "Senha atualizada com sucesso!!";
	}
}
