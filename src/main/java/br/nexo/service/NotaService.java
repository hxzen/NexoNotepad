package br.nexo.service;

import br.nexo.model.Categoria;
import br.nexo.model.Nota;
import br.nexo.repository.CategoriaRepository;
import br.nexo.repository.NotaRepository;

import java.util.List;

/**
 * SERVICE: regras de negócio das notas. É a "inteligência" do app.
 *
 * Recebe os repositories pela INTERFACE (nunca importe repository.memoria
 * nem repository.sqlite aqui) e NÃO usa nada de JavaFX.
 *
 * Responsabilidades:
 *  - Validar antes de qualquer chamada ao repository:
 *      título vazio ou só com espaços, tamanho máximo do título,
 *      categoria informada que não existe.
 *    Em caso de erro: lançar ValidacaoException com mensagem clara.
 *  - Definir a data de criação ao criar e atualizar a data de edição ao editar
 *    (decisão de vocês: quem atualiza, a Nota ou o service?).
 *  - Lançar NaoEncontradoException quando o id não existir.
 */
public class NotaService {

    private final NotaRepository notaRepository;
    private final CategoriaRepository categoriaRepository;

    public NotaService(NotaRepository notaRepository, CategoriaRepository categoriaRepository) {
        this.notaRepository = notaRepository;
        this.categoriaRepository = categoriaRepository;
    }

    /** Valida e cria uma nota nova. A categoria pode ser nula? (decisão de vocês) */
    public Nota criar(String titulo, String conteudo, Categoria categoria) {
        throw new UnsupportedOperationException("TODO: NotaService.criar");
    }

    /** Valida e edita uma nota existente, atualizando a data de edição. */
    public Nota editar(int id, String titulo, String conteudo, Categoria categoria) {
        throw new UnsupportedOperationException("TODO: NotaService.editar");
    }

    /** Exclui a nota. (A confirmação com o usuário é do controller.) */
    public void excluir(int id) {
        throw new UnsupportedOperationException("TODO: NotaService.excluir");
    }

    /** Busca por id; lança NaoEncontradoException se não existir. */
    public Nota buscarPorId(int id) {
        throw new UnsupportedOperationException("TODO: NotaService.buscarPorId");
    }

    public List<Nota> listarTodas() {
        throw new UnsupportedOperationException("TODO: NotaService.listarTodas");
    }

    /** Pesquisa por título e conteúdo. Termo vazio: o que deve acontecer? */
    public List<Nota> pesquisar(String termo) {
        throw new UnsupportedOperationException("TODO: NotaService.pesquisar");
    }

    public List<Nota> listarPorCategoria(Categoria categoria) {
        throw new UnsupportedOperationException("TODO: NotaService.listarPorCategoria");
    }
}
