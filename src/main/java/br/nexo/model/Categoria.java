package br.nexo.model;

/**
 * MODEL: representa uma categoria de notas (ex.: "Estudos", "Ideias").
 *
 * Só guarda dados e sabe se descrever. NÃO acessa banco, NÃO usa JavaFX
 * e NÃO faz validação de regra de negócio (isso é do CategoriaService).
 *
 * O que já existe (do código de vocês): atributos privados, construtor e getters.
 *
 */
public class Categoria {
    private Integer id;
    private String nome;

    public Categoria(String nome, Integer id) {
        this.nome = nome;
        this.id = id;
    }

    public Categoria(String nome) {
        this.nome = nome;
    }

    public Integer getId() { return id; }
    public String getNome() { return nome; }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Categoria outra)) return false;
        if (id == null || outra.id == null) return false;
        return id.equals(outra.id);
    }

    @Override
    public int hashCode() {
        return id == null ? System.identityHashCode(this) : id.hashCode();
    }
}
