import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseSetup {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:biblioteca.db";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            String sql = "CREATE TABLE IF NOT EXISTS livros (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "titulo TEXT NOT NULL," +
                         "autor TEXT NOT NULL," +
                         "editora TEXT," +
                         "ano INTEGER," +
                         "isbn TEXT," +
                         "quantidade INTEGER" +
                         ");";

            stmt.execute(sql);
            System.out.println("Tabela criada com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}