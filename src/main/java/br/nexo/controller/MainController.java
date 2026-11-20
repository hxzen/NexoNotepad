package br.nexo.controller;

import br.nexo.App;
import br.nexo.exception.NaoEncontradoException;
import br.nexo.exception.ValidacaoException;
import br.nexo.model.Categoria;
import br.nexo.model.Nota;
import br.nexo.service.CategoriaService;
import br.nexo.service.NotaService;
import br.nexo.util.Alertas;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * Controller da tela principal. Traduz as ações do usuário em chamadas aos
 * services e mostra o resultado ou o erro. Não tem SQL nem regra de negócio.
 */
public class MainController {

    private final NotaService notaService;
    private final CategoriaService categoriaService;

    @FXML private TextField campoPesquisa;
    @FXML private ComboBox<Categoria> comboFiltroCategoria;
    @FXML private ListView<Nota> listaNotas;

    @FXML private TextField campoTitulo;
    @FXML private ComboBox<Categoria> comboCategoria;
    @FXML private TextArea areaConteudo;

    // Opções fixas dos combos. Não têm id, então nunca são salvas como categoria real.
    private final Categoria opcaoTodas = new Categoria("Todas as categorias");
    private final Categoria opcaoSemCategoria = new Categoria("Sem categoria");

    // Nota que está no formulário. Se for null, o botão Salvar cria uma nota nova.
    private Nota notaEmEdicao;

    // Evita que mudanças feitas pelo próprio código disparem o filtro como se fossem do usuário.
    private boolean atualizandoTela;

    public MainController(NotaService notaService, CategoriaService categoriaService) {
        this.notaService = notaService;
        this.categoriaService = categoriaService;
    }

    @FXML
    private void initialize() {
        carregarCategorias();
        mostrarTodasAsNotas();

        // Ao clicar numa nota da lista, os dados dela vão para o formulário.
        listaNotas.getSelectionModel().selectedItemProperty().addListener(
                (observavel, anterior, selecionada) -> {
                    if (selecionada != null) {
                        preencherFormulario(selecionada);
                    }
                });

        campoPesquisa.setOnAction(evento -> aoPesquisar());
    }

    @FXML
    private void aoNovaNota() {
        notaEmEdicao = null;
        listaNotas.getSelectionModel().clearSelection();
        campoTitulo.clear();
        areaConteudo.clear();
        comboCategoria.setValue(opcaoSemCategoria);
        campoTitulo.requestFocus();
    }

    @FXML
    private void aoSalvar() {
        try {
            String titulo = campoTitulo.getText();
            String conteudo = areaConteudo.getText();
            Categoria categoria = categoriaOuNull(comboCategoria.getValue());

            Nota salva;
            if (notaEmEdicao == null) {
                salva = notaService.criar(titulo, conteudo, categoria);
            } else {
                salva = notaService.editar(notaEmEdicao.getId(), titulo, conteudo, categoria);
            }

            mostrarTodasAsNotas();
            listaNotas.getSelectionModel().select(salva);
        } catch (ValidacaoException | NaoEncontradoException e) {
            Alertas.erro(e.getMessage());
        }
    }

    @FXML
    private void aoExcluir() {
        Nota selecionada = listaNotas.getSelectionModel().getSelectedItem();
        if (selecionada == null) {
            Alertas.info("Selecione uma nota para excluir.");
            return;
        }
        if (!Alertas.confirmar("Excluir a nota \"" + selecionada.getTitulo() + "\"?")) {
            return;
        }

        try {
            notaService.excluir(selecionada.getId());
            aoNovaNota();
            mostrarTodasAsNotas();
        } catch (NaoEncontradoException e) {
            Alertas.erro(e.getMessage());
        }
    }

    @FXML
    private void aoPesquisar() {
        atualizandoTela = true;
        comboFiltroCategoria.setValue(opcaoTodas);
        atualizandoTela = false;

        listaNotas.getItems().setAll(notaService.pesquisar(campoPesquisa.getText()));
    }

    @FXML
    private void aoFiltrarCategoria() {
        if (atualizandoTela) {
            return;
        }
        campoPesquisa.clear();
        // A opção "Todas" não tem id, e o service responde a isso devolvendo todas as notas.
        listaNotas.getItems().setAll(notaService.listarPorCategoria(comboFiltroCategoria.getValue()));
    }

    @FXML
    private void aoGerenciarCategorias() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("view/categorias.fxml"));
            loader.setControllerFactory(tipo -> new CategoriasController(categoriaService));

            Scene cena = new Scene(loader.load());
            cena.getStylesheets().add(App.class.getResource("css/estilo.css").toExternalForm());

            Stage janela = new Stage();
            janela.setTitle("Categorias");
            janela.initModality(Modality.APPLICATION_MODAL);
            janela.setScene(cena);
            janela.showAndWait();

            // Ao fechar, as categorias podem ter mudado: recarrega os combos.
            carregarCategorias();
            mostrarTodasAsNotas();
        } catch (IOException e) {
            Alertas.erro("Não foi possível abrir a tela de categorias.");
        }
    }

    private void preencherFormulario(Nota nota) {
        notaEmEdicao = nota;
        campoTitulo.setText(nota.getTitulo());
        areaConteudo.setText(nota.getConteudo());

        Categoria categoria = nota.getCategoria();
        comboCategoria.setValue(categoria != null ? categoria : opcaoSemCategoria);
    }

    private void carregarCategorias() {
        List<Categoria> categorias = categoriaService.listarTodas();

        atualizandoTela = true;
        comboFiltroCategoria.getItems().setAll(opcaoTodas);
        comboFiltroCategoria.getItems().addAll(categorias);
        comboFiltroCategoria.setValue(opcaoTodas);

        comboCategoria.getItems().setAll(opcaoSemCategoria);
        comboCategoria.getItems().addAll(categorias);
        if (notaEmEdicao != null && notaEmEdicao.getCategoria() != null) {
            comboCategoria.setValue(notaEmEdicao.getCategoria());
        } else {
            comboCategoria.setValue(opcaoSemCategoria);
        }
        atualizandoTela = false;
    }

    /** Volta a lista ao estado normal: sem pesquisa e sem filtro. */
    private void mostrarTodasAsNotas() {
        campoPesquisa.clear();

        atualizandoTela = true;
        comboFiltroCategoria.setValue(opcaoTodas);
        atualizandoTela = false;

        listaNotas.getItems().setAll(notaService.listarTodas());
    }

    /** "Sem categoria" vira null, porque o NotaService só aceita null ou uma categoria já salva. */
    private Categoria categoriaOuNull(Categoria escolhida) {
        if (escolhida == null || escolhida.getId() == null) {
            return null;
        }
        return escolhida;
    }
}