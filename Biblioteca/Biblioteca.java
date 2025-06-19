import java.sql.*;
import java.util.Scanner;

public class Biblioteca {

    private Connection conn;

    public Biblioteca() {
        try {
            // Conecta (ou cria) o banco de dados biblioteca.db
            conn = DriverManager.getConnection("jdbc:sqlite:biblioteca.db");
            criarTabelasSeNaoExistirem();
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco: " + e.getMessage());
        }
    }
    private void criarTabelasSeNaoExistirem() throws SQLException {
        Statement stmt = conn.createStatement();

        // Usuários
        stmt.execute("CREATE TABLE IF NOT EXISTS usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT NOT NULL," +
                "email TEXT UNIQUE NOT NULL," +
                "telefone TEXT)");

        // Funcionários
        stmt.execute("CREATE TABLE IF NOT EXISTS funcionarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT NOT NULL," +
                "email TEXT UNIQUE NOT NULL," +
                "telefone TEXT," +
                "cargo TEXT)");

        // Livros
        stmt.execute("CREATE TABLE IF NOT EXISTS livros (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "titulo TEXT NOT NULL," +
                "autor TEXT NOT NULL," +
                "editora TEXT," +
                "ano INTEGER," +
                "isbn TEXT UNIQUE," +
                "quantidade INTEGER NOT NULL)");

        // Empréstimos
        stmt.execute("CREATE TABLE IF NOT EXISTS emprestimos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "usuario_id INTEGER NOT NULL," +
                "livro_id INTEGER NOT NULL," +
                "data_emprestimo TEXT," +
                "data_devolucao TEXT," +
                "devolvido INTEGER DEFAULT 0," + // 0 = não, 1 = sim
                "FOREIGN KEY(usuario_id) REFERENCES usuarios(id)," +
                "FOREIGN KEY(livro_id) REFERENCES livros(id))");

        stmt.close();
    }

    // ----------- MÉTODOS DE CADASTRO -----------

