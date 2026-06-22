package com.attus.triagem_api.usecase;

import com.attus.triagem_api.domain.model.Processo;
import com.attus.triagem_api.domain.model.StatusProcesso;
import com.attus.triagem_api.domain.repository.ProcessoRepository;
import com.attus.triagem_api.infrastructure.exception.ProcessoNaoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtualizarStatusProcessoUseCaseTest {

    @Mock
    private ProcessoRepository processoRepository;

    @InjectMocks
    private AtualizarStatusProcessoUseCase useCase;

    private Processo processoExemplo;

    @BeforeEach
    void setUp() {
        processoExemplo = new Processo();
        processoExemplo.setId(1L);
        processoExemplo.setNumeroProcesso("12345671220264013400");
        processoExemplo.setStatus(StatusProcesso.PENDENTE);
    }

    @Test
    @DisplayName("Deve atualizar o status do processo com sucesso")
    void deveAtualizarStatusComSucesso() {
        when(processoRepository.buscarPorId(1L)).thenReturn(Optional.of(processoExemplo));
        when(processoRepository.salvar(any(Processo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Processo resultado = useCase.executar(1L, StatusProcesso.EM_ANALISE);

        assertNotNull(resultado);
        assertEquals(StatusProcesso.EM_ANALISE, resultado.getStatus());
        verify(processoRepository, times(1)).buscarPorId(1L);
        verify(processoRepository, times(1)).salvar(processoExemplo);
    }

    @Test
    @DisplayName("Deve lançar exceção customizada quando o processo não existir")
    void deveLancarExcecaoQuandoProcessoNaoEncontrado() {
        when(processoRepository.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThrows(ProcessoNaoEncontradoException.class, () -> {
            useCase.executar(99L, StatusProcesso.EM_ANALISE);
        });

        verify(processoRepository, times(1)).buscarPorId(99L);
        verify(processoRepository, never()).salvar(any());
    }

    @Test
    @DisplayName("Deve proibir alteração de status se o processo já estiver concluído")
    void deveLancarExcecaoAoTentarReverterProcessoConcluido() {
        processoExemplo.setStatus(StatusProcesso.CONCLUIDO);
        when(processoRepository.buscarPorId(1L)).thenReturn(Optional.of(processoExemplo));

        IllegalStateException excecao = assertThrows(IllegalStateException.class, () -> {
            useCase.executar(1L, StatusProcesso.PENDENTE);
        });

        assertEquals("Não é permitido reverter um processo concluído para pendente.", excecao.getMessage());
        verify(processoRepository, never()).salvar(any());
    }
}