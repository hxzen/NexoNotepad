package br.nexo.repository.sqlite;

import br.nexo.database.Conexao;
import br.nexo.model.Categoria;
import br.nexo.model.Nota;
import br.nexo.repository.NotaRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REPOSITORY SQLITE: mesma interface NotaRepository, guardando no banco via JDBC (Conexao).
 *
 *  - Converte a linha do banco em objeto (mapear) e o objeto em linha (parâmetros do SQL).
 *  - Usa PreparedStatement (nunca concatena texto em SQL).
 *  - Fecha os recursos com try-with-resources.
 *  - Transforma falhas de banco em RuntimeException com mensagem compreensível.
 *  - NÃO valida nada (isso é do service).
 *
 * As datas são guardadas como texto ISO-8601 (ex.: 2026-11-21T14:30:00).
 */
public class NotaRepositorySqlite implements NotaRepository {

    /** Consulta base: a nota + a categoria dela (LEFT JOIN, pois a categoria é opcional). */
    private static final String SELECT_BASE = """
            SELECT n.id, n.titulo, n.conteudo, n.data_criacao, n.data_edicao,
                   c.id AS categoria_id, c.nome AS categoria_nome
            FROM notas n
            LEFT JOIN categorias c ON c.id = n.categoria_id
            """;

    /** Preenche os parâmetros (?) de um PreparedStatement. */
    @FunctionalInterface
    private interface Parametros {
        void aplicar(PreparedStatement ps) throws SQLException;
    }

    @Override
    public Nota salvar(Nota nota) {
        String sql = "INSERT INTO notas (titulo, conteudo, data_criacao, data_edicao, categoria_id) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conexao = Conexao.obter();
             PreparedStatement ps = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nota.getTitulo());
            ps.setString(2, nota.getConteudo());
            ps.setString(3, nota.getDataCriacao().toString());
            ps.setString(4, nota.getDataEdicao().toString());
            definirCategoria(ps, 5, nota.getCategoria());
            ps.executeUpdate();

            try (ResultSet chaves = ps.getGeneratedKeys()) {
                if (chaves.next()) {
                    nota.setId(chaves.getInt(1));
                }
            }
            return nota;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar a nota no banco de dados.", e);
        }
    }

    @Override
    public void atualizar(Nota nota) {
        String sql = "UPDATE notas SET titulo = ?, conteudo = ?, data_edicao = ?, categoria_id = ? "
                + "WHERE id = ?";
        try (Connection conexao = Conexao.obter();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, nota.getTitulo());
            ps.setString(2, nota.getConteudo());
            ps.setString(3, nota.getDataEdicao().toString());
            definirCategoria(ps, 4, nota.getCategoria());
            ps.setInt(5, nota.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar a nota no banco de dados.", e);
        }
    }

    @Override
    public void excluir(int id) {
        String sql = "DELETE FROM notas WHERE id = ?";
        try (Connection conexao = Conexao.obter();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir a nota no banco de dados.", e);
        }
    }

    @Override
    public Optional<Nota> buscarPorId(int id) {
        List<Nota> resultado = consultar(SELECT_BASE + " WHERE n.id = ?", ps -> ps.setInt(1, id));
        return resultado.isEmpty() ? Optional.empty() : Optional.of(resultado.get(0));
    }

    @Override
    public List<Nota> listarTodas() {
        return consultar(SELECT_BASE + " ORDER BY n.id", ps -> { });
    }

    @Override
    public List<Nota> buscarPorTermo(String termo) {
        if (termo == null) {
            return new ArrayList<>();
        }

        // Escapa os curingas do LIKE para o usuário poder pesquisar "50%" ou "a_b" literalmente.
        String escapado = termo.trim().toLowerCase()
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
        String padrao = "%" + escapado + "%";

        String sql = SELECT_BASE
                + " WHERE LOWER(n.titulo) LIKE ? ESCAPE '\\' OR LOWER(n.conteudo) LIKE ? ESCAPE '\\'"
                + " ORDER BY n.id";
        return consultar(sql, ps -> {
            ps.setString(1, padrao);
            ps.setString(2, padrao);
        });
    }

    @Override
    public List<Nota> listarPorCategoria(int categoriaId) {
        return consultar(SELECT_BASE + " WHERE n.categoria_id = ? ORDER BY n.id",
                ps -> ps.setInt(1, categoriaId));
    }

    @Override
    public int contarPorCategoria(int categoriaId) {
        String sql = "SELECT COUNT(*) FROM notas WHERE categoria_id = ?";
        try (Connection conexao = Conexao.obter();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, categoriaId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar as notas da categoria no banco de dados.", e);
        }
    }

    // ------------------------------------------------------------------
    // Auxiliares
    // ------------------------------------------------------------------

    /** Executa um SELECT e converte cada linha em Nota. */
    private List<Nota> consultar(String sql, Parametros parametros) {
        List<Nota> notas = new ArrayList<>();
        try (Connection conexao = Conexao.obter();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            parametros.aplicar(ps);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    notas.add(mapear(rs));
                }
            }
            return notas;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar as notas no banco de dados.", e);
        }
    }

    /** Converte a linha do banco em objeto Nota (com a Categoria, se houver). */
    private Nota mapear(ResultSet rs) throws SQLException {
        Categoria categoria = null;
        int categoriaId = rs.getInt("categoria_id");
        if (!rs.wasNull()) {
            categoria = new Categoria(rs.getString("categoria_nome"), categoriaId);
        }

        return new Nota(
                rs.getInt("id"),
                rs.getString("titulo"),
                rs.getString("conteudo"),
                LocalDateTime.parse(rs.getString("data_criacao")),
                LocalDateTime.parse(rs.getString("data_edicao")),
                categoria);
    }

    /** Grava o id da categoria, ou NULL quando a nota não tem categoria. */
    private void definirCategoria(PreparedStatement ps, int posicao, Categoria categoria) throws SQLException {
        if (categoria == null || categoria.getId() == null) {
            ps.setNull(posicao, Types.INTEGER);
        } else {
            ps.setInt(posicao, categoria.getId());
        }
    }
}