package com.attus.triagem_api.infrastructure.mapper;

import com.attus.triagem_api.domain.model.Processo;
import com.attus.triagem_api.infrastructure.controller.dto.ProcessoRequestDTO;
import com.attus.triagem_api.infrastructure.controller.dto.ProcessoResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ProcessoHttpMapper {

    public Processo toDomain(ProcessoRequestDTO request) {
        if (request == null) return null;

        Processo dominio = new Processo();
        dominio.setNumeroProcesso(request.getNumeroProcesso());
        dominio.setTipoAcao(request.getTipoAcao());
        dominio.setUrgencia(request.getUrgencia());
        dominio.setDataRecebimento(request.getDataRecebimento());
        dominio.setDescricaoResumida(request.getDescricaoResumida());
        return dominio;
    }

    public ProcessoResponseDTO toResponse(Processo dominio) {
        if (dominio == null) return null;

        return new ProcessoResponseDTO(
                dominio.getId(),
                dominio.getNumeroProcesso(),
                dominio.getTipoAcao(),
                dominio.getStatus(),
                dominio.getUrgencia(),
                dominio.getDataRecebimento(),
                dominio.getDescricaoResumida()
        );
    }
}