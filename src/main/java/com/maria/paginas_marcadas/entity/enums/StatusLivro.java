package com.maria.paginas_marcadas.entity.enums;

public enum StatusLivro {
	
	PRIMEIRA_LEITURA("Quentinho, descobrindo o livro pela primeira vez"),
	SEGUNDA_LEITURA("Apaixonado, relendo com carinho e amor"),
	TERCEIRA_LEITURA("Não consegue ficar longe deste livro."),
	FAVORITO("Favorito, livro especial que volto sempre"),
    RELENDO("Relendo, revisitando páginas queridas"),
    ABANDONADO("Abandonado, larguei no meio"),
    RETOMADO("Retomado, voltei a ler após abandonar"),
    INSUPERAVEL("Incompleto, comecei várias vezes e não terminei"),
    TERMINADO("Terminado, li até o fim"),
    AGUARDANDO_LEITURA("Na fila, esperando para começar"),
    DESISTI("Desisti, não consegui continuar"),
    EM_ESPERA("Em espera, pausado por algum motivo");

    private final String descricao;

    StatusLivro(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
