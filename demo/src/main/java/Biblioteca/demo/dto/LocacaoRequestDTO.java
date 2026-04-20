package Biblioteca.demo.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class LocacaoRequestDTO {

    @NotNull(message = "ID do livro é obrigatório")
    private Long livroId;

    @NotNull(message = "ID do usuário é obrigatório")
    private Long usuarioId;

    @NotNull(message = "Data de devolução prevista é obrigatória")
    private LocalDate dataDevolucaoPrevista;

    public LocacaoRequestDTO() {}

    public Long getLivroId() { return livroId; }
    public void setLivroId(Long livroId) { this.livroId = livroId; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public LocalDate getDataDevolucaoPrevista() { return dataDevolucaoPrevista; }
    public void setDataDevolucaoPrevista(LocalDate dataDevolucaoPrevista) { this.dataDevolucaoPrevista = dataDevolucaoPrevista; }
}
