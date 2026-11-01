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
        // TODO: Gerar o id, guardar e devolver a categoria com id.
        categoria.setId(proximoId++);
        categorias.put(categoria.getId(), categoria);
        return categoria;
    }

    @Override
    public void atualizar(Categoria categoria) {
        // TODO: Substituir a categoria guardada que tem o mesmo id.
        if (categorias.containsKey(categoria.getId())) {
            categorias.put(categoria.getId(), categoria);
        }
    }

    @Override
    public void excluir(int id) {
        // TODO: Remover a categoria com este id.
        categorias.remove(id);
    }

    @Override
    public Optional<Categoria> buscarPorId(int id) {
        // TODO: Devolver Optional.ofNullable(...).
        return Optional.ofNullable(categorias.get(id));
    }

    @Override
    public Optional<Categoria> buscarPorNome(String nome) {
        // TODO: Comparar o nome (decidam: ignora maiúsculas?).
        if (nome == null) {
            return Optional.empty();
        }
        return categorias.values().stream()
                .filter(c -> c.getNome().equalsIgnoreCase(nome.trim()))
                .findFirst();
    }

    @Override
    public List<Categoria> listarTodas() {
        // TODO: Devolver uma CÓPIA da lista.
        return new ArrayList<>(categorias.values());
    }
}
