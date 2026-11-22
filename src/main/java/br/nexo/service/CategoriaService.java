package br.nexo.service;

import br.nexo.exception.NaoEncontradoException;
import br.nexo.exception.ValidacaoException;
import br.nexo.model.Categoria;
import br.nexo.repository.CategoriaRepository;
import br.nexo.repository.NotaRepository;
import br.nexo.util.Cores;

import java.util.List;
import java.util.Optional;

/**
 * SERVICE: regras de negócio das categorias.
 *
 * Recebe o NotaRepository também, porque precisa saber se existem notas
 * usando uma categoria antes de excluí-la.
 *
 * Responsabilidades:
 *  - Validar o nome (vazio, tamanho máximo) e impedir nomes repetidos.
 *  - Decidir o que acontece ao excluir uma categoria que tem notas:
 *    DECISÃO ATUAL: bloquear a exclusão (evita notas "órfãs").
 *  - Lançar ValidacaoException / NaoEncontradoException com mensagens claras.
 */
public class CategoriaService {

    private static final int TAMANHO_MAXIMO_NOME = 50;

    private final CategoriaRepository categoriaRepository;
    private final NotaRepository notaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository, NotaRepository notaRepository) {
        this.categoriaRepository = categoriaRepository;
        this.notaRepository = notaRepository;
    }

    public Categoria criar(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new ValidacaoException("O nome da categoria não pode ser vazio.");
        }
        String nomeLimpo = nome.trim();
        if (nomeLimpo.length() > TAMANHO_MAXIMO_NOME) {
            throw new ValidacaoException("O nome da categoria deve ter no máximo "
                    + TAMANHO_MAXIMO_NOME + " caracteres.");
        }

        if (categoriaRepository.buscarPorNome(nomeLimpo).isPresent()) {
            throw new ValidacaoException("Já existe uma categoria com o nome \"" + nomeLimpo + "\".");
        }

        Categoria nova = new Categoria(nomeLimpo);
        nova.setCor(Cores.gerarAleatoria()); // cor sorteada UMA vez, ao criar; renomear não muda
        return categoriaRepository.salvar(nova);
    }

    public Categoria renomear(int id, String novoNome) {
        Categoria categoria = buscarPorId(id);

        if (novoNome == null || novoNome.trim().isEmpty()) {
            throw new ValidacaoException("O nome da categoria não pode ser vazio.");
        }
        String nomeLimpo = novoNome.trim();
        if (nomeLimpo.length() > TAMANHO_MAXIMO_NOME) {
            throw new ValidacaoException("O nome da categoria deve ter no máximo "
                    + TAMANHO_MAXIMO_NOME + " caracteres.");
        }

        // Só é duplicado se o nome já pertence a OUTRA categoria.
        Optional<Categoria> existente = categoriaRepository.buscarPorNome(nomeLimpo);
        if (existente.isPresent() && existente.get().getId() != id) {
            throw new ValidacaoException("Já existe uma categoria com o nome \"" + nomeLimpo + "\".");
        }

        categoria.setNome(nomeLimpo);
        categoriaRepository.atualizar(categoria);
        return categoria;
    }

    public void excluir(int id) {
        Categoria categoria = buscarPorId(id);

        if (notaRepository.contarPorCategoria(id) > 0) {
            throw new ValidacaoException("Não é possível excluir a categoria \"" + categoria.getNome()
                    + "\" porque existem notas usando ela.");
        }

        categoriaRepository.excluir(id);
    }

    public Categoria buscarPorId(int id) {
        return categoriaRepository.buscarPorId(id)
                .orElseThrow(() -> new NaoEncontradoException("Categoria não encontrada (id " + id + ")."));
    }

    public List<Categoria> listarTodas() {
        return categoriaRepository.listarTodas();
    }
}