package com.maria.paginas_marcadas.entity;

import java.time.OffsetDateTime;
import com.maria.paginas_marcadas.entity.enums.StatusLeitura;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@Table(name = "leitura")
public class Leitura {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_leitura")
    private Long id;

	@Column(name = "ordenacao")
    private Integer ordenacao;

    @Column(name = "total_paginas_lidas", columnDefinition = "integer default 0")
    private Integer totalPaginasLidas = 0;
    
    @Column(name = "status_leitura", length = 300)
    private StatusLeitura status;

    @Column(name = "data_inicio", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime dataInicio;

    @Column(name = "data_termino", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime dataTermino;

    @Column(name = "data_abandono", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime dataAbandono;

    @Column(name = "data_retorno", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime dataRetorno;

    @Column(name = "data_cadastro", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime dataCadastro;
    
    @ManyToOne
    @JoinColumn(name = "id_livro", nullable = false)
    private Livro livro;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    /*
    @OneToMany(mappedBy = "leitura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemoriaLeitura> memoriasLeitura = new ArrayList<>();

    @OneToMany(mappedBy = "leitura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemoriaLivro> memoriasLivro = new ArrayList<>();
	*/
}
