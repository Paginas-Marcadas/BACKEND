package com.maria.paginas_marcadas.entity;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import com.maria.paginas_marcadas.entity.enums.Genero;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuario")
public class Usuario {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;
	
    @Column(name = "avatar", columnDefinition = "TEXT")
    private String avatar;

    @Column(name = "nome", length = 300, nullable = false)
    private String nome;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;
    
    @Column(name = "genero")
    @Enumerated(EnumType.STRING)
    private Genero genero;

    @Column(name = "email", length = 400, nullable = false, unique = true)
    private String email;

    @Column(name = "novo_email", length = 400)
    private String novoEmail;

    @Column(name = "senha", length = 1000, nullable = false)
    private String senha;

    @Column(name = "token_acesso", length = 1000)
    private String tokenAcesso;

    @Column(name = "token_validacao", length = 1000)
    private String tokenValidacao;

    @Column(name = "token_recuperacao", length = 1000)
    private String tokenRecuperacao;

    @Column(name = "validado")
    private Boolean validado = false;

    @Column(name = "bloqueado")
    private Boolean bloqueado = false;

    @Column(name = "recuperado")
    private Boolean recuperado = false;

    @Column(name = "data_cadastro")
    private OffsetDateTime dataCadastro;
    
    /*
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubCategoria> subCategorias = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Livro> livros = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Leitura> leituras = new ArrayList<>();*/
}
