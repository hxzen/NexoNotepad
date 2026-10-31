package br.nexo.util;

/**
 * UTIL: atalhos para os pop-ups (javafx.scene.control.Alert) usados em várias
 * partes da interface. Evita repetir as mesmas linhas em todo método do controller.
 *
 * Só crie/mantenha esta classe se ela realmente for usada. Se ficar com
 * poucas linhas, pode viver dentro do controller.
 */
public class Alertas {

    /** Pop-up de erro com a mensagem (ex.: texto de uma ValidacaoException). */
    public static void erro(String mensagem) {
        throw new UnsupportedOperationException("TODO: Alertas.erro");
    }

    /** Pop-up informativo (ex.: "Nota salva com sucesso"). */
    public static void info(String mensagem) {
        throw new UnsupportedOperationException("TODO: Alertas.info");
    }

    /** Pergunta Sim/Não e devolve true se o usuário confirmou (ex.: antes de excluir). */
    public static boolean confirmar(String mensagem) {
        throw new UnsupportedOperationException("TODO: Alertas.confirmar");
    }
}
