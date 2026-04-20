package Biblioteca.demo.service;

import Biblioteca.demo.dto.LoginDTO;
import Biblioteca.demo.dto.ResetSenhaDTO;
import Biblioteca.demo.dto.UsuarioDTO;
import Biblioteca.demo.exception.ResourceNotFoundException;
import Biblioteca.demo.model.Usuario;
import Biblioteca.demo.model.enums.Role;
import Biblioteca.demo.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + id));
    }

    public Usuario criar(UsuarioDTO dto) {
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email já cadastrado");
        }
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());
        usuario.setTelefone(dto.getTelefone());
        usuario.setCpf(dto.getCpf());
        usuario.setRole(dto.getRole() != null ? dto.getRole() : Role.CLIENTE);
        return usuarioRepository.save(usuario);
    }

    public Usuario login(LoginDTO dto) {
        return usuarioRepository.findByEmail(dto.getEmail())
                .filter(u -> u.getSenha().equals(dto.getSenha()))
                .orElseThrow(() -> new RuntimeException("Email ou senha inválidos"));
    }

    public Usuario atualizar(Long id, UsuarioDTO dto) {
        Usuario usuario = buscarPorId(id);
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            usuario.setSenha(dto.getSenha());
        }
        usuario.setTelefone(dto.getTelefone());
        usuario.setCpf(dto.getCpf());
        return usuarioRepository.save(usuario);
    }

    public void resetarSenha(ResetSenhaDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Email não encontrado"));
        usuario.setSenha(dto.getNovaSenha());
        usuarioRepository.save(usuario);
    }

    public void deletar(Long id) {
        buscarPorId(id);
        usuarioRepository.deleteById(id);
    }
}
