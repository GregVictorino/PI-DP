package Biblioteca.demo.repository;

import Biblioteca.demo.model.Livro;
import Biblioteca.demo.model.Locacao;
import Biblioteca.demo.model.Usuario;
import Biblioteca.demo.model.enums.StatusLocacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LocacaoRepository extends JpaRepository<Locacao, Long> {
    List<Locacao> findByUsuario(Usuario usuario);
    List<Locacao> findByStatus(StatusLocacao status);
    List<Locacao> findByLivro(Livro livro);
    List<Locacao> findByStatusAndDataDevolucaoPrevistaLessThan(StatusLocacao status, LocalDate data);
}
