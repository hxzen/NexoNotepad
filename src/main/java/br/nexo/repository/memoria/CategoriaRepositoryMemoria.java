package br.nexo.repository.memoria;

import br.nexo.model.Categoria;
import br.nexo.repository.CategoriaRepository;

import java.util.*;

/*
Repository de categorias que guarda os dados em memória, num Map que usa o id como
chave. Implementa a interface CategoriaRepository e faz o papel do banco: gera o id de
cada categoria nova, salva, atualiza, exclui e busca por id ou por nome (ignorando
maiúsculas e minúsculas). Não valida nada, porque as regras ficam no service. Os dados
somem quando o app fecha, por isso ele foi usado na fase Java, antes do SQLite, e a
versão com banco implementa a mesma interface.
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
