package Biblioteca.demo.config;

import Biblioteca.demo.model.Livro;
import Biblioteca.demo.model.Locacao;
import Biblioteca.demo.model.Usuario;
import Biblioteca.demo.model.enums.Role;
import Biblioteca.demo.model.enums.StatusLocacao;
import Biblioteca.demo.repository.LivroRepository;
import Biblioteca.demo.repository.LocacaoRepository;
import Biblioteca.demo.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final LivroRepository   livroRepository;
    private final LocacaoRepository locacaoRepository;

    public DataInitializer(UsuarioRepository usuarioRepository,
                           LivroRepository livroRepository,
                           LocacaoRepository locacaoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.livroRepository   = livroRepository;
        this.locacaoRepository = locacaoRepository;
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
            domCasmurro.setImageUrl("https://covers.openlibrary.org/b/id/647501-L.jpg");
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
            memorias.setImageUrl("https://covers.openlibrary.org/b/id/7959338-L.jpg");
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
            iracema.setImageUrl("https://covers.openlibrary.org/b/id/2664651-L.jpg");
            iracema.setQuantidadeTotal(4);
            iracema.setQuantidadeDisponivel(3); // 1 exemplar está em locação ativa abaixo
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
            oCortiço.setImageUrl("https://covers.openlibrary.org/b/id/8176059-L.jpg");
            oCortiço.setQuantidadeTotal(3);
            oCortiço.setQuantidadeDisponivel(3);
            livroRepository.save(oCortiço);

            System.out.println("✔ 4 livros de demonstração criados.");

            // ── Locações de demonstração ─────────────────────────────────
            Usuario joao = usuarioRepository.findByEmail("joao.silva@email.com").orElseThrow();

            // 1) PENDENTE — João solicitou Dom Casmurro hoje (aguarda aprovação do admin)
            Locacao pendente = new Locacao();
            pendente.setUsuario(joao);
            pendente.setLivro(domCasmurro);
            pendente.setDataLocacao(LocalDate.now());
            pendente.setDataDevolucaoPrevista(LocalDate.now().plusDays(14));
            pendente.setStatus(StatusLocacao.PENDENTE);
            locacaoRepository.save(pendente);

            // 2) ATIVA com prazo vencido — Iracema retirada há 20 dias, devia ter voltado há 10
            //    (pronta para o admin clicar "Verificar Atrasos" na demo)
            Locacao ativaVencida = new Locacao();
            ativaVencida.setUsuario(joao);
            ativaVencida.setLivro(iracema);
            ativaVencida.setDataLocacao(LocalDate.now().minusDays(20));
            ativaVencida.setDataDevolucaoPrevista(LocalDate.now().minusDays(10));
            ativaVencida.setStatus(StatusLocacao.ATIVA);
            locacaoRepository.save(ativaVencida);

            // 3) DEVOLVIDA — Memórias Póstumas já foi devolvida normalmente
            Locacao devolvida = new Locacao();
            devolvida.setUsuario(joao);
            devolvida.setLivro(memorias);
            devolvida.setDataLocacao(LocalDate.now().minusDays(30));
            devolvida.setDataDevolucaoPrevista(LocalDate.now().minusDays(16));
            devolvida.setDataDevolucaoReal(LocalDate.now().minusDays(18));
            devolvida.setStatus(StatusLocacao.DEVOLVIDA);
            locacaoRepository.save(devolvida);

            System.out.println("✔ 3 locações de demonstração criadas (PENDENTE, ATIVA vencida, DEVOLVIDA).");
        }
    }
}
