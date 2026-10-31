package br.nexo.repository;

import br.nexo.model.Categoria;

import java.util.List;
import java.util.Optional;

/**
 * REPOSITORY (contrato): operações de dados para Categoria.
 * Mesma ideia do NotaRepository: só diz O QUÊ, não COMO.
 */
public interface CategoriaRepository {

    /** Guarda uma categoria nova e devolve a categoria COM o id gerado. */
    Categoria salvar(Categoria categoria);

    /** Atualiza uma categoria existente (renomear). */
    void atualizar(Categoria categoria);

    /** Remove a categoria com este id. */
    void excluir(int id);

    Optional<Categoria> buscarPorId(int id);

    /** Usado pelo service para impedir nomes repetidos. */
    Optional<Categoria> buscarPorNome(String nome);

    List<Categoria> listarTodas();
}
