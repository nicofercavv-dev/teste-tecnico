package com.attus.triagem_api.infrastructure.mapper;

import com.attus.triagem_api.domain.model.Processo;
import com.attus.triagem_api.infrastructure.persistence.ProcessoEntity;
import org.springframework.stereotype.Component;

@Component
public class ProcessoMapper {

    public ProcessoEntity toEntity(Processo dominio) {
        if (dominio == null) return null;

        return new ProcessoEntity(
                dominio.getId(),
                dominio.getNumeroProcesso(),
                dominio.getTipoAcao(),
                dominio.getStatus(),
                dominio.getUrgencia(),
                dominio.getDataRecebimento(),
                dominio.getDescricaoResumida()
        );
    }

    public Processo toDomain(ProcessoEntity entity) {
        if (entity == null) return null;

        return new Processo(
                entity.getId(),
                entity.getNumeroProcesso(),
                entity.getTipoAcao(),
                entity.getStatus(),
                entity.getUrgencia(),
                entity.getDataRecebimento(),
                entity.getDescricaoResumida()
        );
    }
}