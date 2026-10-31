package br.nexo;

import br.nexo.controller.MainController;
import br.nexo.repository.CategoriaRepository;
import br.nexo.repository.NotaRepository;
import br.nexo.repository.memoria.CategoriaRepositoryMemoria;
import br.nexo.repository.memoria.NotaRepositoryMemoria;
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
 *  1. Cria os repositories (hoje em MEMÓRIA; na fase SQL troca-se só estas linhas).
 *  2. Cria os services, entregando a eles os repositories.
 *  3. Carrega o main.fxml e entrega os services ao MainController (controller factory).
 *  4. Aplica o CSS e mostra a janela.
 *
 * Regra: nenhuma regra de negócio aqui. Só "montar as peças".
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // 1. Repositories (TROCAR AQUI na fase SQL)
        NotaRepository notaRepository = new NotaRepositoryMemoria();
        CategoriaRepository categoriaRepository = new CategoriaRepositoryMemoria();

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
