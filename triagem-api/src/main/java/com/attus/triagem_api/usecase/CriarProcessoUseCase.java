package com.attus.triagem_api.usecase;

import com.attus.triagem_api.domain.model.Processo;
import com.attus.triagem_api.domain.model.StatusProcesso;
import com.attus.triagem_api.domain.repository.ProcessoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
public class CriarProcessoUseCase {

    private final ProcessoRepository processoRepository;

    public CriarProcessoUseCase(ProcessoRepository processoRepository) {
        this.processoRepository = processoRepository;
    }

    public Processo executar(Processo novoProcesso) {
        log.info("Iniciando triagem do novo processo número: {}", novoProcesso.getNumeroProcesso());

        // Regra de Negócio: Valida duplicidade
        if (processoRepository.buscarPorNumero(novoProcesso.getNumeroProcesso()).isPresent()) {
            log.error("Falha ao criar processo: Número {} já está cadastrado no sistema", novoProcesso.getNumeroProcesso());
            throw new IllegalArgumentException("Já existe um processo cadastrado com este número.");
        }

        // Regra de Negócio: Todo processo recém-criado na triagem entra como PENDENTE
        novoProcesso.setStatus(StatusProcesso.PENDENTE);
        if (novoProcesso.getDataRecebimento() == null) {
            novoProcesso.setDataRecebimento(LocalDate.now());
        }

        Processo processoSalvo = processoRepository.salvar(novoProcesso);

        log.info("Processo número {} salvo com sucesso na triagem. ID gerado: {}",
                processoSalvo.getNumeroProcesso(), processoSalvo.getId());

        return processoSalvo;
    }
}