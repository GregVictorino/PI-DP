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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class LocacaoService {

    private final LocacaoRepository locacaoRepository;
    private final LivroRepository livroRepository;
    private final UsuarioRepository usuarioRepository;

    public LocacaoService(LocacaoRepository locacaoRepository,
                          LivroRepository livroRepository,
                          UsuarioRepository usuarioRepository) {
        this.locacaoRepository = locacaoRepository;
        this.livroRepository = livroRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Locacao> listarTodas() {
        return locacaoRepository.findAll();
    }

    public Locacao buscarPorId(Long id) {
        return locacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Locação não encontrada: " + id));
    }

    public List<Locacao> listarPorUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + usuarioId));
        return locacaoRepository.findByUsuario(usuario);
    }

    public List<Locacao> listarAtivas() {
        return locacaoRepository.findByStatus(StatusLocacao.ATIVA);
    }

    @Transactional
    public Locacao criar(LocacaoRequestDTO dto) {
        Livro livro = livroRepository.findById(dto.getLivroId())
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado"));

        if (livro.getQuantidadeDisponivel() <= 0) {
            throw new RuntimeException("Livro indisponível para locação");
        }

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        // Não decrementa aqui — aguarda aprovação do admin
        Locacao locacao = new Locacao();
        locacao.setLivro(livro);
        locacao.setUsuario(usuario);
        locacao.setDataLocacao(LocalDate.now());
        locacao.setDataDevolucaoPrevista(dto.getDataDevolucaoPrevista());
        locacao.setStatus(StatusLocacao.PENDENTE);
        return locacaoRepository.save(locacao);
    }

    @Transactional
    public Locacao aprovar(Long id) {
        Locacao locacao = buscarPorId(id);
        if (locacao.getStatus() != StatusLocacao.PENDENTE) {
            throw new RuntimeException("Apenas solicitações pendentes podem ser aprovadas");
        }

        Livro livro = locacao.getLivro();
        if (livro.getQuantidadeDisponivel() <= 0) {
            throw new RuntimeException("Livro indisponível no momento — não é possível aprovar");
        }

        livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() - 1);
        livroRepository.save(livro);

        locacao.setStatus(StatusLocacao.ATIVA);
        return locacaoRepository.save(locacao);
    }

    @Transactional
    public Locacao rejeitar(Long id) {
        Locacao locacao = buscarPorId(id);
        if (locacao.getStatus() != StatusLocacao.PENDENTE) {
            throw new RuntimeException("Apenas solicitações pendentes podem ser rejeitadas");
        }
        locacao.setStatus(StatusLocacao.REJEITADA);
        return locacaoRepository.save(locacao);
    }

    @Transactional
    public Locacao devolver(Long id) {
        Locacao locacao = buscarPorId(id);
        if (locacao.getStatus() == StatusLocacao.DEVOLVIDA) {
            throw new RuntimeException("Livro já foi devolvido");
        }
        locacao.setDataDevolucaoReal(LocalDate.now());
        locacao.setStatus(StatusLocacao.DEVOLVIDA);

        Livro livro = locacao.getLivro();
        livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() + 1);
        livroRepository.save(livro);

        return locacaoRepository.save(locacao);
    }

    public void deletar(Long id) {
        Locacao locacao = buscarPorId(id);
        // Só devolve ao estoque se já estava ATIVA (PENDENTE e REJEITADA nunca decrementaram)
        if (locacao.getStatus() == StatusLocacao.ATIVA || locacao.getStatus() == StatusLocacao.ATRASADA) {
            Livro livro = locacao.getLivro();
            livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() + 1);
            livroRepository.save(livro);
        }
        locacaoRepository.deleteById(id);
    }
}
