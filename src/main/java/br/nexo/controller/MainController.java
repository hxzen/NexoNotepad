package br.nexo.controller;

import br.nexo.model.Categoria;
import br.nexo.model.Nota;
import br.nexo.service.CategoriaService;
import br.nexo.service.NotaService;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * CONTROLLER da tela principal (main.fxml).
 *
 * Papel: traduzir o que o usuário faz na tela em chamadas ao SERVICE e
 * mostrar o resultado (ou o erro) na tela.
 *
 * Regras:
 *  - SEM SQL e SEM regra de negócio aqui.
 *  - Não fala com o repository, só com os services.
 *  - Captura ValidacaoException / NaoEncontradoException e mostra a mensagem
 *    num Alert (use a classe util.Alertas).
 *  - Pergunta "tem certeza?" antes de excluir.
 *
 * Os campos @FXML abaixo precisam ter EXATAMENTE o mesmo nome do fx:id no main.fxml.
 */
public class MainController {

    private final NotaService notaService;
    private final CategoriaService categoriaService;

    // --- Painel esquerdo: pesquisa, filtro e lista ---
    @FXML private TextField campoPesquisa;
    @FXML private ComboBox<Categoria> comboFiltroCategoria;
    @FXML private ListView<Nota> listaNotas;

    // --- Painel central: edição da nota ---
    @FXML private TextField campoTitulo;
    @FXML private ComboBox<Categoria> comboCategoria;
    @FXML private TextArea areaConteudo;

    public MainController(NotaService notaService, CategoriaService categoriaService) {
        this.notaService = notaService;
        this.categoriaService = categoriaService;
    }

    /** Chamado pelo JavaFX depois que o FXML carrega: preencher lista e combos. */
    @FXML
    private void initialize() {
        // TODO: carregar categorias nos combos e notas na lista.
        // TODO: ao selecionar uma nota na lista, mostrar os dados nos campos de edição.
    }

    /** Limpa os campos para começar uma nota nova. */
    @FXML
    private void aoNovaNota() {
        // TODO
    }

    /** Cria (se for nova) ou edita (se já existe) a nota, e atualiza a lista. */
    @FXML
    private void aoSalvar() {
        // TODO
    }

    /** Confirma com o usuário, exclui a nota selecionada e atualiza a lista. */
    @FXML
    private void aoExcluir() {
        // TODO
    }

    /** Pesquisa pelo termo digitado e mostra o resultado na lista. */
    @FXML
    private void aoPesquisar() {
        // TODO
    }

    /** Filtra a lista pela categoria escolhida no combo. */
    @FXML
    private void aoFiltrarCategoria() {
        // TODO
    }

    /** Abre a gestão de categorias (criar, renomear, excluir). */
    @FXML
    private void aoGerenciarCategorias() {
        // TODO
    }
}
