package Biblioteca.demo.controller;

import Biblioteca.demo.dto.LocacaoRequestDTO;
import Biblioteca.demo.model.Locacao;
import Biblioteca.demo.service.LocacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import java.util.List;

@RestController
@RequestMapping("/api/locacoes")
public class LocacaoController {

    private final LocacaoService locacaoService;

    public LocacaoController(LocacaoService locacaoService) {
        this.locacaoService = locacaoService;
    }

    @GetMapping
    public ResponseEntity<List<Locacao>> listarTodas() {
        return ResponseEntity.ok(locacaoService.listarTodas());
    }

    @GetMapping("/ativas")
    public ResponseEntity<List<Locacao>> listarAtivas() {
        return ResponseEntity.ok(locacaoService.listarAtivas());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Locacao>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(locacaoService.listarPorUsuario(usuarioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Locacao> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(locacaoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Locacao> criar(@Valid @RequestBody LocacaoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(locacaoService.criar(dto));
    }

    @PutMapping("/{id}/aprovar")
    public ResponseEntity<Locacao> aprovar(@PathVariable Long id) {
        return ResponseEntity.ok(locacaoService.aprovar(id));
    }

    @PutMapping("/{id}/rejeitar")
    public ResponseEntity<Locacao> rejeitar(@PathVariable Long id) {
        return ResponseEntity.ok(locacaoService.rejeitar(id));
    }

    @PutMapping("/{id}/devolver")
    public ResponseEntity<Locacao> devolver(@PathVariable Long id) {
        return ResponseEntity.ok(locacaoService.devolver(id));
    }

    @PostMapping("/verificar-atrasos")
    public ResponseEntity<Map<String, Object>> verificarAtrasos() {
        int count = locacaoService.verificarAtrasos();
        return ResponseEntity.ok(Map.of("atualizadas", count));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        locacaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
