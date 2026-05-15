package Biblioteca.demo;

import Biblioteca.demo.dto.LocacaoRequestDTO;
import Biblioteca.demo.model.Livro;
import Biblioteca.demo.model.Locacao;
import Biblioteca.demo.model.Usuario;
import Biblioteca.demo.model.enums.Role;
import Biblioteca.demo.model.enums.StatusLocacao;
import Biblioteca.demo.repository.LivroRepository;
import Biblioteca.demo.repository.LocacaoRepository;
import Biblioteca.demo.repository.UsuarioRepository;
import Biblioteca.demo.service.LocacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FluxoSolicitacaoIntegrationTest {

    @Autowired LocacaoService    locacaoService;
    @Autowired LivroRepository   livroRepository;
    @Autowired UsuarioRepository usuarioRepository;
    @Autowired LocacaoRepository locacaoRepository;

    private Livro   livro;
    private Usuario cliente;

    @BeforeEach
    void setUp() {
        livro = new Livro();
        livro.setTitulo("Dom Casmurro");
        livro.setAutor("Machado de Assis");
        livro.setQuantidadeTotal(5);
        livro.setQuantidadeDisponivel(5);
        livroRepository.save(livro);

        cliente = new Usuario();
        cliente.setNome("João Silva");
        cliente.setEmail("joao.teste@email.com");
        cliente.setSenha("senha123");
        cliente.setRole(Role.CLIENTE);
        usuarioRepository.save(cliente);
    }

    @Test
    @DisplayName("1 - Solicitação deve ser criada com status PENDENTE")
    void solicitacaoDeveCriarComStatusPendente() {
        LocacaoRequestDTO dto = new LocacaoRequestDTO();
        dto.setLivroId(livro.getId());
        dto.setUsuarioId(cliente.getId());
        dto.setDataDevolucaoPrevista(LocalDate.now().plusDays(7));

        Locacao locacao = locacaoService.criar(dto);

        assertEquals(StatusLocacao.PENDENTE, locacao.getStatus(),
            "❌ Status deveria ser PENDENTE mas foi: " + locacao.getStatus());
        assertEquals(5, livro.getQuantidadeDisponivel(),
            "❌ Quantidade nao devia mudar na solicitacao");

        System.out.println("✔ Solicitação criada com status PENDENTE");
    }

    @Test
    @DisplayName("2 - Admin aprova → status vira ATIVA e estoque diminui")
    void adminDeveAprovarESatusViraAtiva() {
        LocacaoRequestDTO dto = new LocacaoRequestDTO();
        dto.setLivroId(livro.getId());
        dto.setUsuarioId(cliente.getId());
        dto.setDataDevolucaoPrevista(LocalDate.now().plusDays(7));

        Locacao locacao = locacaoService.criar(dto);
        assertEquals(StatusLocacao.PENDENTE, locacao.getStatus());

        Locacao aprovada = locacaoService.aprovar(locacao.getId());

        assertEquals(StatusLocacao.ATIVA, aprovada.getStatus(),
            "❌ Status deveria ser ATIVA mas foi: " + aprovada.getStatus());

        Livro livroAtualizado = livroRepository.findById(livro.getId()).orElseThrow();
        assertEquals(4, livroAtualizado.getQuantidadeDisponivel(),
            "❌ Estoque deveria ser 4 mas foi: " + livroAtualizado.getQuantidadeDisponivel());

        System.out.println("✔ Aprovação OK - status ATIVA, estoque = 4");
    }

    @Test
    @DisplayName("3 - Admin rejeita → status vira REJEITADA e estoque fica intacto")
    void adminDeveRejeitarESatusViraRejeitada() {
        LocacaoRequestDTO dto = new LocacaoRequestDTO();
        dto.setLivroId(livro.getId());
        dto.setUsuarioId(cliente.getId());
        dto.setDataDevolucaoPrevista(LocalDate.now().plusDays(7));

        Locacao locacao = locacaoService.criar(dto);
        Locacao rejeitada = locacaoService.rejeitar(locacao.getId());

        assertEquals(StatusLocacao.REJEITADA, rejeitada.getStatus(),
            "❌ Status deveria ser REJEITADA mas foi: " + rejeitada.getStatus());

        Livro livroAtualizado = livroRepository.findById(livro.getId()).orElseThrow();
        assertEquals(5, livroAtualizado.getQuantidadeDisponivel(),
            "❌ Estoque nao devia mudar na rejeicao");

        System.out.println("✔ Rejeição OK - status REJEITADA, estoque = 5");
    }

    @Test
    @DisplayName("4 - Aprovação falha se status não for PENDENTE")
    void aprovacaoFalhaSestStatusNaoPendente() {
        LocacaoRequestDTO dto = new LocacaoRequestDTO();
        dto.setLivroId(livro.getId());
        dto.setUsuarioId(cliente.getId());
        dto.setDataDevolucaoPrevista(LocalDate.now().plusDays(7));

        Locacao locacao = locacaoService.criar(dto);
        locacaoService.aprovar(locacao.getId()); // aprova uma vez

        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> locacaoService.aprovar(locacao.getId()));

        assertTrue(ex.getMessage().contains("pendentes"),
            "❌ Mensagem de erro incorreta: " + ex.getMessage());

        System.out.println("✔ Dupla aprovação bloqueada corretamente");
    }
}
