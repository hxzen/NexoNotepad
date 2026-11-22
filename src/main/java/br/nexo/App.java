package br.nexo;

import br.nexo.controller.MainController;
import br.nexo.database.Conexao;
import br.nexo.repository.CategoriaRepository;
import br.nexo.repository.NotaRepository;
import br.nexo.repository.sqlite.CategoriaRepositorySqlite;
import br.nexo.repository.sqlite.NotaRepositorySqlite;
import br.nexo.service.CategoriaService;
import br.nexo.service.NotaService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * PONTO DE ENTRADA e ÚNICO lugar que conhece as implementações concretas.
 *
 * O que ela faz:
 *  0. Garante que o banco e as tabelas existem (Conexao.criarTabelas()).
 *  1. Cria os repositories (hoje em SQLITE; para voltar à memória, troque só estas linhas).
 *  2. Cria os services, entregando a eles os repositories.
 *  3. Carrega o main.fxml e entrega os services ao MainController (controller factory).
 *  4. Aplica o CSS e mostra a janela.
 *
 * Regra: nenhuma regra de negócio aqui. Só "montar as peças".
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // 0. Banco: precisa vir ANTES de tudo, porque o MainController.initialize()
        //    já consulta as categorias durante o loader.load().
        Conexao.criarTabelas();

        // 1. Repositories (TROCAR AQUI para voltar à memória)
        NotaRepository notaRepository = new NotaRepositorySqlite();
        CategoriaRepository categoriaRepository = new CategoriaRepositorySqlite();

        // 2. Services
        NotaService notaService = new NotaService(notaRepository, categoriaRepository);
        CategoriaService categoriaService = new CategoriaService(categoriaRepository, notaRepository);

        // 3. Tela + controller
        FXMLLoader loader = new FXMLLoader(App.class.getResource("view/main.fxml"));
        loader.setControllerFactory(tipo -> new MainController(notaService, categoriaService));

        // 4. Cena
        Scene scene = new Scene(loader.load(), 960, 600);
        scene.getStylesheets().add(App.class.getResource("css/estilo.css").toExternalForm());

        stage.setTitle("Nexo Notepad");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}