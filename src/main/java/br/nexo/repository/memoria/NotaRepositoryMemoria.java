package br.nexo.repository.memoria;

import br.nexo.model.Nota;
import br.nexo.repository.NotaRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/*
Repository de notas que guarda os dados em memória, num Map que usa o id como chave.
Implementa a interface NotaRepository e faz o papel do banco: gera o id de cada nota
nova, salva, atualiza, exclui e busca por id. Também pesquisa notas por um termo, que
pode estar no título ou no conteúdo (ignorando maiúsculas e minúsculas), lista as notas
de uma categoria e conta quantas notas usam uma categoria, o que o service usa para
impedir a exclusão de uma categoria em uso. Não valida nada, porque as regras ficam no
service. Os dados somem quando o app fecha, por isso ele foi usado na fase Java, antes
do SQLite, e a versão com banco implementa a mesma interface.
*/
public class NotaRepositoryMemoria implements NotaRepository {

    private final Map<Integer, Nota> notas = new LinkedHashMap<>();
    private int proximoId = 1;

    @Override
    public Nota salvar(Nota nota) {
        nota.setId(proximoId++);
        notas.put(nota.getId(), nota);
        return nota;
    }

    @Override
    public void atualizar(Nota nota) {
        if (notas.containsKey(nota.getId())) {
            notas.put(nota.getId(), nota);
        }
    }

    @Override
    public void excluir(int id) {
        notas.remove(id);
    }

    @Override
    public Optional<Nota> buscarPorId(int id) {
        return Optional.ofNullable(notas.get(id));
    }

    @Override
    public List<Nota> listarTodas() {
        return new ArrayList<>(notas.values());
    }

    @Override
    public List<Nota> buscarPorTermo(String termo) {
        List<Nota> resultado = new ArrayList<>();
        String busca = termo == null ? "" : termo.trim().toLowerCase();

        for (Nota nota : notas.values()) {
            String titulo = nota.getTitulo() == null ? "" : nota.getTitulo().toLowerCase();
            String conteudo = nota.getConteudo() == null ? "" : nota.getConteudo().toLowerCase();

            if (titulo.contains(busca) || conteudo.contains(busca)) {
                resultado.add(nota);
            }
        }
        return resultado;
    }

    @Override
    public List<Nota> listarPorCategoria(int categoriaId) {
        List<Nota> resultado = new ArrayList<>();

        for (Nota nota : notas.values()) {
            if (nota.getCategoria() != null
                    && nota.getCategoria().getId() != null
                    && nota.getCategoria().getId().equals(categoriaId)) {
                resultado.add(nota);
            }
        }
        return resultado;
    }

    @Override
    public int contarPorCategoria(int categoriaId) {
        return listarPorCategoria(categoriaId).size();
    }
}