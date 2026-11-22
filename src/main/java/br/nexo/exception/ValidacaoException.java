package br.nexo.exception;

/*
Exceção própria do projeto, lançada pelo service quando uma regra de negócio é violada,
como título vazio, título grande demais ou nome de categoria repetido. Ela estende
RuntimeException, então não obriga quem chama a usar try/catch, e quem trata é o
controller, que mostra a mensagem num pop-up. A mensagem é escrita para o usuário ler,
sem detalhe técnico.
*/
public class ValidacaoException extends RuntimeException {
    public ValidacaoException(String mensagem) {
        super(mensagem);
    }
}
