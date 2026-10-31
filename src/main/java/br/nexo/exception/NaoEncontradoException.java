package br.nexo.exception;

/**
 * EXCEPTION: lançada pelo SERVICE quando se procura uma nota ou categoria
 * por id e ela não existe (ex.: editar ou excluir algo que já foi removido).
 */
public class NaoEncontradoException extends RuntimeException {
    public NaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
