package com.attus.triagem_api.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Processo {
    private Long id;
    private String numeroProcesso;
    private TipoAcao tipoAcao;
    private StatusProcesso status;
    private UrgenciaProcesso urgencia;
    private LocalDate dataRecebimento;
    private String descricaoResumida;
}
