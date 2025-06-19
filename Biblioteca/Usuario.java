import java.util.ArrayList;

public class Usuario {
    private int id;
    private String nome;
    private String email;
    private String telefone;
    private ArrayList<Emprestimo> emprestimos;

    public Usuario(int id, String nome, String email, String telefone) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.emprestimos = new ArrayList<>();
    }

    // Getters
    public int getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }
    public String getEmail() {
        return email;
    }
    public String getTelefone() {
        return telefone;
    }
    public ArrayList<Emprestimo> getEmprestimos() {
        return emprestimos;
    }

    // Método para adicionar empréstimo
    public void adicionarEmprestimo(Emprestimo emprestimo) {
        emprestimos.add(emprestimo);
    }
}