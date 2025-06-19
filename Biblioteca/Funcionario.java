public class Funcionario {
    private int id;
    private String nome;
    private String email;
    private String telefone;
    private String cargo;

    public Funcionario(int id, String nome, String email, String telefone, String cargo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.cargo = cargo;
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
    public String getCargo() {
        return cargo;
    }
}