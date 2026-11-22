package br.nexo.controller;

import br.nexo.exception.NaoEncontradoException;
import br.nexo.exception.ValidacaoException;
import br.nexo.model.Categoria;
import br.nexo.model.Nota;
import br.nexo.service.CategoriaService;
import br.nexo.service.NotaService;
import br.nexo.util.Alertas;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    // Nota que está aberta no painel de edição (null = nota nova).
    private Nota notaSelecionada;

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // --- Painel esquerdo: pesquisa, filtro e lista ---
    @FXML private TextField campoPesquisa;
    @FXML private ComboBox<Categoria> comboFiltroCategoria;
    @FXML private ListView<Nota> listaNotas;

    // --- Painel central: edição da nota ---
    @FXML private TextField campoTitulo;
    @FXML private ComboBox<Categoria> comboCategoria;
    @FXML private TextArea areaConteudo;
    @FXML private Label labelDatas; // opcional: mostra datas de criação/edição da nota aberta

    public MainController(NotaService notaService, CategoriaService categoriaService) {
        this.notaService = notaService;
        this.categoriaService = categoriaService;
    }

    /** Chamado pelo JavaFX depois que o FXML carrega: preencher lista e combos. */
    @FXML
    private void initialize() {
        // O combo de filtro tem um item null que aparece como "Todas as categorias".
        comboFiltroCategoria.setCellFactory(lv -> criarCelulaCategoria("Todas as categorias"));
        comboFiltroCategoria.setButtonCell(criarCelulaCategoria("Todas as categorias"));

        // O combo da nota tem um item null que aparece como "Sem categoria".
        comboCategoria.setCellFactory(lv -> criarCelulaCategoria("Sem categoria"));
        comboCategoria.setButtonCell(criarCelulaCategoria("Sem categoria"));

        carregarCategorias();
        recarregarNotas();

        // Cada item da lista mostra título, categoria (etiqueta colorida) e datas.
        listaNotas.setCellFactory(lv -> criarCelulaNota());

        // Ao selecionar uma nota na lista, mostra os dados nos campos de edição.
        listaNotas.getSelectionModel().selectedItemProperty().addListener((obs, antiga, nova) -> {
            if (nova != null) {
                mostrarNota(nova);
            }
        });
    }

    /** Limpa os campos para começar uma nota nova. */
    @FXML
    private void aoNovaNota() {
        notaSelecionada = null;
        listaNotas.getSelectionModel().clearSelection();
        campoTitulo.clear();
        areaConteudo.clear();
        mostrarDatas(null);
        comboCategoria.getSelectionModel().selectFirst(); // "Sem categoria"
        campoTitulo.requestFocus();
    }

    /** Cria (se for nova) ou edita (se já existe) a nota, e atualiza a lista. */
    @FXML
    private void aoSalvar() {
        String titulo = campoTitulo.getText();
        String conteudo = areaConteudo.getText();
        Categoria categoria = comboCategoria.getValue(); // null = sem categoria

        try {
            Nota salva;
            if (notaSelecionada == null) {
                salva = notaService.criar(titulo, conteudo, categoria);
            } else {
                salva = notaService.editar(notaSelecionada.getId(), titulo, conteudo, categoria);
            }

            recarregarNotas();
            selecionarNotaPorId(salva.getId());
        } catch (ValidacaoException | NaoEncontradoException e) {
            Alertas.erro(e.getMessage());
        }
    }

    /** Confirma com o usuário, exclui a nota selecionada e atualiza a lista. */
    @FXML
    private void aoExcluir() {
        if (notaSelecionada == null) {
            Alertas.info("Selecione uma nota para excluir.");
            return;
        }

        boolean confirmou = Alertas.confirmar(
                "Tem certeza que deseja excluir a nota \"" + notaSelecionada.getTitulo() + "\"?");
        if (!confirmou) {
            return;
        }

        try {
            notaService.excluir(notaSelecionada.getId());
            recarregarNotas();
            aoNovaNota();
        } catch (ValidacaoException | NaoEncontradoException e) {
            Alertas.erro(e.getMessage());
        }
    }

    /** Pesquisa pelo termo digitado e mostra o resultado na lista. */
    @FXML
    private void aoPesquisar() {
        recarregarNotas();
    }

    /** Filtra a lista pela categoria escolhida no combo. */
    @FXML
    private void aoFiltrarCategoria() {
        recarregarNotas();
    }

    /** Abre a gestão de categorias (criar, renomear, excluir). */
    @FXML
    private void aoGerenciarCategorias() {
        Dialog<Void> dialogo = new Dialog<>();
        dialogo.setTitle("Categorias");
        dialogo.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        ListView<Categoria> lista = new ListView<>(
                FXCollections.observableArrayList(categoriaService.listarTodas()));
        lista.setPrefHeight(220);

        // Atualiza a lista do diálogo depois de cada mudança.
        Runnable recarregarLista = () ->
                lista.setItems(FXCollections.observableArrayList(categoriaService.listarTodas()));

        Button botaoNova = new Button("Nova");
        Button botaoRenomear = new Button("Renomear");
        Button botaoExcluir = new Button("Excluir");

        botaoNova.setOnAction(e ->
                pedirNome("Nova categoria", "").ifPresent(nome -> {
                    if (executar(() -> categoriaService.criar(nome))) {
                        recarregarLista.run();
                    }
                }));

        botaoRenomear.setOnAction(e -> {
            Categoria escolhida = lista.getSelectionModel().getSelectedItem();
            if (escolhida == null) {
                Alertas.info("Selecione uma categoria para renomear.");
                return;
            }
            pedirNome("Renomear categoria", escolhida.getNome()).ifPresent(nome -> {
                if (executar(() -> categoriaService.renomear(escolhida.getId(), nome))) {
                    recarregarLista.run();
                }
            });
        });

        botaoExcluir.setOnAction(e -> {
            Categoria escolhida = lista.getSelectionModel().getSelectedItem();
            if (escolhida == null) {
                Alertas.info("Selecione uma categoria para excluir.");
                return;
            }
            boolean confirmou = Alertas.confirmar(
                    "Tem certeza que deseja excluir a categoria \"" + escolhida.getNome() + "\"?");
            if (confirmou && executar(() -> categoriaService.excluir(escolhida.getId()))) {
                recarregarLista.run();
            }
        });

        HBox botoes = new HBox(8, botaoNova, botaoRenomear, botaoExcluir);
        dialogo.getDialogPane().setContent(new VBox(8, lista, botoes));
        dialogo.showAndWait();

        // Ao fechar, atualiza os combos e a lista de notas com as mudanças.
        carregarCategorias();
        recarregarNotas();
    }

    // ------------------------------------------------------------------
    // Métodos auxiliares (só cuidam da tela)
    // ------------------------------------------------------------------

    /** Preenche os dois combos com as categorias, mantendo a seleção atual quando possível. */
    private void carregarCategorias() {
        Categoria filtroAnterior = comboFiltroCategoria.getValue();
        Categoria categoriaAnterior = comboCategoria.getValue();

        List<Categoria> categorias = categoriaService.listarTodas();

        // Item null no começo = "Todas as categorias" / "Sem categoria".
        List<Categoria> comNulo = new ArrayList<>();
        comNulo.add(null);
        comNulo.addAll(categorias);

        comboFiltroCategoria.setItems(FXCollections.observableArrayList(comNulo));
        comboCategoria.setItems(FXCollections.observableArrayList(comNulo));

        selecionarOuPrimeiro(comboFiltroCategoria, filtroAnterior);
        selecionarOuPrimeiro(comboCategoria, categoriaAnterior);
    }

    /** Seleciona o item se ele ainda existir no combo; senão, o primeiro (o null). */
    private void selecionarOuPrimeiro(ComboBox<Categoria> combo, Categoria desejada) {
        if (desejada != null && combo.getItems().contains(desejada)) {
            combo.getSelectionModel().select(desejada);
        } else {
            combo.getSelectionModel().selectFirst();
        }
    }

    /**
     * Recarrega a lista de notas: se tem texto na pesquisa, pesquisa;
     * senão, se tem categoria no filtro, filtra; senão, mostra todas.
     */
    private void recarregarNotas() {
        String termo = campoPesquisa.getText();
        Categoria filtro = comboFiltroCategoria.getValue();

        List<Nota> notas;
        if (termo != null && !termo.trim().isEmpty()) {
            notas = notaService.pesquisar(termo.trim());
        } else if (filtro != null) {
            notas = notaService.listarPorCategoria(filtro);
        } else {
            notas = notaService.listarTodas();
        }

        listaNotas.setItems(FXCollections.observableArrayList(notas));
    }

    /** Mostra os dados da nota nos campos de edição. */
    private void mostrarNota(Nota nota) {
        notaSelecionada = nota;
        campoTitulo.setText(nota.getTitulo());
        areaConteudo.setText(nota.getConteudo());
        mostrarDatas(nota);

        selecionarOuPrimeiro(comboCategoria, nota.getCategoria());
    }

    /** Seleciona na lista a nota com este id (se ela estiver na lista atual). */
    private void selecionarNotaPorId(int id) {
        for (Nota n : listaNotas.getItems()) {
            if (n.getId() == id) {
                listaNotas.getSelectionModel().select(n);
                return;
            }
        }
    }

    /** Célula de combo que mostra um texto especial quando o item é null. */
    private ListCell<Categoria> criarCelulaCategoria(String textoParaNulo) {
        return new ListCell<>() {
            @Override
            protected void updateItem(Categoria item, boolean vazio) {
                super.updateItem(item, vazio);
                if (vazio) {
                    setText(null);
                } else {
                    setText(item == null ? textoParaNulo : item.getNome());
                }
            }
        };
    }

    /** Célula da lista: título em negrito, etiqueta da categoria e datas de criação/edição. */
    private ListCell<Nota> criarCelulaNota() {
        return new ListCell<>() {
            @Override
            protected void updateItem(Nota nota, boolean vazio) {
                super.updateItem(nota, vazio);
                if (vazio || nota == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                }

                Label titulo = new Label(nota.getTitulo());
                titulo.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

                Label datas = new Label("Criada: " + formatarData(nota.getDataCriacao())
                        + "  •  Editada: " + formatarData(nota.getDataEdicao()));
                datas.setStyle("-fx-font-size: 11px; -fx-opacity: 0.7;");

                setText(null);
                // Linha de cima: título + etiqueta da categoria lado a lado.
                HBox linhaTitulo = new HBox(8, titulo, criarEtiquetaCategoria(nota.getCategoria()));
                linhaTitulo.setAlignment(Pos.CENTER_LEFT);

                setGraphic(new VBox(3, linhaTitulo, datas));
            }
        };
    }

    /** Etiqueta arredondada com o nome da categoria (cinza quando não tem categoria). */
    private Label criarEtiquetaCategoria(Categoria categoria) {
        Label etiqueta;
        String cor;
        if (categoria == null) {
            etiqueta = new Label("Sem categoria");
            cor = "#9E9E9E";
        } else {
            etiqueta = new Label(categoria.getNome());
            cor = corDaCategoria(categoria);
        }

        etiqueta.setStyle("-fx-background-color: " + cor + "; -fx-text-fill: white;"
                + " -fx-background-radius: 10; -fx-padding: 1 8 1 8; -fx-font-size: 11px;");
        return etiqueta;
    }

    /** Cada categoria ganha sempre a mesma cor, calculada a partir do id dela. */
    private String corDaCategoria(Categoria categoria) {
        long base = categoria.getId() != null ? categoria.getId() : categoria.getNome().hashCode();
        double matiz = Math.abs(base * 47L) % 360;
        Color cor = Color.hsb(matiz, 0.60, 0.70);
        return String.format("#%02X%02X%02X",
                Math.round(cor.getRed() * 255),
                Math.round(cor.getGreen() * 255),
                Math.round(cor.getBlue() * 255));
    }

    /** Mostra "Criada em ... • Editada em ..." no painel de edição (se o label existir no FXML). */
    private void mostrarDatas(Nota nota) {
        if (labelDatas == null) {
            return;
        }
        labelDatas.setText(nota == null ? ""
                : "Criada em " + formatarData(nota.getDataCriacao())
                + "  •  Editada em " + formatarData(nota.getDataEdicao()));
    }

    private String formatarData(LocalDateTime data) {
        return data == null ? "—" : data.format(FORMATO_DATA);
    }

    /** Abre uma caixinha pedindo um nome ao usuário. */
    private Optional<String> pedirNome(String titulo, String valorInicial) {
        TextInputDialog dialogo = new TextInputDialog(valorInicial);
        dialogo.setTitle(titulo);
        dialogo.setHeaderText(titulo);
        dialogo.setContentText("Nome:");
        return dialogo.showAndWait();
    }

    /**
     * Executa uma ação do service e mostra a mensagem de erro, se houver.
     * Devolve true se deu certo.
     */
    private boolean executar(Runnable acao) {
        try {
            acao.run();
            return true;
        } catch (ValidacaoException | NaoEncontradoException e) {
            Alertas.erro(e.getMessage());
            return false;
        }
    }
}