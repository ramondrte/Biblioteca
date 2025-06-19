import java.sql.*;
import java.util.ArrayList;

public class LivroDAO {
    private Connection conn;

    public LivroDAO() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:biblioteca.db");
        } catch (SQLException e) {
            System.out.println("Erro ao conectar com o banco: " + e.getMessage());
        }
    }

    public void adicionarLivro(Livro livro) {
        String sql = "INSERT INTO livros(titulo, autor, editora, ano, isbn, quantidade) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.setString(3, livro.getEditora());
            stmt.setInt(4, livro.getAno());
            stmt.setString(5, livro.getIsbn());
            stmt.setInt(6, livro.getQuantidade());
            stmt.executeUpdate();
            System.out.println("Livro adicionado com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao adicionar livro: " + e.getMessage());
        }
    }

    public Livro buscarPorTitulo(String titulo) {
        String sql = "SELECT * FROM livros WHERE titulo = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, titulo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Livro(
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("autor"),
                    rs.getString("editora"),
                    rs.getInt("ano"),
                    rs.getString("isbn"),
                    rs.getInt("quantidade")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar livro: " + e.getMessage());
        }
        return null;
    }
}