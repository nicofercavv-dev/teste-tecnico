package com.attus.triagem_api.infrastructure.persistence;

import com.attus.triagem_api.domain.model.StatusProcesso;
import com.attus.triagem_api.domain.model.TipoAcao;
import com.attus.triagem_api.domain.model.UrgenciaProcesso;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "tb_processos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String numeroProcesso;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TipoAcao tipoAcao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusProcesso status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UrgenciaProcesso urgencia;

    @Column(nullable = false)
    private LocalDate dataRecebimento;

    @Column(length = 500)
    private String descricaoResumida;
}