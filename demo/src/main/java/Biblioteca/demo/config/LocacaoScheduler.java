package Biblioteca.demo.config;

import Biblioteca.demo.model.Locacao;
import Biblioteca.demo.model.enums.StatusLocacao;
import Biblioteca.demo.repository.LocacaoRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class LocacaoScheduler {

    private final LocacaoRepository locacaoRepository;

    public LocacaoScheduler(LocacaoRepository locacaoRepository) {
        this.locacaoRepository = locacaoRepository;
    }

    // Roda todo dia à meia-noite
    @Scheduled(cron = "0 0 0 * * *")
    public void atualizarLocacoesAtrasadas() {
        List<Locacao> atrasadas = locacaoRepository
                .findByStatusAndDataDevolucaoPrevistaLessThan(StatusLocacao.ATIVA, LocalDate.now());

        atrasadas.forEach(l -> l.setStatus(StatusLocacao.ATRASADA));
        locacaoRepository.saveAll(atrasadas);

        if (!atrasadas.isEmpty()) {
            System.out.println("[Scheduler] " + atrasadas.size() + " locação(ões) marcada(s) como ATRASADA.");
        }
    }
}
