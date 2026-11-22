package br.nexo.exception;

/*
Exceção própria do projeto, lançada pelo service quando se procura uma nota ou
categoria pelo id e ela não existe, por exemplo ao editar ou excluir algo que já foi
removido. Também estende RuntimeException, então não obriga o uso de try/catch, e o
controller trata mostrando a mensagem num pop-up.
*/

public class NaoEncontradoException extends RuntimeException {
    public NaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
