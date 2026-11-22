package br.nexo.repository;

import br.nexo.model.Nota;

import java.util.List;
import java.util.Optional;

/**
 * REPOSITORY (contrato): quais operações de dados existem para Nota.
 *
 * É uma INTERFACE: não diz COMO guardar (memória, SQLite...), só O QUÊ.
 * O NotaService conhece apenas esta interface. Por isso, trocar a
 * implementação em memória pela do SQLite não muda o service.
 */
public interface NotaRepository {

    /** Guarda uma nota nova e devolve a nota COM o id gerado. */
    Nota salvar(Nota nota);

    /** Atualiza uma nota que já existe (identificada pelo id). */
    void atualizar(Nota nota);

    /** Remove a nota com este id. */
    void excluir(int id);

    /** Procura por id. Optional vazio se não existir. */
    Optional<Nota> buscarPorId(int id);

    /** Devolve todas as notas. */
    List<Nota> listarTodas();

    /** Notas cujo título OU conteúdo contém o termo (ignorando maiúsculas/minúsculas). */
    List<Nota> buscarPorTermo(String termo);

    /** Notas de uma categoria. */
    List<Nota> listarPorCategoria(int categoriaId);

    /** Quantas notas usam esta categoria (para a regra de excluir categoria). */
    int contarPorCategoria(int categoriaId);
}
