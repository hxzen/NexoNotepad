package br.nexo.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Pop-ups reutilizados pela interface, para os controllers não repetirem
 * a configuração do Alert a cada uso.
 */
public class Alertas {

    public static void erro(String mensagem) {
        mostrar(Alert.AlertType.ERROR, "Erro", mensagem);
    }

    public static void info(String mensagem) {
        mostrar(Alert.AlertType.INFORMATION, "Aviso", mensagem);
    }

    /** Devolve true somente se o usuário clicar em "Sim". */
    public static boolean confirmar(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, mensagem, ButtonType.YES, ButtonType.NO);
        alerta.setTitle("Confirmação");
        alerta.setHeaderText(null);

        Optional<ButtonType> resposta = alerta.showAndWait();
        return resposta.isPresent() && resposta.get() == ButtonType.YES;
    }

    private static void mostrar(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}