package Biblioteca.demo.service;

import Biblioteca.demo.dto.LocacaoRequestDTO;
import Biblioteca.demo.exception.ResourceNotFoundException;
import Biblioteca.demo.model.Livro;
import Biblioteca.demo.model.Locacao;
import Biblioteca.demo.model.Usuario;
import Biblioteca.demo.model.enums.StatusLocacao;
import Biblioteca.demo.repository.LivroRepository;
import Biblioteca.demo.repository.LocacaoRepository;
import Biblioteca.demo.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocacaoServiceTest {

    @Mock
    private LocacaoRepository locacaoRepository;

    @Mock
    private LivroRepository livroRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private LocacaoService locacaoService;

    private Livro livro;
    private Usuario usuario;
    private Locacao locacao;

    @BeforeEach
    void setUp() {
        livro = new Livro();
        livro.setId(1L);
        livro.setTitulo("Dom Casmurro");
        livro.setQuantidadeTotal(3);
        livro.setQuantidadeDisponivel(3);

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");

        locacao = new Locacao();
        locacao.setId(1L);
        locacao.setLivro(livro);
        locacao.setUsuario(usuario);
        locacao.setDataLocacao(LocalDate.now());
        locacao.setDataDevolucaoPrevista(LocalDate.now().plusDays(7));
        locacao.setStatus(StatusLocacao.ATIVA);
    }

    @Test
    @DisplayName("Deve criar solicitação com status PENDENTE sem decrementar o estoque")
    void deveCriarSolicitacaoComStatusPendente() {
        LocacaoRequestDTO dto = new LocacaoRequestDTO();
        dto.setLivroId(1L);
        dto.setUsuarioId(1L);
        dto.setDataDevolucaoPrevista(LocalDate.now().plusDays(7));

        locacao.setStatus(StatusLocacao.PENDENTE);

        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(locacaoRepository.save(any(Locacao.class))).thenReturn(locacao);

        Locacao resultado = locacaoService.criar(dto);

        assertNotNull(resultado);
        assertEquals(StatusLocacao.PENDENTE, resultado.getStatus());
        // Estoque NÃO deve ser decrementado na solicitação — só na aprovação
        assertEquals(3, livro.getQuantidadeDisponivel());
        verify(livroRepository, never()).save(livro);
        verify(locacaoRepository, times(1)).save(any(Locacao.class));
    }

    @Test
    @DisplayName("Deve aprovar solicitação, mudar status para ATIVA e decrementar estoque")
    void deveAprovarSolicitacaoEDecrementarEstoque() {
        locacao.setStatus(StatusLocacao.PENDENTE);

        when(locacaoRepository.findById(1L)).thenReturn(Optional.of(locacao));
        when(livroRepository.save(any(Livro.class))).thenReturn(livro);
        when(locacaoRepository.save(any(Locacao.class))).thenReturn(locacao);

        Locacao resultado = locacaoService.aprovar(1L);

        assertEquals(StatusLocacao.ATIVA, resultado.getStatus());
        assertEquals(2, livro.getQuantidadeDisponivel());
        verify(livroRepository, times(1)).save(livro);
    }

    @Test
    @DisplayName("Deve rejeitar solicitação e manter o estoque intacto")
    void deveRejeitarSolicitacao() {
        locacao.setStatus(StatusLocacao.PENDENTE);

        when(locacaoRepository.findById(1L)).thenReturn(Optional.of(locacao));
        when(locacaoRepository.save(any(Locacao.class))).thenReturn(locacao);

        Locacao resultado = locacaoService.rejeitar(1L);

        assertEquals(StatusLocacao.REJEITADA, resultado.getStatus());
        assertEquals(3, livro.getQuantidadeDisponivel()); // estoque não muda
        verify(livroRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar locar livro sem exemplares disponíveis")
    void deveLancarExcecaoQuandoLivroIndisponivel() {
        livro.setQuantidadeDisponivel(0);

        LocacaoRequestDTO dto = new LocacaoRequestDTO();
        dto.setLivroId(1L);
        dto.setUsuarioId(1L);
        dto.setDataDevolucaoPrevista(LocalDate.now().plusDays(7));

        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> locacaoService.criar(dto));
        assertEquals("Livro indisponível para locação", ex.getMessage());
        verify(locacaoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve registrar devolução e incrementar quantidade disponível do livro")
    void deveRegistrarDevolucaoEIncrementarEstoque() {
        when(locacaoRepository.findById(1L)).thenReturn(Optional.of(locacao));
        when(livroRepository.save(any(Livro.class))).thenReturn(livro);
        when(locacaoRepository.save(any(Locacao.class))).thenReturn(locacao);

        Locacao resultado = locacaoService.devolver(1L);

        assertEquals(StatusLocacao.DEVOLVIDA, resultado.getStatus());
        assertNotNull(resultado.getDataDevolucaoReal());
        // Verifica que o estoque foi incrementado
        assertEquals(4, livro.getQuantidadeDisponivel());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar devolver locação já devolvida")
    void deveLancarExcecaoAoDevolverLocacaoJaDevolvida() {
        locacao.setStatus(StatusLocacao.DEVOLVIDA);
        when(locacaoRepository.findById(1L)).thenReturn(Optional.of(locacao));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> locacaoService.devolver(1L));
        assertEquals("Livro já foi devolvido", ex.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar locação com ID inexistente")
    void deveLancarExcecaoQuandoLocacaoNaoEncontrada() {
        when(locacaoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> locacaoService.buscarPorId(99L));
    }
}
