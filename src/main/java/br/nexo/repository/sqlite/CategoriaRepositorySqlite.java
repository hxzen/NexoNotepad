package br.nexo.repository.sqlite;

import br.nexo.database.Conexao;
import br.nexo.model.Categoria;
import br.nexo.repository.CategoriaRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REPOSITORY SQLITE: mesma interface CategoriaRepository, guardando no banco via JDBC.
 *
 *  - Usa PreparedStatement (nunca concatena texto em SQL).
 *  - Fecha os recursos com try-with-resources.
 *  - Transforma falhas de banco em RuntimeException com mensagem compreensível.
 *  - NÃO valida nada (isso é do service).
 */
public class CategoriaRepositorySqlite implements CategoriaRepository {

    @Override
    public Categoria salvar(Categoria categoria) {
        String sql = "INSERT INTO categorias (nome) VALUES (?)";
        try (Connection conexao = Conexao.obter();
             PreparedStatement ps = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, categoria.getNome());
            ps.executeUpdate();

            try (ResultSet chaves = ps.getGeneratedKeys()) {
                if (chaves.next()) {
                    categoria.setId(chaves.getInt(1));
                }
            }
            return categoria;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar a categoria no banco de dados.", e);
        }
    }

    @Override
    public void atualizar(Categoria categoria) {
        String sql = "UPDATE categorias SET nome = ? WHERE id = ?";
        try (Connection conexao = Conexao.obter();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, categoria.getNome());
            ps.setInt(2, categoria.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar a categoria no banco de dados.", e);
        }
    }

    @Override
    public void excluir(int id) {
        String sql = "DELETE FROM categorias WHERE id = ?";
        try (Connection conexao = Conexao.obter();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir a categoria no banco de dados.", e);
        }
    }

    @Override
    public Optional<Categoria> buscarPorId(int id) {
        String sql = "SELECT id, nome FROM categorias WHERE id = ?";
        try (Connection conexao = Conexao.obter();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar a categoria no banco de dados.", e);
        }
    }

    @Override
    public Optional<Categoria> buscarPorNome(String nome) {
        if (nome == null) {
            return Optional.empty();
        }

        String sql = "SELECT id, nome FROM categorias WHERE nome = ? COLLATE NOCASE";
        try (Connection conexao = Conexao.obter();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, nome.trim());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar a categoria no banco de dados.", e);
        }
    }

    @Override
    public List<Categoria> listarTodas() {
        String sql = "SELECT id, nome FROM categorias ORDER BY id";
        List<Categoria> categorias = new ArrayList<>();
        try (Connection conexao = Conexao.obter();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                categorias.add(mapear(rs));
            }
            return categorias;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar as categorias do banco de dados.", e);
        }
    }

    /** Converte a linha do banco em objeto. */
    private Categoria mapear(ResultSet rs) throws SQLException {
        return new Categoria(rs.getString("nome"), rs.getInt("id"));
    }
}