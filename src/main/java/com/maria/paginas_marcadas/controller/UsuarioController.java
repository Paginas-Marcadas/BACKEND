package com.maria.paginas_marcadas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.maria.paginas_marcadas.dto.UsuarioRequestDto;
import com.maria.paginas_marcadas.dto.UsuarioUpdateDto;
import com.maria.paginas_marcadas.service.UsuarioService;
import com.maria.paginas_marcadas.utils.RespostaComTokenDeAcesso;

import io.jsonwebtoken.io.IOException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

	@Autowired
    private UsuarioService usuarioService;

    @PostMapping("/cadastro")
    public ResponseEntity<String> cadastrar(@Valid @RequestBody UsuarioRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioService.cadastro(request));
    }

    @GetMapping("/validar-email")
    public ResponseEntity<String> validarEmail(
            @RequestParam String email,
            @RequestParam String token) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(usuarioService.validarEmail(email, token));
    }

    @GetMapping("/tempo-restante-validacao")
    public ResponseEntity<Long> tempoRestanteValidacao(@RequestParam String email) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(usuarioService.tempoRestanteParaNovoToken(email));
    }

    @PostMapping("/reenviar-codigo-validacao")
    public ResponseEntity<String> reenviarCodigoValidacao(@RequestParam String email) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(usuarioService.reenviarCodigoValidacao(email));
    }

    @PostMapping("/login")
    public ResponseEntity<RespostaComTokenDeAcesso> login(
            @RequestParam String email,
            @RequestParam String senha,
            @RequestParam Boolean salvar) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(usuarioService.login(email, senha, salvar));
    }

    @PostMapping("/avatar")
    public ResponseEntity<String> uploadAvatar(@RequestParam MultipartFile imagem) throws IOException, java.io.IOException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(usuarioService.uploadAvatarFile(imagem));
    }

    @PatchMapping("/atualizar")
    public ResponseEntity<String> atualizarCadastro(@Valid @RequestBody UsuarioUpdateDto update) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(usuarioService.atualizarCadastro(update));
    }

    @GetMapping("/tempo-restante-recuperacao")
    public ResponseEntity<Long> tempoRestanteRecuperacao(@RequestParam String email) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(usuarioService.tempoRestanteParaNovoTokenRecuperacao(email));
    }

    @PostMapping("/reenviar-codigo-recuperacao")
    public ResponseEntity<String> reenviarCodigoRecuperacao(@RequestParam String email) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(usuarioService.reenviarCodigoRecuperacao(email));
    }

    @PostMapping("/recuperar-senha")
    public ResponseEntity<String> recuperarSenha(
            @RequestParam String email,
            @RequestParam String novaSenha) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(usuarioService.recuperacaoSenha(email, novaSenha));
    }
}
