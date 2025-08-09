package com.maria.paginas_marcadas.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import com.maria.paginas_marcadas.entity.enums.StatusMemoriaLivro;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "memoria_livro")
public class MemoriaLivro {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_memoria_livro")
    private Long id;

    @Column(name = "titulo", length = 300)
    private String titulo;

    @Column(name = "subtitulo", length = 300)
    private String subtitulo;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "nota", precision = 3, scale = 1)
    private BigDecimal nota;

    @Column(name = "status_memoria_livro", length = 300)
    private StatusMemoriaLivro status;

    @Column(name = "data_cadastro")
    private OffsetDateTime dataCadastro;

    @ManyToMany(mappedBy = "memoriasLivro")
    private List<Tag> tags = new ArrayList<>();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_leitura")
    private Leitura leitura;
    
    @OneToMany(mappedBy = "memoriaLivro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Foto> fotos = new ArrayList<>();
}
