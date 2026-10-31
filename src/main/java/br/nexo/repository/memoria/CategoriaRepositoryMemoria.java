package br.nexo.repository.memoria;

import br.nexo.model.Categoria;
import br.nexo.repository.CategoriaRepository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REPOSITORY EM MEMÓRIA (provisório, fase Java): guarda as categorias numa coleção.
 * Os dados somem ao fechar o app, e tudo bem nesta fase.
 *
 * Papéis:
 *  - Implementar CategoriaRepository.
 *  - SIMULAR o banco gerando o id das categorias novas (o SQLite fará isso depois).
 *
 * NÃO valida nada (isso é do service). Será substituído pelo CategoriaRepositorySqlite.
 */
public class CategoriaRepositoryMemoria implements CategoriaRepository {

    private final Map<Integer, Categoria> categorias = new LinkedHashMap<>();
    private int proximoId = 1;

    @Override
    public Categoria salvar(Categoria categoria) {
        // TODO: Gerar o id, guardar e devolver a categoria com id.
        throw new UnsupportedOperationException("TODO: CategoriaRepositoryMemoria.salvar");
    }

    @Override
    public void atualizar(Categoria categoria) {
        // TODO: Substituir a categoria guardada que tem o mesmo id.
        throw new UnsupportedOperationException("TODO: CategoriaRepositoryMemoria.atualizar");
    }

    @Override
    public void excluir(int id) {
        // TODO: Remover a categoria com este id.
        throw new UnsupportedOperationException("TODO: CategoriaRepositoryMemoria.excluir");
    }

    @Override
    public Optional<Categoria> buscarPorId(int id) {
        // TODO: Devolver Optional.ofNullable(...).
        throw new UnsupportedOperationException("TODO: CategoriaRepositoryMemoria.buscarPorId");
    }

    @Override
    public Optional<Categoria> buscarPorNome(String nome) {
        // TODO: Comparar o nome (decidam: ignora maiúsculas?).
        throw new UnsupportedOperationException("TODO: CategoriaRepositoryMemoria.buscarPorNome");
    }

    @Override
    public List<Categoria> listarTodas() {
        // TODO: Devolver uma CÓPIA da lista.
        throw new UnsupportedOperationException("TODO: CategoriaRepositoryMemoria.listarTodas");
    }
}