    public void menuCadastrarUsuario(Scanner scanner) {
        try {
            System.out.print("Nome: ");
            String nome = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Telefone: ");
            String telefone = scanner.nextLine();

            String sql = "INSERT INTO usuarios (nome, email, telefone) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nome);
            ps.setString(2, email);
            ps.setString(3, telefone);

            ps.executeUpdate();
            ps.close();

            System.out.println("Usuário cadastrado com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }

    public void menuCadastrarFuncionario(Scanner scanner) {
        try {
            System.out.print("Nome: ");
            String nome = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Telefone: ");
            String telefone = scanner.nextLine();
            System.out.print("Cargo: ");
            String cargo = scanner.nextLine();

            String sql = "INSERT INTO funcionarios (nome, email, telefone, cargo) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nome);
            ps.setString(2, email);
            ps.setString(3, telefone);
            ps.setString(4, cargo);

            ps.executeUpdate();
            ps.close();

            System.out.println("Funcionário cadastrado com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar funcionário: " + e.getMessage());
        }
    }

    public void menuCadastrarLivro(Scanner scanner) {
        try {
            System.out.print("Título: ");
            String titulo = scanner.nextLine();
            System.out.print("Autor: ");
            String autor = scanner.nextLine();
            System.out.print("Editora: ");
            String editora = scanner.nextLine();
            System.out.print("Ano: ");
            int ano = Integer.parseInt(scanner.nextLine());
            System.out.print("ISBN (número de chamada): ");
            String isbn = scanner.nextLine();
            System.out.print("Quantidade: ");
            int quantidade = Integer.parseInt(scanner.nextLine());

            String sql = "INSERT INTO livros (titulo, autor, editora, ano, isbn, quantidade) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, titulo);
            ps.setString(2, autor);
            ps.setString(3, editora);
            ps.setInt(4, ano);
            ps.setString(5, isbn);
            ps.setInt(6, quantidade);

            ps.executeUpdate();
            ps.close();

            System.out.println("Livro cadastrado com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar livro: " + e.getMessage());
        }
    }

    // ----------- EMPRÉSTIMO -----------

    public void menuEmprestimo(Scanner scanner) {
        try {
            System.out.print("Email do usuário: ");
            String email = scanner.nextLine();

            Integer usuarioId = buscarUsuarioIdPorEmail(email);
            if (usuarioId == null) {
                System.out.println("Usuário não encontrado.");
                return;
            }

            System.out.print("Título do livro: ");
            String titulo = scanner.nextLine();

            Livro livro = buscarLivroPorTitulo(titulo);
            if (livro == null) {
                System.out.println("Livro não encontrado.");
                return;
            }

            if (livro.getQuantidade() <= 0) {
                System.out.println("Livro indisponível para empréstimo.");
                return;
            }

            // Inserir empréstimo
            String sqlInsert = "INSERT INTO emprestimos (usuario_id, livro_id, data_emprestimo, devolvido) VALUES (?, ?, datetime('now'), 0)";
            PreparedStatement psInsert = conn.prepareStatement(sqlInsert);
            psInsert.setInt(1, usuarioId);
            psInsert.setInt(2, livro.getId());
            psInsert.executeUpdate();
            psInsert.close();

            // Atualizar quantidade do livro
            String sqlUpdate = "UPDATE livros SET quantidade = quantidade - 1 WHERE id = ?";
            PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);
            psUpdate.setInt(1, livro.getId());
            psUpdate.executeUpdate();
            psUpdate.close();

            System.out.println("Empréstimo realizado com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao realizar empréstimo: " + e.getMessage());
        }
    }

    // ----------- DEVOLUÇÃO -----------

    public void menuDevolucao(Scanner scanner) {
        try {
            System.out.print("Email do usuário: ");
            String email = scanner.nextLine();

            Integer usuarioId = buscarUsuarioIdPorEmail(email);
            if (usuarioId == null) {
                System.out.println("Usuário não encontrado.");
                return;
            }

            System.out.print("Título do livro: ");
            String titulo = scanner.nextLine();

            Livro livro = buscarLivroPorTitulo(titulo);
            if (livro == null) {
                System.out.println("Livro não encontrado.");
                return;
            }

            // Verificar empréstimo ativo
            String sqlSelect = "SELECT id FROM emprestimos WHERE usuario_id = ? AND livro_id = ? AND devolvido = 0";
            PreparedStatement psSelect = conn.prepareStatement(sqlSelect);
            psSelect.setInt(1, usuarioId);
            psSelect.setInt(2, livro.getId());
            ResultSet rs = psSelect.executeQuery();

            if (rs.next()) {
                int emprestimoId = rs.getInt("id");
                rs.close();
                psSelect.close();

                // Atualizar devolução
                String sqlUpdateEmp = "UPDATE emprestimos SET devolvido = 1, data_devolucao = datetime('now') WHERE id = ?";
                PreparedStatement psUpdateEmp = conn.prepareStatement(sqlUpdateEmp);
                psUpdateEmp.setInt(1, emprestimoId);
                psUpdateEmp.executeUpdate();
                psUpdateEmp.close();

                // Atualizar quantidade do livro
                String sqlUpdateLivro = "UPDATE livros SET quantidade = quantidade + 1 WHERE id = ?";
                PreparedStatement psUpdateLivro = conn.prepareStatement(sqlUpdateLivro);
                psUpdateLivro.setInt(1, livro.getId());
                psUpdateLivro.executeUpdate();
                psUpdateLivro.close();

                System.out.println("Livro devolvido com sucesso!");
            } else {
                rs.close();
                psSelect.close();
                System.out.println("Não há empréstimos ativos desse livro para esse usuário.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao realizar devolução: " + e.getMessage());
        }
    }

    // ----------- CONSULTAS -----------

    public void menuConsultaTitulo(Scanner scanner) {
        System.out.print("Digite o título: ");
        String titulo = scanner.nextLine();

        Livro livro = buscarLivroPorTitulo(titulo);
        exibirInfoLivro(livro);
    }

    public void menuConsultaAutor(Scanner scanner) {
        System.out.print("Digite o autor: ");
        String autor = scanner.nextLine();

        try {
            String sql = "SELECT * FROM livros WHERE autor LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + autor + "%");
            ResultSet rs = ps.executeQuery();

            boolean achou = false;
            while (rs.next()) {
                achou = true;
                exibirLivroDoResultSet(rs);
                System.out.println("--------------------");
            }
            if (!achou) System.out.println("Nenhum livro encontrado para esse autor.");

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Erro na consulta por autor: " + e.getMessage());
        }
    }

    public void menuConsultaEditora(Scanner scanner) {
        System.out.print("Digite a editora: ");
        String editora = scanner.nextLine();

        try {
            String sql = "SELECT * FROM livros WHERE editora LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + editora + "%");
            ResultSet rs = ps.executeQuery();

            boolean achou = false;
            while (rs.next()) {
                achou = true;
                exibirLivroDoResultSet(rs);
                System.out.println("--------------------");
            }
            if (!achou) System.out.println("Nenhum livro encontrado para essa editora.");

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Erro na consulta por editora: " + e.getMessage());
        }
    }

    public void menuConsultaISBN(Scanner scanner) {
        System.out.print("Digite o ISBN: ");
        String isbn = scanner.nextLine();

        Livro livro = buscarLivroPorISBN(isbn);
        exibirInfoLivro(livro);
    }

    // ----------- MÉTODOS AUXILIARES -----------

    private Integer buscarUsuarioIdPorEmail(String email) throws SQLException {
        String sql = "SELECT id FROM usuarios WHERE email = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            int id = rs.getInt("id");
            rs.close();
            ps.close();
            return id;
        }
        rs.close();
        ps.close();
        return null;
    }

    private Livro buscarLivroPorTitulo(String titulo) {
        try {
            String sql = "SELECT * FROM livros WHERE titulo LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + titulo + "%");
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Livro livro = new Livro(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("editora"),
                        rs.getInt("ano"),
                        rs.getString("isbn"),
                        rs.getInt("quantidade"));
                rs.close();
                ps.close();
                return livro;
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Erro na busca do livro por título: " + e.getMessage());
        }
        return null;
    }

    private Livro buscarLivroPorISBN(String isbn) {
        try {
            String sql = "SELECT * FROM livros WHERE isbn = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, isbn);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Livro livro = new Livro(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("editora"),
                        rs.getInt("ano"),
                        rs.getString("isbn"),
                        rs.getInt("quantidade"));
                rs.close();
                ps.close();
                return livro;
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Erro na busca do livro por ISBN: " + e.getMessage());
        }
        return null;
    }

    private void exibirInfoLivro(Livro livro) {
        if (livro == null) {
            System.out.println("Livro não encontrado.");
        } else {
            System.out.println("Título: " + livro.getTitulo());
            System.out.println("Autor: " + livro.getAutor());
            System.out.println("Editora: " + livro.getEditora());
            System.out.println("Ano: " + livro.getAno());
            System.out.println("ISBN: " + livro.getIsbn());
            System.out.println("Quantidade disponível: " + livro.getQuantidade());
        }
    }

    private void exibirLivroDoResultSet(ResultSet rs) throws SQLException {
        System.out.println("Título: " + rs.getString("titulo"));
        System.out.println("Autor: " + rs.getString("autor"));
        System.out.println("Editora: " + rs.getString("editora"));
        System.out.println("Ano: " + rs.getInt("ano"));
        System.out.println("ISBN: " + rs.getString("isbn"));
        System.out.println("Quantidade disponível: " + rs.getInt("quantidade"));
    }

    // ------- FECHAR CONEXÃO (Opcional para limpeza) -------
    public void fecharConexao() {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.out.println("Erro ao fechar conexão: " + e.getMessage());
        }
    }
}

// Classe auxiliar Livro (pode colocar em outro arquivo Livro.java)
class LivroBiblioteca {
    private int id;
    private String titulo;
    private String autor;
    private String editora;
    private int ano;
    private String isbn;
    private int quantidade;

    public void Livro(int id, String titulo, String autor, String editora, int ano, String isbn, int quantidade) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.editora = editora;
        this.ano = ano;
        this.isbn = isbn;
        this.quantidade = quantidade;
    }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getEditora() { return editora; }
    public int getAno() { return ano; }
    public String getIsbn() { return isbn; }
    public int getQuantidade() { return quantidade; }
}