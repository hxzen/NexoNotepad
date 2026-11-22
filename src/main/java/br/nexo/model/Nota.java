package br.nexo.model;

import java.time.LocalDateTime;

/*
Classe de modelo que representa uma nota. Guarda o id, o título, o conteúdo, a data de
criação, a data de edição e a categoria (opcional, pode ser null). Só guarda dados: não
acessa banco, não usa JavaFX e não valida nada, porque as regras ficam no NotaService.
Tem dois construtores: um para nota nova, que ainda não tem id e já nasce com as datas de
agora, e outro para nota que vem do banco, com id e datas já existentes. O toString
devolve o título, que é o que a lista de notas usa para mostrar cada item.
*/
public class Nota {
    private Integer id;
    private String titulo;
    private String conteudo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataEdicao;
    private Categoria categoria;

    // TODO: construtores, getters, setters necessários, toString

    //Getters
    public Integer getId(){return id;}
    public String getTitulo(){return titulo;}
    public String getConteudo(){return conteudo;}
    public LocalDateTime getDataCriacao(){return dataCriacao;}
    public LocalDateTime getDataEdicao(){return dataEdicao;}
    public Categoria getCategoria(){return categoria;}

    // Setters
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public void setDataEdicao(LocalDateTime dataEdicao) {
        this.dataEdicao = dataEdicao;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    //Construtores
    public Nota(String titulo, String conteudo, Categoria categoria) {
        this.titulo = titulo;
        this.conteudo = conteudo;
        this.categoria = categoria;

        this.dataCriacao = LocalDateTime.now();
        this.dataEdicao = LocalDateTime.now();
    }

    public Nota(int id, String titulo, String conteudo,
                LocalDateTime dataCriacao,
                LocalDateTime dataEdicao,
                Categoria categoria) {

        this.id = id;
        this.titulo = titulo;
        this.conteudo = conteudo;
        this.dataCriacao = dataCriacao;
        this.dataEdicao = dataEdicao;
        this.categoria = categoria;
    }

    @Override
    public String toString(){
        return titulo;
    }
}
