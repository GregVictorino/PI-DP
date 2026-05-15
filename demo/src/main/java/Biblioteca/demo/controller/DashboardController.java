package Biblioteca.demo.controller;

import Biblioteca.demo.config.AuthHelper;
import Biblioteca.demo.model.Livro;
import Biblioteca.demo.model.Locacao;
import Biblioteca.demo.model.Usuario;
import Biblioteca.demo.model.enums.Role;
import Biblioteca.demo.model.enums.StatusLocacao;
import Biblioteca.demo.repository.LivroRepository;
import Biblioteca.demo.repository.LocacaoRepository;
import Biblioteca.demo.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final LivroRepository livroRepository;
    private final LocacaoRepository locacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AuthHelper authHelper;

    public DashboardController(LivroRepository livroRepository,
                               LocacaoRepository locacaoRepository,
                               UsuarioRepository usuarioRepository,
                               AuthHelper authHelper) {
        this.livroRepository    = livroRepository;
        this.locacaoRepository  = locacaoRepository;
        this.usuarioRepository  = usuarioRepository;
        this.authHelper         = authHelper;
    }

    /**
     * Retorna todas as estatísticas necessárias para o dashboard em uma única chamada.
     * Requer perfil ADMIN (header X-User-Id).
     */
    @GetMapping("/resumo")
    public ResponseEntity<Map<String, Object>> resumo(
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader) {

        authHelper.exigirAdmin(userIdHeader);

        List<Livro>   livros   = livroRepository.findAll();
        List<Locacao> locacoes = locacaoRepository.findAll();
        List<Usuario> usuarios = usuarioRepository.findAll();

        // ── Totais gerais ──
        long totalLivros      = livros.size();
        long totalDisponiveis = livros.stream()
                .mapToLong(l -> l.getQuantidadeDisponivel() != null ? l.getQuantidadeDisponivel() : 0)
                .sum();
        long locacoesPendentes  = locacoes.stream().filter(l -> l.getStatus() == StatusLocacao.PENDENTE).count();
        long locacoesAtivas     = locacoes.stream().filter(l -> l.getStatus() == StatusLocacao.ATIVA).count();
        long locacoesAtrasadas  = locacoes.stream().filter(l -> l.getStatus() == StatusLocacao.ATRASADA).count();
        long locacoesDevolvidas = locacoes.stream().filter(l -> l.getStatus() == StatusLocacao.DEVOLVIDA).count();
        long totalClientes      = usuarios.stream().filter(u -> u.getRole() == Role.CLIENTE).count();

        // ── Livros por gênero ──
        Map<String, Long> livrosPorGenero = livros.stream()
                .collect(Collectors.groupingBy(
                        l -> l.getGenero() != null ? l.getGenero() : "Sem gênero",
                        Collectors.counting()
                ));

        // ── Últimas 5 locações ──
        List<Locacao> recentes = locacoes.stream()
                .sorted((a, b) -> {
                    if (a.getId() == null) return 1;
                    if (b.getId() == null) return -1;
                    return b.getId().compareTo(a.getId());
                })
                .limit(5)
                .toList();

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("totalLivros",       totalLivros);
        resposta.put("totalDisponiveis",  totalDisponiveis);
        resposta.put("locacoesPendentes", locacoesPendentes);
        resposta.put("locacoesAtivas",    locacoesAtivas);
        resposta.put("locacoesAtrasadas", locacoesAtrasadas);
        resposta.put("locacoesDevolvidas",locacoesDevolvidas);
        resposta.put("totalClientes",     totalClientes);
        resposta.put("livrosPorGenero",   livrosPorGenero);
        resposta.put("locacoesRecentes",  recentes);

        return ResponseEntity.ok(resposta);
    }
}
