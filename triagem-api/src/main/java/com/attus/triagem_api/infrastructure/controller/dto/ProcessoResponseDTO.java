package com.attus.triagem_api.infrastructure.controller.dto;

import com.attus.triagem_api.domain.model.StatusProcesso;
import com.attus.triagem_api.domain.model.TipoAcao;
import com.attus.triagem_api.domain.model.UrgenciaProcesso;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessoResponseDTO {
    private Long id;
    private String numeroProcesso;
    private TipoAcao tipoAcao;
    private StatusProcesso status;
    private UrgenciaProcesso urgencia;
    private LocalDate dataRecebimento;
    private String descricaoResumida;
}