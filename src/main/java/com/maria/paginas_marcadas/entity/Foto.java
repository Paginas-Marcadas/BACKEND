package com.maria.paginas_marcadas.entity;

import java.time.OffsetDateTime;

import com.maria.paginas_marcadas.entity.enums.TipoFoto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "foto", schema = "principal")
public class Foto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_foto")
    private Long id;

    @Column(name = "link", nullable = false, length = 1000)
    private String link;

    @Column(name = "ordem", nullable = false)
    private Integer ordem;

    @Column(length = 300, nullable = false)
    private TipoFoto tipo;

    @Column(length = 150)
    private String descricao;

    @Column(length = 300)
    private String localizacao;

    @Column(name = "data_cadastro", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime dataCadastro;
    
    @ManyToOne
    @JoinColumn(name = "id_memoria_leitura") 
    private MemoriaLeitura memoriaLeitura;

    @ManyToOne
    @JoinColumn(name = "id_memoria_livro") 
    private MemoriaLivro memoriaLivro;
}
