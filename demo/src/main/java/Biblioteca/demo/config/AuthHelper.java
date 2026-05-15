package Biblioteca.demo.config;

import Biblioteca.demo.model.Usuario;
import Biblioteca.demo.model.enums.Role;
import Biblioteca.demo.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AuthHelper {

    private final UsuarioRepository usuarioRepository;

    public AuthHelper(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Verifica se o header X-User-Id pertence a um ADMIN.
     * Lança 401 se não autenticado, 403 se não for admin.
     */
    public void exigirAdmin(String userIdHeader) {
        if (userIdHeader == null || userIdHeader.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Autenticação necessária");
        }
        try {
            Long userId = Long.parseLong(userIdHeader);
            Usuario usuario = usuarioRepository.findById(userId)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.UNAUTHORIZED, "Usuário não encontrado"));

            if (usuario.getRole() != Role.ADMIN) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN, "Acesso restrito a administradores");
            }
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Header X-User-Id inválido");
        }
    }
}
