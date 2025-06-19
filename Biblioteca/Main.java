import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Biblioteca biblioteca = new Biblioteca();
        LoginService loginService = new LoginService();
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Bem-vindo à Biblioteca ===");

        // Login simples
        boolean autenticado = false;
        while (!autenticado) {
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Senha: ");
            String senha = scanner.nextLine();

            if (loginService.autenticar(email, senha)) {
                autenticado = true;
                System.out.println("Login realizado com sucesso!");
            } else {
                System.out.println("Usuário ou senha inválidos. Tente novamente.");
            }
        }

        // Menu principal
        boolean sair = false;
        while (!sair) {
            System.out.println("\nMenu principal:");
            System.out.println("1 - Cadastrar Usuário");
            System.out.println("2 - Cadastrar Funcionário");
            System.out.println("3 - Cadastrar Livro");
            System.out.println("4 - Realizar Empréstimo");
            System.out.println("5 - Realizar Devolução");
            System.out.println("6 - Consultar Livro por Título");
            System.out.println("7 - Consultar Livro por Autor");
            System.out.println("8 - Consultar Livro por Editora");
            System.out.println("9 - Consultar Livro por ISBN");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");

            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    biblioteca.menuCadastrarUsuario(scanner);
                    break;
                case "2":
                    biblioteca.menuCadastrarFuncionario(scanner);
                    break;
                case "3":
                    biblioteca.menuCadastrarLivro(scanner);
                    break;
                case "4":
                    biblioteca.menuEmprestimo(scanner);
                    break;
                case "5":
                    biblioteca.menuDevolucao(scanner);
                    break;
                case "6":
                    biblioteca.menuConsultaTitulo(scanner);
                    break;
                case "7":
                    biblioteca.menuConsultaAutor(scanner);
                    break;
                case "8":
                    biblioteca.menuConsultaEditora(scanner);
                    break;
                case "9":
                    biblioteca.menuConsultaISBN(scanner);
                    break;
                case "0":
                    sair = true;
                    System.out.println("Saindo... Obrigado por usar a Biblioteca!");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }

        scanner.close();
    }
}