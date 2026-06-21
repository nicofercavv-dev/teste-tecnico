package com.attus.triagem_api.infrastructure.controller.dto;

import com.attus.triagem_api.domain.model.TipoAcao;
import com.attus.triagem_api.domain.model.UrgenciaProcesso;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessoRequestDTO {

    @NotBlank(message = "O número do processo é obrigatório.")
    // Padrão CNJ simplificado (ex: 1234567-12.2026.8.00.0000) ou texto livre restrito
    @Size(min = 10, max = 25, message = "O número do processo deve ter entre 10 e 25 caracteres.")
    private String numeroProcesso;

    @NotNull(message = "O tipo de ação judicial é obrigatório.")
    private TipoAcao tipoAcao;

    @NotNull(message = "O nível de urgência é obrigatório.")
    private UrgenciaProcesso urgencia;

    private LocalDate dataRecebimento;

    @Size(max = 500, message = "A descrição resumida não pode passar de 500 caracteres.")
    private String descricaoResumida;
}