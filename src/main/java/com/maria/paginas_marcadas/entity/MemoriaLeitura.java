package com.maria.paginas_marcadas.entity;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import com.maria.paginas_marcadas.entity.enums.StatusMemoriaLeitura;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "memoria_leitura", schema = "principal")
public class MemoriaLeitura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_memoria_leitura")
    private Long id;

    @Column(name = "titulo", length = 300)
    private String titulo;

    @Column(name = "subtitulo", length = 300)
    private String subtitulo;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "pagina_inicial")
    private Integer paginaInicial;

    @Column(name = "pagina_final")
    private Integer paginaFinal;

    @Column(name = "status_memoria_leitura", length = 300)
    private StatusMemoriaLeitura status;

    @Column(name = "data_cadastro", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime dataCadastro;
    
    @ManyToOne
    @JoinColumn(name = "id_leitura")
    private Leitura leitura;

    @ManyToMany(mappedBy = "memoriasLeitura")
    private List<Tag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "memoriaLeitura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Foto> fotos = new ArrayList<>();
}
