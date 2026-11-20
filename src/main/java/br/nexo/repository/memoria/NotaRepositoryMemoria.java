package br.nexo.repository.memoria;

import br.nexo.model.Nota;
import br.nexo.repository.NotaRepository;

import java.util.ArrayList;
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