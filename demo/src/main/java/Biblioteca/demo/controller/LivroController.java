package Biblioteca.demo.controller;

import Biblioteca.demo.config.AuthHelper;
import Biblioteca.demo.dto.LivroDTO;
import Biblioteca.demo.model.Livro;
import Biblioteca.demo.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livros")
public class LivroController {

    private final LivroService livroService;
    private final AuthHelper authHelper;

    public LivroController(LivroService livroService, AuthHelper authHelper) {
        this.livroService = livroService;
        this.authHelper   = authHelper;
    }

    // GET — público (catálogo público usa este endpoint)
    @GetMapping
    public ResponseEntity<List<Livro>> listarTodos(
            @RequestParam(required = false) String busca,
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) Boolean disponivel) {

        boolean temFiltro = (busca != null && !busca.isBlank())
                         || (genero != null && !genero.isBlank())
                         || disponivel != null;

        if (temFiltro) {
            return ResponseEntity.ok(livroService.buscarComFiltros(busca, genero, disponivel));
        }
        return ResponseEntity.ok(livroService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Livro> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(livroService.buscarPorId(id));
    }

    // POST, PUT, DELETE — somente ADMIN
    @PostMapping
    public ResponseEntity<Livro> criar(
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader,
            @Valid @RequestBody LivroDTO dto) {
        authHelper.exigirAdmin(userIdHeader);
        return ResponseEntity.status(HttpStatus.CREATED).body(livroService.criar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Livro> atualizar(
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader,
            @PathVariable Long id,
            @Valid @RequestBody LivroDTO dto) {
        authHelper.exigirAdmin(userIdHeader);
        return ResponseEntity.ok(livroService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader,
            @PathVariable Long id) {
        authHelper.exigirAdmin(userIdHeader);
        livroService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
