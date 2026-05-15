package Biblioteca.demo.service;

import Biblioteca.demo.dto.LivroDTO;
import Biblioteca.demo.exception.ResourceNotFoundException;
import Biblioteca.demo.model.Livro;
import Biblioteca.demo.repository.LivroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LivroService {

    private final LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public List<Livro> listarTodos() {
        return livroRepository.findAll();
    }

    public Livro buscarPorId(Long id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado: " + id));
    }

    public List<Livro> buscarPorTitulo(String titulo) {
        return livroRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public List<Livro> buscarComFiltros(String busca, String genero, Boolean disponivel) {
        return livroRepository.buscarComFiltros(
                busca   != null ? busca   : "",
                genero  != null ? genero  : "",
                disponivel
        );
    }

    public Livro criar(LivroDTO dto) {
        Livro livro = new Livro();
        livro.setTitulo(dto.getTitulo());
        livro.setAutor(dto.getAutor());
        livro.setIsbn(dto.getIsbn());
        livro.setGenero(dto.getGenero());
        livro.setImageUrl(dto.getImageUrl());
        livro.setDescricao(dto.getDescricao());
        livro.setAnoPublicacao(dto.getAnoPublicacao());
        livro.setQuantidadeTotal(dto.getQuantidadeTotal());
        livro.setQuantidadeDisponivel(dto.getQuantidadeTotal());
        return livroRepository.save(livro);
    }

    public Livro atualizar(Long id, LivroDTO dto) {
        Livro livro = buscarPorId(id);
        livro.setTitulo(dto.getTitulo());
        livro.setAutor(dto.getAutor());
        livro.setIsbn(dto.getIsbn());
        livro.setGenero(dto.getGenero());
        livro.setImageUrl(dto.getImageUrl());
        livro.setDescricao(dto.getDescricao());
        livro.setAnoPublicacao(dto.getAnoPublicacao());
        livro.setQuantidadeTotal(dto.getQuantidadeTotal());
        return livroRepository.save(livro);
    }

    public void deletar(Long id) {
        buscarPorId(id);
        livroRepository.deleteById(id);
    }
}
