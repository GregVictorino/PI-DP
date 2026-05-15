package Biblioteca.demo.config;

import Biblioteca.demo.model.Livro;
import Biblioteca.demo.model.Usuario;
import Biblioteca.demo.model.enums.Role;
import Biblioteca.demo.repository.LivroRepository;
import Biblioteca.demo.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final LivroRepository   livroRepository;

    public DataInitializer(UsuarioRepository usuarioRepository,
                           LivroRepository livroRepository) {
        this.usuarioRepository = usuarioRepository;
        this.livroRepository   = livroRepository;
    }

    @Override
    public void run(String... args) {

        // ── Admin padrão ──────────────────────────────────────────────
        if (usuarioRepository.findByEmail("admin@biblioteca.com").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setNome("Administrador");
            admin.setEmail("admin@biblioteca.com");
            admin.setSenha("admin123");
            admin.setRole(Role.ADMIN);
            usuarioRepository.save(admin);
            System.out.println("✔ Admin criado: admin@biblioteca.com / admin123");
        }

        // ── Cliente de demonstração ───────────────────────────────────
        if (usuarioRepository.findByEmail("joao.silva@email.com").isEmpty()) {
            Usuario cliente = new Usuario();
            cliente.setNome("João Silva");
            cliente.setEmail("joao.silva@email.com");
            cliente.setSenha("cliente123");
            cliente.setTelefone("(11) 98765-4321");
            cliente.setCpf("123.456.789-00");
            cliente.setRole(Role.CLIENTE);
            usuarioRepository.save(cliente);
            System.out.println("✔ Cliente criado: joao.silva@email.com / cliente123");
        }

        // ── Livros de demonstração ────────────────────────────────────
        if (livroRepository.count() == 0) {

            Livro domCasmurro = new Livro();
            domCasmurro.setTitulo("Dom Casmurro");
            domCasmurro.setAutor("Machado de Assis");
            domCasmurro.setIsbn("978-85-209-2325-0");
            domCasmurro.setGenero("Romance");
            domCasmurro.setAnoPublicacao(1899);
            domCasmurro.setDescricao(
                "Um dos maiores romances da literatura brasileira. " +
                "Bentinho, o Dom Casmurro, narra sua história de amor com Capitu, " +
                "a famosa \"Capitu dos olhos de ressaca\", e a dúvida que marcou sua vida."
            );
            domCasmurro.setImageUrl(
                "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3e/" +
                "Primeira_edi%C3%A7%C3%A3o_de_Dom_Casmurro.jpg/330px-Primeira_edi%C3%A7%C3%A3o_de_Dom_Casmurro.jpg"
            );
            domCasmurro.setQuantidadeTotal(5);
            domCasmurro.setQuantidadeDisponivel(5);
            livroRepository.save(domCasmurro);

            Livro memorias = new Livro();
            memorias.setTitulo("Memórias Póstumas de Brás Cubas");
            memorias.setAutor("Machado de Assis");
            memorias.setIsbn("978-85-209-1785-3");
            memorias.setGenero("Romance");
            memorias.setAnoPublicacao(1881);
            memorias.setDescricao(
                "Narrado por um defunto-autor, Brás Cubas conta sua vida de forma " +
                "irreverente e filosófica, inaugurando o Realismo no Brasil."
            );
            memorias.setImageUrl(
                "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f5/" +
                "Mem%C3%B3rias_P%C3%B3stumas_de_Br%C3%A1s_Cubas_capa.jpg/250px-Mem%C3%B3rias_P%C3%B3stumas_de_Br%C3%A1s_Cubas_capa.jpg"
            );
            memorias.setQuantidadeTotal(3);
            memorias.setQuantidadeDisponivel(3);
            livroRepository.save(memorias);

            Livro iracema = new Livro();
            iracema.setTitulo("Iracema");
            iracema.setAutor("José de Alencar");
            iracema.setIsbn("978-85-209-1234-5");
            iracema.setGenero("Romance");
            iracema.setAnoPublicacao(1865);
            iracema.setDescricao(
                "Lenda do Ceará que narra o amor entre a índia Iracema e o " +
                "guerreiro português Martim Soares Moreno, marco do Romantismo brasileiro."
            );
            iracema.setImageUrl(
                "https://upload.wikimedia.org/wikipedia/commons/thumb/9/9e/" +
                "Iracema_%281865%29_crop.jpg/220px-Iracema_%281865%29_crop.jpg"
            );
            iracema.setQuantidadeTotal(4);
            iracema.setQuantidadeDisponivel(4);
            livroRepository.save(iracema);

            Livro oCortiço = new Livro();
            oCortiço.setTitulo("O Cortiço");
            oCortiço.setAutor("Aluísio Azevedo");
            oCortiço.setIsbn("978-85-209-5678-9");
            oCortiço.setGenero("Naturalismo");
            oCortiço.setAnoPublicacao(1890);
            oCortiço.setDescricao(
                "Clássico do Naturalismo brasileiro que retrata a vida coletiva " +
                "em uma habitação popular do Rio de Janeiro do século XIX."
            );
            oCortiço.setImageUrl(
                "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5e/" +
                "O_Corti%C3%A7o_-_Aluisio_Azevedo.jpg/220px-O_Corti%C3%A7o_-_Aluisio_Azevedo.jpg"
            );
            oCortiço.setQuantidadeTotal(3);
            oCortiço.setQuantidadeDisponivel(3);
            livroRepository.save(oCortiço);

            System.out.println("✔ 4 livros de demonstração criados.");
        }
    }
}
