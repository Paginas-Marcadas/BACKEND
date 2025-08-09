package com.maria.paginas_marcadas.entity;

import java.util.ArrayList;
import java.util.List;
import com.maria.paginas_marcadas.entity.enums.TipoTag;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
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
@Table(name = "tag")
public class Tag {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tag")
    private Long idTag;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "nome_tag", nullable = false, length = 100)
    private String nome;
    
    @Column(name = "tipo_tag")
    @Enumerated(EnumType.STRING)
    private TipoTag tipo;

    @ManyToMany
    @JoinTable(
        name = "tag_memoria_leitura",
        schema = "principal",
        joinColumns = @JoinColumn(name = "id_tag"),
        inverseJoinColumns = @JoinColumn(name = "id_memoria_leitura")
    )
    private List<MemoriaLeitura> memoriasLeitura = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "tag_memoria_livro",
        schema = "principal",
        joinColumns = @JoinColumn(name = "id_tag"),
        inverseJoinColumns = @JoinColumn(name = "id_memoria_livro")
    )
    private List<MemoriaLivro> memoriasLivro = new ArrayList<>();
    
}
