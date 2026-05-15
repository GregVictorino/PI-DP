package Biblioteca.demo.controller;

import Biblioteca.demo.config.AuthHelper;
import Biblioteca.demo.dto.LocacaoRequestDTO;
import Biblioteca.demo.model.Locacao;
import Biblioteca.demo.service.LocacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locacoes")
public class LocacaoController {

    private final LocacaoService locacaoService;
    private final AuthHelper authHelper;

    public LocacaoController(LocacaoService locacaoService, AuthHelper authHelper) {
        this.locacaoService = locacaoService;
        this.authHelper     = authHelper;
    }

    // GET — somente ADMIN (listagem geral)
    @GetMapping
    public ResponseEntity<List<Locacao>> listarTodas(
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader) {
        authHelper.exigirAdmin(userIdHeader);
        return ResponseEntity.ok(locacaoService.listarTodas());
    }

    @GetMapping("/ativas")
    public ResponseEntity<List<Locacao>> listarAtivas(
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader) {
        authHelper.exigirAdmin(userIdHeader);
        return ResponseEntity.ok(locacaoService.listarAtivas());
    }

    // GET por usuário — ADMIN ou o próprio cliente (sem validação de role aqui,
    // pois o cliente acessa suas próprias locações)
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Locacao>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(locacaoService.listarPorUsuario(usuarioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Locacao> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(locacaoService.buscarPorId(id));
    }

    // POST — ADMIN ou CLIENTE autenticado
    @PostMapping
    public ResponseEntity<Locacao> criar(@Valid @RequestBody LocacaoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(locacaoService.criar(dto));
    }

    // Devolver — somente ADMIN
    @PutMapping("/{id}/devolver")
    public ResponseEntity<Locacao> devolver(
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader,
            @PathVariable Long id) {
        authHelper.exigirAdmin(userIdHeader);
        return ResponseEntity.ok(locacaoService.devolver(id));
    }

    // DELETE — somente ADMIN
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader,
            @PathVariable Long id) {
        authHelper.exigirAdmin(userIdHeader);
        locacaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
