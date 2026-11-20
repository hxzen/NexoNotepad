package br.nexo.service;

import br.nexo.exception.NaoEncontradoException;
import br.nexo.exception.ValidacaoException;
import br.nexo.model.Categoria;
import br.nexo.model.Nota;
import br.nexo.repository.CategoriaRepository;
import br.nexo.repository.NotaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Camada de regras de negócio das notas.
 * Valida os dados antes de chegar ao repository e não conhece JavaFX nem a
 * implementação concreta do repository (só a interface).
 */
public class NotaService {

    private static final int TAMANHO_MAXIMO_TITULO = 100;

    private final NotaRepository notaRepository;
    private final CategoriaRepository categoriaRepository;

    public NotaService(NotaRepository notaRepository, CategoriaRepository categoriaRepository) {
        this.notaRepository = notaRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public Nota criar(String titulo, String conteudo, Categoria categoria) {
        validarTitulo(titulo);
        validarCategoria(categoria);

        Nota nota = new Nota(titulo.trim(), textoOuVazio(conteudo), categoria);
        return notaRepository.salvar(nota);
    }

    /** Edita a nota e atualiza a data de edição. */
    public Nota editar(int id, String titulo, String conteudo, Categoria categoria) {
        Nota nota = buscarPorId(id);
        validarTitulo(titulo);
        validarCategoria(categoria);

        nota.setTitulo(titulo.trim());
        nota.setConteudo(textoOuVazio(conteudo));
        nota.setCategoria(categoria);
        nota.setDataEdicao(LocalDateTime.now());

        notaRepository.atualizar(nota);
        return nota;
    }

    /** Confere se a nota existe antes de excluir, para não apagar "nada" em silêncio. */
    public void excluir(int id) {
        buscarPorId(id);
        notaRepository.excluir(id);
    }

    /** Traduz o Optional vazio do repository em um erro compreensível. */
    public Nota buscarPorId(int id) {
        Optional<Nota> nota = notaRepository.buscarPorId(id);
        if (nota.isEmpty()) {
            throw new NaoEncontradoException("Nota não encontrada.");
        }
        return nota.get();
    }

    public List<Nota> listarTodas() {
        return notaRepository.listarTodas();
    }

    /** Pesquisa vazia devolve todas as notas. */
    public List<Nota> pesquisar(String termo) {
        if (termo == null || termo.trim().isEmpty()) {
            return notaRepository.listarTodas();
        }
        return notaRepository.buscarPorTermo(termo.trim());
    }

    /** Sem categoria escolhida, devolve todas as notas. */
    public List<Nota> listarPorCategoria(Categoria categoria) {
        if (categoria == null || categoria.getId() == null) {
            return notaRepository.listarTodas();
        }
        return notaRepository.listarPorCategoria(categoria.getId());
    }

    private void validarTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new ValidacaoException("O título da nota é obrigatório.");
        }
        if (titulo.trim().length() > TAMANHO_MAXIMO_TITULO) {
            throw new ValidacaoException(
                    "O título pode ter no máximo " + TAMANHO_MAXIMO_TITULO + " caracteres.");
        }
    }

    /** A categoria é opcional, mas se for informada precisa existir. */
    private void validarCategoria(Categoria categoria) {
        if (categoria == null) {
            return;
        }
        if (categoria.getId() == null || categoriaRepository.buscarPorId(categoria.getId()).isEmpty()) {
            throw new ValidacaoException("A categoria informada não existe.");
        }
    }

    private String textoOuVazio(String texto) {
        return texto == null ? "" : texto;
    }
}