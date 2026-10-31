package br.nexo.model;

import java.time.LocalDateTime;

/**
 * MODEL: representa uma nota.
 *
 * Só guarda dados e sabe se descrever. NÃO acessa banco, NÃO usa JavaFX
 * e NÃO valida regra de negócio (isso é do NotaService).
 *
 * Campos sugeridos (as decisões abaixo são de vocês):
 *  - id            (identificador; ver decisão sobre o tipo na Categoria)
 *  - titulo        (obrigatório, validado no service)
 *  - conteudo
 *  - dataCriacao   (definida uma vez, ao criar)
 *  - dataEdicao    (atualizada a cada edição)
 *  - categoria     (objeto Categoria ou só categoriaId? obrigatória ou opcional?)
 *
 * TODO (vocês):
 *  - Atributos privados + getters (setters só onde fizer sentido: dataCriacao deve ter?).
 *  - Construtor para nota NOVA (sem id, datas de agora).
 *  - Construtor para nota JÁ EXISTENTE (com id e datas, como virá do repository).
 *  - toString(): normalmente o título, pois a ListView usa isso.
 *  - equals()/hashCode() se precisarem comparar notas.
 */
public class Nota {
    private int id;
    private String titulo;
    private String conteudo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataEdicao;
    private Categoria categoria;

    // TODO: construtores, getters, setters necessários, toString
}
