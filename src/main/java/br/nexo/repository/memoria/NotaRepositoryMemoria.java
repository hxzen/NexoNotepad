package br.nexo.repository.memoria;

import br.nexo.model.Nota;
import br.nexo.repository.NotaRepository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REPOSITORY EM MEMÓRIA (provisório, fase Java): guarda as notas numa coleção.
 * Os dados somem ao fechar o app, e tudo bem nesta fase.
 *
 * Papéis:
 *  - Implementar NotaRepository.
 *  - SIMULAR o banco gerando o id das notas novas (o SQLite fará isso depois).
 *
 * NÃO valida nada (isso é do service). Será substituído pelo NotaRepositorySqlite.
 */
public class NotaRepositoryMemoria implements NotaRepository {

    // Decisão de vocês: LinkedHashMap mantém a ordem de inserção; pensem se outra coleção serve melhor.
    private final Map<Integer, Nota> notas = new LinkedHashMap<>();
    private int proximoId = 1;

    @Override
    public Nota salvar(Nota nota) {
        // TODO: Gerar o id (proximoId++), guardar e devolver a nota com id.
        throw new UnsupportedOperationException("TODO: NotaRepositoryMemoria.salvar");
    }

    @Override
    public void atualizar(Nota nota) {
        // TODO: Substituir a nota guardada que tem o mesmo id.
        throw new UnsupportedOperationException("TODO: NotaRepositoryMemoria.atualizar");
    }

    @Override
    public void excluir(int id) {
        // TODO: Remover a nota com este id.
        throw new UnsupportedOperationException("TODO: NotaRepositoryMemoria.excluir");
    }

    @Override
    public Optional<Nota> buscarPorId(int id) {
        // TODO: Devolver Optional.ofNullable(...) da coleção.
        throw new UnsupportedOperationException("TODO: NotaRepositoryMemoria.buscarPorId");
    }

    @Override
    public List<Nota> listarTodas() {
        // TODO: Devolver uma CÓPIA da lista (não exponha a coleção interna).
        throw new UnsupportedOperationException("TODO: NotaRepositoryMemoria.listarTodas");
    }

    @Override
    public List<Nota> buscarPorTermo(String termo) {
        // TODO: Filtrar por título OU conteúdo, ignorando maiúsculas.
        throw new UnsupportedOperationException("TODO: NotaRepositoryMemoria.buscarPorTermo");
    }

    @Override
    public List<Nota> listarPorCategoria(int categoriaId) {
        // TODO: Filtrar as notas dessa categoria.
        throw new UnsupportedOperationException("TODO: NotaRepositoryMemoria.listarPorCategoria");
    }

    @Override
    public int contarPorCategoria(int categoriaId) {
        // TODO: Contar as notas dessa categoria.
        throw new UnsupportedOperationException("TODO: NotaRepositoryMemoria.contarPorCategoria");
    }
}
