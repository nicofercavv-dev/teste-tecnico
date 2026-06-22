package com.attus.triagem_api.usecase;

import com.attus.triagem_api.domain.model.Processo;
import com.attus.triagem_api.domain.model.StatusProcesso;
import com.attus.triagem_api.domain.model.TipoAcao;
import com.attus.triagem_api.domain.model.UrgenciaProcesso;
import com.attus.triagem_api.domain.repository.ProcessoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CriarProcessoUseCaseTest {

    @Mock
    private ProcessoRepository processoRepository;

    @InjectMocks
    private CriarProcessoUseCase useCase;

    private Processo processoInput;

    @BeforeEach
    void setUp() {
        processoInput = new Processo();
        processoInput.setNumeroProcesso("12345671220264013400");
        processoInput.setTipoAcao(TipoAcao.MANDADO_DE_SEGURANCA);
        processoInput.setUrgencia(UrgenciaProcesso.ALTA);
        processoInput.setDescricaoResumida("Pedido de liminar de saúde.");
    }

    @Test
    @DisplayName("Deve inicializar o processo como PENDENTE e salvar com sucesso")
    void deveCriarProcessoComSucesso() {
        when(processoRepository.salvar(any(Processo.class))).thenAnswer(invocation -> {
            Processo processoSalvo = invocation.getArgument(0);
            processoSalvo.setId(1L);
            return processoSalvo;
        });

        Processo resultado = useCase.executar(processoInput);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("12345671220264013400", resultado.getNumeroProcesso());

        assertEquals(StatusProcesso.PENDENTE, resultado.getStatus());

        verify(processoRepository, times(1)).salvar(processoInput);
    }
}