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

/*
Ponto de entrada do programa e o único lugar que conhece as implementações concretas.
Não tem regra de negócio, só monta as peças na ordem certa e abre a janela:

1. Chama Conexao.criarTabelas() para garantir que o banco e as tabelas existem. Precisa
vir primeiro, porque a tela já consulta as categorias assim que carrega.

2. Cria os repositories, hoje os do SQLite. Para voltar à versão em memória, basta
trocar essas duas linhas, porque as duas implementam a mesma interface.

3. Cria os services, entregando a cada um os repositories de que precisa.

4. Carrega o main.fxml e entrega os services ao MainController por meio de um controller
factory, já que o controller recebe os services pelo construtor.

5. Aplica o CSS (estilo.css), define o título e mostra a janela.

A classe estende Application, que é a base de todo programa JavaFX. O metodo start monta
e abre a janela, e o main só chama o launch, que inicia o JavaFX.
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