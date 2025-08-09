package com.maria.paginas_marcadas.entity;

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
@Table(name = "sub_categoria")
public class SubCategoria {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sub_categoria")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "nome_sub_categoria", nullable = false, length = 100)
    private String nome;

   /*
    @ManyToMany
    @JoinTable(
        name = "sub_categoria_livro",
        schema = "principal",
        joinColumns = @JoinColumn(name = "id_sub_categoria"),
        inverseJoinColumns = @JoinColumn(name = "id_livro")
    )
    private List<Livro> livros = new ArrayList<>();*/
}
