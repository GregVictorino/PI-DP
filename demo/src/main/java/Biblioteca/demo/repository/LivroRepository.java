package Biblioteca.demo.repository;

import Biblioteca.demo.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {
    List<Livro> findByTituloContainingIgnoreCase(String titulo);
    List<Livro> findByAutorContainingIgnoreCase(String autor);

    @Query("SELECT l FROM Livro l WHERE " +
           "(:busca = '' OR LOWER(l.titulo) LIKE LOWER(CONCAT('%', :busca, '%')) " +
           "   OR LOWER(l.autor) LIKE LOWER(CONCAT('%', :busca, '%'))) AND " +
           "(:genero = '' OR l.genero = :genero) AND " +
           "(:disponivel IS NULL OR " +
           "   (:disponivel = true AND l.quantidadeDisponivel > 0) OR " +
           "   (:disponivel = false AND l.quantidadeDisponivel <= 0))")
    List<Livro> buscarComFiltros(@Param("busca") String busca,
                                 @Param("genero") String genero,
                                 @Param("disponivel") Boolean disponivel);
}
