package com.attus.triagem_api.infrastructure.controller;

import com.attus.triagem_api.domain.model.Pagina;
import com.attus.triagem_api.domain.model.Processo;
import com.attus.triagem_api.domain.model.StatusProcesso;
import com.attus.triagem_api.infrastructure.controller.dto.ProcessoRequestDTO;
import com.attus.triagem_api.infrastructure.controller.dto.ProcessoResponseDTO;
import com.attus.triagem_api.infrastructure.mapper.ProcessoHttpMapper;
import com.attus.triagem_api.usecase.AtualizarStatusProcessoUseCase;
import com.attus.triagem_api.usecase.CriarProcessoUseCase;
import com.attus.triagem_api.usecase.ListarProcessosUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/processos")
@Tag(name = "Processos", description = "Endpoints para gerenciamento e triagem de processos judiciais")
public class ProcessoController {

    private final CriarProcessoUseCase criarProcessoUseCase;
    private final ListarProcessosUseCase listarProcessosUseCase;
    private final AtualizarStatusProcessoUseCase atualizarStatusProcessoUseCase;
    private final ProcessoHttpMapper httpMapper;

    public ProcessoController(CriarProcessoUseCase criarProcessoUseCase,
                              ListarProcessosUseCase listarProcessosUseCase,
                              AtualizarStatusProcessoUseCase atualizarStatusProcessoUseCase,
                              ProcessoHttpMapper httpMapper) {
        this.criarProcessoUseCase = criarProcessoUseCase;
        this.listarProcessosUseCase = listarProcessosUseCase;
        this.atualizarStatusProcessoUseCase = atualizarStatusProcessoUseCase;
        this.httpMapper = httpMapper;
    }

    @Operation(summary = "Cadastrar um novo processo", description = "Realiza a triagem inicial e salva um processo com o número padronizado do CNJ.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Processo criado e triado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de requisição inválidos ou número CNJ malformado")
    })
    @PostMapping
    public ResponseEntity<ProcessoResponseDTO> criarProcesso(@RequestBody @Valid ProcessoRequestDTO request) {
        Processo dominioInput = httpMapper.toDomain(request);
        Processo dominioOutput = criarProcessoUseCase.executar(dominioInput);
        return ResponseEntity.status(HttpStatus.CREATED).body(httpMapper.toResponse(dominioOutput));
    }

    @Operation(summary = "Listar processos paginados", description = "Recupera uma lista de processos do banco de dados utilizando paginação Server-Side.")
    @GetMapping
    public ResponseEntity<Pagina<ProcessoResponseDTO>> listar(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "5") Integer size) {
        Pagina<Processo> resposta = listarProcessosUseCase.executar(page, size);

        List<ProcessoResponseDTO> dtoList = resposta.conteudo().stream()
                .map(httpMapper::toResponse)
                .collect(Collectors.toList());

        Pagina<ProcessoResponseDTO> paginaResponse = new Pagina<>(
                dtoList,
                resposta.totalElementos(),
                resposta.paginaAtual(),
                resposta.tamanhoPagina()
        );
        return ResponseEntity.ok(paginaResponse);
    }

    @Operation(
            summary = "Atualizar o status de um processo",
            description = "Modifica o estado atual de um processo na triagem (ex: PENDENTE -> EM_ANALISE -> CONCLUIDO)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Status do processo atualizado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Status fornecido é inválido ou requisição malformada"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Processo não encontrado para atualização"
            )
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<ProcessoResponseDTO> atualizarStatus(
            @Parameter(description = "ID numérico do processo a ser atualizado", example = "1")
            @PathVariable Long id,

            @Parameter(description = "Novo status a ser atribuído ao processo", example = "EM_ANALISE")
            @RequestParam StatusProcesso novoStatus) {
        Processo dominioOutput = atualizarStatusProcessoUseCase.executar(id, novoStatus);
        return ResponseEntity.ok(httpMapper.toResponse(dominioOutput));
    }
}