package br.nexo.repository.memoria;

import br.nexo.model.Categoria;
import br.nexo.repository.CategoriaRepository;

import java.util.*;

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
        categoria.setId(proximoId++);
        categorias.put(categoria.getId(), categoria);
        return categoria;
    }

    @Override
    public void atualizar(Categoria categoria) {
        if (categorias.containsKey(categoria.getId())) {
            categorias.put(categoria.getId(), categoria);
        }
    }

    @Override
    public void excluir(int id) {
        categorias.remove(id);
    }

    @Override
    public Optional<Categoria> buscarPorId(int id) {
        return Optional.ofNullable(categorias.get(id));
    }

    @Override
    public Optional<Categoria> buscarPorNome(String nome) {
        if (nome == null) {
            return Optional.empty();
        }
        return categorias.values().stream()
                .filter(c -> c.getNome().equalsIgnoreCase(nome.trim()))
                .findFirst();
    }

    @Override
    public List<Categoria> listarTodas() {
        return new ArrayList<>(categorias.values());
    }
}
