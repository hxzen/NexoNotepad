package br.nexo.repository.sqlite;

import br.nexo.model.Categoria;
import br.nexo.repository.CategoriaRepository;
import java.util.List;
import java.util.Optional;

/**
 * REPOSITORY SQLITE (FASE SQL, NÃO MEXER AGORA): mesma interface CategoriaRepository, mas
 * guardando no banco via JDBC (Conexao). Quando chegar a hora:
 *  - Converter a linha do banco em objeto e o objeto em linha.
 *  - Usar PreparedStatement (nunca concatenar texto em SQL).
 *  - Fechar os recursos (try-with-resources).
 *  - Transformar falhas de banco em erro compreensível.
 * Depois é só trocar a linha no App.
 */
public class CategoriaRepositorySqlite implements CategoriaRepository {

    @Override
    public Categoria salvar(Categoria categoria) {
        // TODO: Gerar o id, guardar e devolver a categoria com id.
        throw new UnsupportedOperationException("TODO: CategoriaRepositorySqlite.salvar");
    }

    @Override
    public void atualizar(Categoria categoria) {
        // TODO: Substituir a categoria guardada que tem o mesmo id.
        throw new UnsupportedOperationException("TODO: CategoriaRepositorySqlite.atualizar");
    }

    @Override
    public void excluir(int id) {
        // TODO: Remover a categoria com este id.
        throw new UnsupportedOperationException("TODO: CategoriaRepositorySqlite.excluir");
    }

    @Override
    public Optional<Categoria> buscarPorId(int id) {
        // TODO: Devolver Optional.ofNullable(...).
        throw new UnsupportedOperationException("TODO: CategoriaRepositorySqlite.buscarPorId");
    }

    @Override
    public Optional<Categoria> buscarPorNome(String nome) {
        // TODO: Comparar o nome (decidam: ignora maiúsculas?).
        throw new UnsupportedOperationException("TODO: CategoriaRepositorySqlite.buscarPorNome");
    }

    @Override
    public List<Categoria> listarTodas() {
        // TODO: Devolver uma CÓPIA da lista.
        throw new UnsupportedOperationException("TODO: CategoriaRepositorySqlite.listarTodas");
    }
}
