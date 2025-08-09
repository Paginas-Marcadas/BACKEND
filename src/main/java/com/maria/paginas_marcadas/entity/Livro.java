package com.maria.paginas_marcadas.entity;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import com.maria.paginas_marcadas.entity.enums.Categoria;
import com.maria.paginas_marcadas.entity.enums.StatusLivro;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
@Table(name = "livro")
public class Livro {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_livro")
    private Long id;
	
	@Column(name = "capa", columnDefinition = "TEXT")
	private String capa;
	
	@Column(name = "ordenacao")
    private Integer ordenacao;

    @Column(name = "titulo", length = 500, nullable = false)
    private String titulo;
    
    @Column(name = "sinopse", columnDefinition = "TEXT")
    private String sinopse;
    
    @Column(name = "autor", length = 500)
    private String autor;

    @Column(name = "status_livro", length = 300)
    @Enumerated(EnumType.STRING)
    private StatusLivro status;

    @Column(name = "categoria", length = 300)
    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    @Column(name = "total_paginas")
    private Integer totalPaginas;

    @Column(name = "data_ultimo_acesso", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime dataUltimoAcesso;

    @Column(name = "data_cadastro", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime dataCadastro;
    
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToMany(mappedBy = "livros")
    private List<SubCategoria> subCategorias = new ArrayList<>();

    /*
    // Relação OneToMany com Leitura
    @OneToMany(mappedBy = "livro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Leitura> leituras = new ArrayList<>();
    */
}
