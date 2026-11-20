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

/**
 * Controller da janela de categorias. Só traduz os cliques em chamadas ao
 * service e mostra o resultado; as regras ficam no CategoriaService.
 */
public class CategoriasController {

    private final CategoriaService categoriaService;

    @FXML private ListView<Categoria> listaCategorias;
    @FXML private TextField campoNome;

    public CategoriasController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @FXML
    private void initialize() {
        atualizarLista();

        // Ao selecionar uma categoria, o nome dela aparece no campo para renomear.
        listaCategorias.getSelectionModel().selectedItemProperty().addListener(
                (observavel, anterior, selecionada) -> {
                    if (selecionada != null) {
                        campoNome.setText(selecionada.getNome());
                    }
                });
    }

    @FXML
    private void aoCriar() {
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
        if (selecionada == null) {
            Alertas.info("Selecione uma categoria para renomear.");
            return;
        }
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
        Stage janela = (Stage) campoNome.getScene().getWindow();
        janela.close();
    }

    private void atualizarLista() {
        listaCategorias.getItems().setAll(categoriaService.listarTodas());
    }
}