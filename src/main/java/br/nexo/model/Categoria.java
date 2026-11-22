package br.nexo.model;

/*
Classe de modelo que representa uma categoria de notas, como "Estudos" ou "Ideias".
Guarda o id, o nome e a cor (um texto hexadecimal no formato #RRGGBB, usada na etiqueta
colorida da nota). Só guarda dados: não acessa banco, não usa JavaFX e não valida nada,
porque as regras ficam no CategoriaService. O id fica vazio (null) enquanto a categoria
não foi salva. Duas categorias são consideradas iguais quando têm o mesmo id, e o
toString devolve o nome, que é o que aparece nas listas e nos combos.
*/
public class Categoria {
    private Integer id;
    private String nome;
    private String cor;

    public Categoria(String nome, Integer id, String cor) {
        this.nome = nome;
        this.id = id;
        this.cor = cor;
    }

    public Categoria(String nome, Integer id) {
        this.nome = nome;
        this.id = id;
    }

    public Categoria(String nome) {
        this.nome = nome;
    }

    public Integer getId() { return id; }
    public String getNome() { return nome; }
    public String getCor() { return cor; }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCor(String cor) {
        this.cor = cor;
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