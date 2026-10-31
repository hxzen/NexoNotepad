package br.nexo.model;

/**
 * MODEL: representa uma categoria de notas (ex.: "Estudos", "Ideias").
 *
 * Só guarda dados e sabe se descrever. NÃO acessa banco, NÃO usa JavaFX
 * e NÃO faz validação de regra de negócio (isso é do CategoriaService).
 *
 * O que já existe (do código de vocês): atributos privados, construtor e getters.
 *
 * TODO (vocês):
 *  - Um segundo construtor para categoria NOVA (ainda sem id). Qual tipo de id
 *    consegue representar "ainda não foi salva"?
 *  - Decidir a ordem dos parâmetros e usar a mesma em todas as classes.
 *  - Setter do nome (renomear)? O id deve ter setter?
 *  - toString(): o JavaFX usa para mostrar a categoria em listas e combos.
 *  - equals() e hashCode(): quando duas categorias são "a mesma"?
 */
public class Categoria {
    private int id;
    private String nome;

    public Categoria(String nome, int id) {
        this.nome = nome;
        this.id = id;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
}
