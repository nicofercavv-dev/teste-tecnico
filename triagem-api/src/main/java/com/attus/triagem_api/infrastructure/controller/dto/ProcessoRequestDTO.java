package com.attus.triagem_api.infrastructure.controller.dto;

import com.attus.triagem_api.domain.model.TipoAcao;
import com.attus.triagem_api.domain.model.UrgenciaProcesso;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "Número único do processo no padrão CNJ (apenas números)", example = "12345671220264013400")
    @NotBlank(message = "O número do processo é obrigatório.")
    @Size(min = 20, max = 20, message = "O número do processo deve ter 20 caracteres.")
    private String numeroProcesso;

    @Schema(description = "Tipo/Classe da ação judicial", example = "MANDADO_DE_SEGURANCA")
    @NotNull(message = "O tipo de ação judicial é obrigatório.")
    private TipoAcao tipoAcao;

    @Schema(description = "Nível de urgência do processo", example = "MEDIA")
    @NotNull(message = "O nível de urgência é obrigatório.")
    private UrgenciaProcesso urgencia;

    private LocalDate dataRecebimento;

    @Schema(description = "Descrição resumida do processo (opcional) de no máximo 500 caracteres")
    @Size(max = 500, message = "A descrição resumida não pode passar de 500 caracteres.")
    private String descricaoResumida;
}