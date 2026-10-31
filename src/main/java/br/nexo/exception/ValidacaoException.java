package br.nexo.exception;

/**
 * EXCEPTION: lançada pelo SERVICE quando uma regra de negócio é violada
 * (título vazio, título grande demais, nome de categoria repetido, etc.).
 *
 * A mensagem deve ser legível para o usuário final: o controller a mostra
 * num Alert. Nunca coloque detalhes técnicos nela.
 *
 * Decisão de vocês: RuntimeException (não obriga try/catch) ou Exception
 * (obriga)? Aqui começou como RuntimeException; discutam as vantagens.
 */
public class ValidacaoException extends RuntimeException {
    public ValidacaoException(String mensagem) {
        super(mensagem);
    }
}
