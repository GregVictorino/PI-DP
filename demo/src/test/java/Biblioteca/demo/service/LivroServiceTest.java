package Biblioteca.demo.service;

import Biblioteca.demo.dto.LivroDTO;
import Biblioteca.demo.exception.ResourceNotFoundException;
import Biblioteca.demo.model.Livro;
import Biblioteca.demo.repository.LivroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LivroServiceTest {

    @Mock
    private LivroRepository livroRepository;

    @InjectMocks
    private LivroService livroService;

    private Livro livro;
    private LivroDTO livroDTO;

    @BeforeEach
    void setUp() {
        livro = new Livro();
        livro.setId(1L);
        livro.setTitulo("Dom Casmurro");
        livro.setAutor("Machado de Assis");
        livro.setGenero("Romance");
        livro.setQuantidadeTotal(3);
        livro.setQuantidadeDisponivel(3);

        livroDTO = new LivroDTO();
        livroDTO.setTitulo("Dom Casmurro");
        livroDTO.setAutor("Machado de Assis");
        livroDTO.setGenero("Romance");
        livroDTO.setQuantidadeTotal(3);
    }

    @Test
    @DisplayName("Deve criar um livro e definir quantidade disponível igual ao total")
    void deveCriarLivroComQuantidadeDisponivel() {
        when(livroRepository.save(any(Livro.class))).thenReturn(livro);

        Livro resultado = livroService.criar(livroDTO);

        assertNotNull(resultado);
        assertEquals("Dom Casmurro", resultado.getTitulo());
        assertEquals("Machado de Assis", resultado.getAutor());
        assertEquals(3, resultado.getQuantidadeDisponivel());
        verify(livroRepository, times(1)).save(any(Livro.class));
    }

    @Test
    @DisplayName("Deve retornar livro ao buscar por ID existente")
    void deveBuscarLivroPorId() {
        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));

        Livro resultado = livroService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Dom Casmurro", resultado.getTitulo());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar livro com ID inexistente")
    void deveLancarExcecaoQuandoLivroNaoEncontrado() {
        when(livroRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> livroService.buscarPorId(99L));
    }

    @Test
    @DisplayName("Deve retornar lista de todos os livros")
    void deveListarTodosOsLivros() {
        Livro livro2 = new Livro();
        livro2.setId(2L);
        livro2.setTitulo("O Cortiço");
        livro2.setAutor("Aluísio Azevedo");

        when(livroRepository.findAll()).thenReturn(List.of(livro, livro2));

        List<Livro> resultado = livroService.listarTodos();

        assertEquals(2, resultado.size());
        verify(livroRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve deletar livro existente sem erros")
    void deveDeletarLivro() {
        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));
        doNothing().when(livroRepository).deleteById(1L);

        assertDoesNotThrow(() -> livroService.deletar(1L));
        verify(livroRepository, times(1)).deleteById(1L);
    }
}
