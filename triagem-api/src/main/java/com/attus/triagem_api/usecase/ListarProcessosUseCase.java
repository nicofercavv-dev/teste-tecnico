package com.attus.triagem_api.usecase;

import com.attus.triagem_api.domain.model.Pagina;
import com.attus.triagem_api.domain.model.Processo;
import com.attus.triagem_api.domain.repository.ProcessoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ListarProcessosUseCase {

    private final ProcessoRepository processoRepository;

    public ListarProcessosUseCase(ProcessoRepository processoRepository) {
        this.processoRepository = processoRepository;
    }

    public Pagina<Processo> executar(Integer page, Integer size) {
        log.debug("Buscando listagem geral de processos na base de dados para triagem.");
        Pagina<Processo> pagina = processoRepository.listarPaginado(page, size);
        log.info("Listagem realizada. Total de processos encontrados: {}", pagina.totalElementos());
        return pagina;
    }
}