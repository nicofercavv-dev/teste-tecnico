package com.attus.triagem_api.usecase;

import com.attus.triagem_api.domain.model.Processo;
import com.attus.triagem_api.domain.model.StatusProcesso;
import com.attus.triagem_api.domain.repository.ProcessoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AtualizarStatusProcessoUseCase {

    private final ProcessoRepository processoRepository;

    public AtualizarStatusProcessoUseCase(ProcessoRepository processoRepository) {
        this.processoRepository = processoRepository;
    }

    public Processo executar(Long id, StatusProcesso novoStatus) {
        log.info("Solicitação recebida para alterar status do processo ID {} para {}", id, novoStatus);

        // Busca o processo existente
        Processo processo = processoRepository.buscarPorId(id)
                .orElseThrow(() -> {
                    log.error("Erro de diagnóstico: Processo com ID {} não foi encontrado para atualização.", id);
                    return new RuntimeException("Processo não encontrado.");
                });

        StatusProcesso statusAnterior = processo.getStatus();

        // Regra de Negócio: Se já estiver CONCLUÍDO, não pode voltar para PENDENTE (exemplo de trava jurídica)
        if (statusAnterior == StatusProcesso.CONCLUIDO && novoStatus == StatusProcesso.PENDENTE) {
            log.warn("Tentativa de retrocesso inválida: Processo ID {} já concluído não pode voltar a ser Pendente", id);
            throw new IllegalStateException("Não é permitido reverter um processo concluído para pendente.");
        }

        processo.setStatus(novoStatus);
        Processo processoAtualizado = processoRepository.salvar(processo);

        log.info("Status do processo ID {} (Nº {}) alterado com sucesso de {} para {}",
                id, processoAtualizado.getNumeroProcesso(), statusAnterior, novoStatus);

        return processoAtualizado;
    }
}