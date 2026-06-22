package com.attus.triagem_api.infrastructure.controller;

import com.attus.triagem_api.BaseIntegrationTest;
import com.attus.triagem_api.domain.model.StatusProcesso;
import com.attus.triagem_api.domain.model.TipoAcao;
import com.attus.triagem_api.domain.model.UrgenciaProcesso;
import com.attus.triagem_api.infrastructure.persistence.ProcessoEntity;
import com.attus.triagem_api.infrastructure.persistence.SpringDataProcessoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class ProcessoControllerIT extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SpringDataProcessoRepository jpaRepository;

    private ProcessoEntity entidadeSalva;

    @BeforeEach
    void setUp() {
        jpaRepository.deleteAll();

        ProcessoEntity processo = new ProcessoEntity();
        processo.setNumeroProcesso("12345671220264013400");
        processo.setTipoAcao(TipoAcao.EXECUCAO_FISCAL);
        processo.setStatus(StatusProcesso.PENDENTE);
        processo.setUrgencia(UrgenciaProcesso.MEDIA);
        processo.setDataRecebimento(LocalDate.now());

        entidadeSalva = jpaRepository.save(processo);
    }

    @Test
    @DisplayName("POST /api/processos - Deve criar um processo com sucesso e retornar 201")
    void deveCriarProcessoComSucesso() throws Exception {
        String jsonRequest = """
                {
                    "numeroProcesso": "12345671220264013401",
                    "tipoAcao": "MANDADO_DE_SEGURANCA",
                    "urgencia": "ALTA",
                    "descricaoResumida": "Pedido de liminar para fornecimento de medicamento."
                }
                """;

        mockMvc.perform(post("/api/processos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.numeroProcesso", is("12345671220264013401")))
                .andExpect(jsonPath("$.status", is("PENDENTE")));
    }

    @Test
    @DisplayName("GET /api/processos - Deve retornar os processos paginados estruturados no formato Pagina")
    void deveListarProcessosPaginadosComSucesso() throws Exception {
        ProcessoEntity p2 = new ProcessoEntity();
        p2.setNumeroProcesso("00000000000000000002");
        p2.setTipoAcao(TipoAcao.EXECUCAO_FISCAL);
        p2.setStatus(StatusProcesso.PENDENTE);
        p2.setUrgencia(UrgenciaProcesso.MEDIA);
        p2.setDataRecebimento(LocalDate.now());
        jpaRepository.save(p2);

        ProcessoEntity p3 = new ProcessoEntity();
        p3.setNumeroProcesso("00000000000000000003");
        p3.setTipoAcao(TipoAcao.ACAO_POPULAR);
        p3.setStatus(StatusProcesso.PENDENTE);
        p3.setUrgencia(UrgenciaProcesso.ALTA);
        p3.setDataRecebimento(LocalDate.now());
        jpaRepository.save(p3);

        mockMvc.perform(get("/api/processos")
                        .param("page", "0")
                        .param("size", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElementos", is(3)))
                .andExpect(jsonPath("$.paginaAtual", is(0)))
                .andExpect(jsonPath("$.tamanhoPagina", is(2)))
                .andExpect(jsonPath("$.conteudo.length()", is(2)));
    }

    @Test
    @DisplayName("PATCH /api/processos/{id}/status - Deve alterar status no H2 de teste")
    void deveAtualizarStatusViaApi() throws Exception {
        mockMvc.perform(patch("/api/processos/" + entidadeSalva.getId() + "/status")
                        .param("novoStatus", "EM_ANALISE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("EM_ANALISE")));
    }
}