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
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/processos")
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

    @PostMapping
    public ResponseEntity<ProcessoResponseDTO> criarProcesso(@RequestBody @Valid ProcessoRequestDTO request) {
        Processo dominioInput = httpMapper.toDomain(request);
        Processo dominioOutput = criarProcessoUseCase.executar(dominioInput);
        return ResponseEntity.status(HttpStatus.CREATED).body(httpMapper.toResponse(dominioOutput));
    }

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

    @PatchMapping("/{id}/status")
    public ResponseEntity<ProcessoResponseDTO> atualizarStatus(@PathVariable Long id,
                                                               @RequestParam StatusProcesso novoStatus) {
        Processo dominioOutput = atualizarStatusProcessoUseCase.executar(id, novoStatus);
        return ResponseEntity.ok(httpMapper.toResponse(dominioOutput));
    }
}