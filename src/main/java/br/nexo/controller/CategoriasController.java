package br.nexo.controller;

import br.nexo.exception.NaoEncontradoException;
import br.nexo.exception.ValidacaoException;
import br.nexo.model.Categoria;
import br.nexo.service.CategoriaService;
import br.nexo.util.Alertas;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/*
Controller da janela de categorias (categorias.fxml). Liga a tela ao CategoriaService:
pega o que o usuário digitou ou clicou, chama o service e mostra o resultado. As regras
(nome vazio, nome repetido, categoria com notas) ficam no service.
*/
public class CategoriasController {

    // O controller não cria o service, ele recebe pronto pelo construtor. Quem entrega
    // é o MainController, na hora de abrir esta janela.
    private final CategoriaService categoriaService;

    // @FXML faz o JavaFX preencher o campo com o componente do arquivo FXML que tem o
    // mesmo fx:id. Por isso o nome aqui precisa ser igual ao do categorias.fxml.
    @FXML private ListView<Categoria> listaCategorias;
    @FXML private TextField campoNome;

    public CategoriasController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    // O JavaFX chama o initialize sozinho quando o FXML termina de carregar. Por isso é
    // aqui que a lista já nasce preenchida quando a janela abre.
    @FXML
    private void initialize() {
        atualizarLista();

        // Ao selecionar uma categoria, o nome dela aparece no campo para renomear.
        // O addListener fica "escutando" a seleção da lista e roda o código sempre que
        // ela muda. O trecho (observavel, anterior, selecionada) -> { ... } é uma lambda,
        // uma função curta passada como argumento. A "selecionada" é a nova escolha.
        listaCategorias.getSelectionModel().selectedItemProperty().addListener(
                (observavel, anterior, selecionada) -> {
                    if (selecionada != null) {
                        campoNome.setText(selecionada.getNome());
                    }
                });
    }

    @FXML
    private void aoCriar() {
        // O try tenta executar o código. Se o service lançar ValidacaoException (nome
        // vazio, repetido...), o programa pula direto para o catch, que mostra a
        // mensagem num pop-up em vez de quebrar. As linhas seguintes do try não rodam.
        try {
            categoriaService.criar(campoNome.getText());
            campoNome.clear();
            atualizarLista();
        } catch (ValidacaoException e) {
            Alertas.erro(e.getMessage());
        }
    }

    @FXML
    private void aoRenomear() {
        Categoria selecionada = listaCategorias.getSelectionModel().getSelectedItem();
        // Sem categoria escolhida não há o que renomear: avisa e sai do método com return.
        if (selecionada == null) {
            Alertas.info("Selecione uma categoria para renomear.");
            return;
        }
        // O | no catch captura dois tipos de exceção no mesmo bloco, já que o tratamento
        // é igual: mostrar a mensagem.
        try {
            categoriaService.renomear(selecionada.getId(), campoNome.getText());
            atualizarLista();
        } catch (ValidacaoException | NaoEncontradoException e) {
            Alertas.erro(e.getMessage());
        }
    }

    @FXML
    private void aoExcluir() {
        Categoria selecionada = listaCategorias.getSelectionModel().getSelectedItem();
        if (selecionada == null) {
            Alertas.info("Selecione uma categoria para excluir.");
            return;
        }
        // Alertas.confirmar devolve true só se o usuário clicou em Sim. O ! inverte: se
        // ele NÃO confirmou, sai do método sem excluir nada.
        if (!Alertas.confirmar("Excluir a categoria \"" + selecionada.getNome() + "\"?")) {
            return;
        }
        try {
            categoriaService.excluir(selecionada.getId());
            campoNome.clear();
            atualizarLista();
        } catch (ValidacaoException | NaoEncontradoException e) {
            Alertas.erro(e.getMessage());
        }
    }

    @FXML
    private void aoFechar() {
        // Stage é a janela. O campo de texto sabe a qual cena pertence (getScene), e a
        // cena sabe a qual janela pertence (getWindow). O (Stage) é um cast: converte o
        // tipo genérico Window que o getWindow devolve para Stage, que tem o close().
        Stage janela = (Stage) campoNome.getScene().getWindow();
        janela.close();
    }

    // setAll troca todo o conteúdo da lista pelo que o service devolve, para a tela
    // sempre mostrar os dados atuais. Cada linha usa o toString() da Categoria.
    private void atualizarLista() {
        listaCategorias.getItems().setAll(categoriaService.listarTodas());
    }
}