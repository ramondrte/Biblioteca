public class LoginService {
    public boolean autenticar(String email, String senha) {
        // Simulação: login genérico
        return email.equals("admin@biblioteca.com") && senha.equals("1234");
    }
}