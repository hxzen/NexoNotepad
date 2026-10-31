package br.nexo.database;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * DATABASE (FASE SQL, NÃO MEXER AGORA): ponto único de acesso ao SQLite.
 *
 * Quando chegar a hora:
 *  - Abrir a conexão com o arquivo .db (caminho que funcione em qualquer computador).
 *  - Criar as tabelas se não existirem (primeira execução).
 *  - Ativar as chaves estrangeiras (no SQLite vêm DESLIGADAS por padrão).
 *
 * Decisões de vocês: uma conexão compartilhada ou uma nova por operação? Quem fecha?
 */
public class Conexao {

    /** Devolve uma conexão pronta para uso. */
    public static Connection obter() throws SQLException {
        throw new UnsupportedOperationException("FASE SQL");
    }

    /** Cria as tabelas (notas e categorias) se ainda não existirem. */
    public static void criarTabelas() {
        throw new UnsupportedOperationException("FASE SQL");
    }
}
