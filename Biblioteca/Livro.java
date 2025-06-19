public class Livro {
    private int id;
    private String titulo;
    private String autor;
    private String editora;
    private int ano;
    private String isbn;
    private int quantidade;

    public Livro(int id, String titulo, String autor, String editora, int ano, String isbn, int quantidade) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.editora = editora;
        this.ano = ano;
        this.isbn = isbn;
        this.quantidade = quantidade;
    }

    // Getters
    public int getId() {
        return id;
    }
    public String getTitulo() {
        return titulo;
    }
    public String getAutor() {
        return autor;
    }
    public String getEditora() {
        return editora;
    }
    public int getAno() {
        return ano;
    }
    public String getIsbn() {
        return isbn;
    }
    public int getQuantidade() {
        return quantidade;
    }

    // Métodos para controle de quantidade (empréstimo e devolução)
    public boolean isDisponivel() {
        return quantidade > 0;
    }

    public void emprestar() {
        if (quantidade > 0) {
            quantidade--;
        }
    }

    public void devolver() {
        quantidade++;
    }
}