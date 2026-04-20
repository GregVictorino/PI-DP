package Biblioteca.demo.config;

import Biblioteca.demo.model.Usuario;
import Biblioteca.demo.model.enums.Role;
import Biblioteca.demo.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;

    public DataInitializer(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void run(String... args) {
        if (usuarioRepository.findByEmail("admin@biblioteca.com").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setNome("Administrador");
            admin.setEmail("admin@biblioteca.com");
            admin.setSenha("admin123");
            admin.setRole(Role.ADMIN);
            usuarioRepository.save(admin);
            System.out.println("Admin criado: admin@biblioteca.com / admin123");
        }
    }
}
