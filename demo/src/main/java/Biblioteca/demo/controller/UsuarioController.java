package Biblioteca.demo.controller;

import Biblioteca.demo.config.AuthHelper;
import Biblioteca.demo.dto.LoginDTO;
import Biblioteca.demo.dto.ResetSenhaDTO;
import Biblioteca.demo.dto.UsuarioDTO;
import Biblioteca.demo.model.Usuario;
import Biblioteca.demo.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AuthHelper authHelper;

    public UsuarioController(UsuarioService usuarioService, AuthHelper authHelper) {
        this.usuarioService = usuarioService;
        this.authHelper     = authHelper;
    }

    // GET — somente ADMIN
    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos(
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader) {
        authHelper.exigirAdmin(userIdHeader);
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    // POST — público (cadastro de admin e cliente)
    @PostMapping
    public ResponseEntity<Usuario> criar(@Valid @RequestBody UsuarioDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.criar(dto));
    }

    // Login — público
    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestBody LoginDTO dto) {
        return ResponseEntity.ok(usuarioService.login(dto));
    }

    // Resetar senha — público (esqueci minha senha)
    @PutMapping("/resetar-senha")
    public ResponseEntity<Map<String, String>> resetarSenha(@RequestBody ResetSenhaDTO dto) {
        usuarioService.resetarSenha(dto);
        return ResponseEntity.ok(Map.of("mensagem", "Senha alterada com sucesso"));
    }

    // PUT e DELETE — somente ADMIN
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader,
            @PathVariable Long id,
            @Valid @RequestBody UsuarioDTO dto) {
        authHelper.exigirAdmin(userIdHeader);
        return ResponseEntity.ok(usuarioService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader,
            @PathVariable Long id) {
        authHelper.exigirAdmin(userIdHeader);
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
