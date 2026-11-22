package br.nexo.database;

import br.nexo.util.Cores;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/*
Ponto único de acesso ao banco SQLite. Abre a conexão com o arquivo nexo.db, que fica
na pasta do usuário (~/.nexo), e cria essa pasta se ela ainda não existir. Em toda
conexão liga as chaves estrangeiras, que no SQLite vêm desligadas, para a nota só poder
apontar para uma categoria que existe. Também cria as tabelas de categorias e de notas
na primeira execução e atualiza bancos antigos, adicionando a coluna de cor e sorteando
uma cor para as categorias que ainda não tinham. Cada operação abre uma conexão nova, e
quem a recebe é responsável por fechar.
*/

@SuppressWarnings({"SqlResolve", "SqlNoDataSourceInspection"}) // avisos da IDE: ela não conhece o banco
public class Conexao {

    private static final Path ARQUIVO_BANCO =
            Paths.get(System.getProperty("user.home"), ".nexo", "nexo.db");

    /** Devolve uma conexão pronta para uso. Quem recebe é responsável por fechar. */
    public static Connection obter() throws SQLException {
        try {
            Files.createDirectories(ARQUIVO_BANCO.getParent());
        } catch (IOException e) {
            throw new SQLException("Não foi possível criar a pasta do banco de dados: "
                    + ARQUIVO_BANCO.getParent(), e);
        }

        Connection conexao = DriverManager.getConnection("jdbc:sqlite:" + ARQUIVO_BANCO);
        try (Statement st = conexao.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON");
        } catch (SQLException e) {
            conexao.close();
            throw e;
        }
        return conexao;
    }

    /** Cria as tabelas (notas e categorias) se ainda não existirem e atualiza bancos antigos. */
    public static void criarTabelas() {
        try (Connection conexao = obter(); Statement st = conexao.createStatement()) {
            st.execute("""
                    CREATE TABLE IF NOT EXISTS categorias (
                        id   INTEGER PRIMARY KEY AUTOINCREMENT,
                        nome TEXT NOT NULL,
                        cor  TEXT
                    )
                    """);

            st.execute("""
                    CREATE TABLE IF NOT EXISTS notas (
                        id           INTEGER PRIMARY KEY AUTOINCREMENT,
                        titulo       TEXT NOT NULL,
                        conteudo     TEXT NOT NULL DEFAULT '',
                        data_criacao TEXT NOT NULL,
                        data_edicao  TEXT NOT NULL,
                        categoria_id INTEGER,
                        FOREIGN KEY (categoria_id) REFERENCES categorias(id) ON DELETE RESTRICT
                    )
                    """);

            garantirCoresDasCategorias(conexao, st);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar as tabelas do banco de dados.", e);
        }
    }

    /**
     * Para bancos criados ANTES da cor existir: adiciona a coluna "cor" se faltar
     * e sorteia uma cor para cada categoria que ainda não tem.
     */
    private static void garantirCoresDasCategorias(Connection conexao, Statement st) throws SQLException {
        boolean temColunaCor = false;
        try (ResultSet rs = st.executeQuery("PRAGMA table_info(categorias)")) {
            while (rs.next()) {
                if ("cor".equalsIgnoreCase(rs.getString("name"))) {
                    temColunaCor = true;
                }
            }
        }
        if (!temColunaCor) {
            st.execute("ALTER TABLE categorias ADD COLUMN cor TEXT");
        }

        List<Integer> semCor = new ArrayList<>();
        try (ResultSet rs = st.executeQuery("SELECT id FROM categorias WHERE cor IS NULL")) {
            while (rs.next()) {
                semCor.add(rs.getInt("id"));
            }
        }

        if (!semCor.isEmpty()) {
            try (PreparedStatement ps = conexao.prepareStatement("UPDATE categorias SET cor = ? WHERE id = ?")) {
                for (int id : semCor) {
                    ps.setString(1, Cores.gerarAleatoria());
                    ps.setInt(2, id);
                    ps.executeUpdate();
                }
            }
        }
    }
}