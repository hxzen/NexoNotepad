package br.nexo.database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DATABASE: ponto único de acesso ao SQLite.
 *
 * DECISÕES:
 *  - O arquivo .db fica na pasta do usuário (~/.nexo/nexo.db), que existe em
 *    qualquer computador e não depende de onde o app foi executado.
 *  - Uma conexão NOVA por operação: quem chama obter() fecha com try-with-resources.
 *  - Chaves estrangeiras ligadas em TODA conexão (no SQLite vêm desligadas e
 *    o PRAGMA vale só para a conexão em que foi executado).
 */
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

    /** Cria as tabelas (notas e categorias) se ainda não existirem. */
    public static void criarTabelas() {
        try (Connection conexao = obter(); Statement st = conexao.createStatement()) {
            st.execute("""
                    CREATE TABLE IF NOT EXISTS categorias (
                        id   INTEGER PRIMARY KEY AUTOINCREMENT,
                        nome TEXT NOT NULL
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
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar as tabelas do banco de dados.", e);
        }
    }
}