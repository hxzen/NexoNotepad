package br.nexo.service;

import br.nexo.model.Categoria;
import br.nexo.repository.CategoriaRepository;
import br.nexo.repository.NotaRepository;

import java.util.List;

/**
 * SERVICE: regras de negócio das categorias.
 *
 * Recebe o NotaRepository também, porque precisa saber se existem notas
 * usando uma categoria antes de excluí-la.
 *
 * Responsabilidades:
 *  - Validar o nome (vazio, tamanho máximo) e impedir nomes repetidos.
 *  - Decidir o que acontece ao excluir uma categoria que tem notas:
 *    bloquear? deixar as notas sem categoria? (decisão de vocês)
 *  - Lançar ValidacaoException / NaoEncontradoException com mensagens claras.
 */
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final NotaRepository notaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository, NotaRepository notaRepository) {
        this.categoriaRepository = categoriaRepository;
        this.notaRepository = notaRepository;
    }

    public Categoria criar(String nome) {
        throw new UnsupportedOperationException("TODO: CategoriaService.criar");
    }

    public Categoria renomear(int id, String novoNome) {
        throw new UnsupportedOperationException("TODO: CategoriaService.renomear");
    }

    public void excluir(int id) {
        throw new UnsupportedOperationException("TODO: CategoriaService.excluir");
    }

    public Categoria buscarPorId(int id) {
        throw new UnsupportedOperationException("TODO: CategoriaService.buscarPorId");
    }

    public List<Categoria> listarTodas() {
        throw new UnsupportedOperationException("TODO: CategoriaService.listarTodas");
    }
}
