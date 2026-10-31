package br.nexo.repository.sqlite;

import br.nexo.model.Nota;
import br.nexo.repository.NotaRepository;
import java.util.List;
import java.util.Optional;

/**
 * REPOSITORY SQLITE (FASE SQL, NÃO MEXER AGORA): mesma interface NotaRepository, mas
 * guardando no banco via JDBC (Conexao). Quando chegar a hora:
 *  - Converter a linha do banco em objeto e o objeto em linha.
 *  - Usar PreparedStatement (nunca concatenar texto em SQL).
 *  - Fechar os recursos (try-with-resources).
 *  - Transformar falhas de banco em erro compreensível.
 * Depois é só trocar a linha no App.
 */
public class NotaRepositorySqlite implements NotaRepository {

    @Override
    public Nota salvar(Nota nota) {
        // TODO: Gerar o id (proximoId++), guardar e devolver a nota com id.
        throw new UnsupportedOperationException("TODO: NotaRepositorySqlite.salvar");
    }

    @Override
    public void atualizar(Nota nota) {
        // TODO: Substituir a nota guardada que tem o mesmo id.
        throw new UnsupportedOperationException("TODO: NotaRepositorySqlite.atualizar");
    }

    @Override
    public void excluir(int id) {
        // TODO: Remover a nota com este id.
        throw new UnsupportedOperationException("TODO: NotaRepositorySqlite.excluir");
    }

    @Override
    public Optional<Nota> buscarPorId(int id) {
        // TODO: Devolver Optional.ofNullable(...) da coleção.
        throw new UnsupportedOperationException("TODO: NotaRepositorySqlite.buscarPorId");
    }

    @Override
    public List<Nota> listarTodas() {
        // TODO: Devolver uma CÓPIA da lista (não exponha a coleção interna).
        throw new UnsupportedOperationException("TODO: NotaRepositorySqlite.listarTodas");
    }

    @Override
    public List<Nota> buscarPorTermo(String termo) {
        // TODO: Filtrar por título OU conteúdo, ignorando maiúsculas.
        throw new UnsupportedOperationException("TODO: NotaRepositorySqlite.buscarPorTermo");
    }

    @Override
    public List<Nota> listarPorCategoria(int categoriaId) {
        // TODO: Filtrar as notas dessa categoria.
        throw new UnsupportedOperationException("TODO: NotaRepositorySqlite.listarPorCategoria");
    }

    @Override
    public int contarPorCategoria(int categoriaId) {
        // TODO: Contar as notas dessa categoria.
        throw new UnsupportedOperationException("TODO: NotaRepositorySqlite.contarPorCategoria");
    }
}
