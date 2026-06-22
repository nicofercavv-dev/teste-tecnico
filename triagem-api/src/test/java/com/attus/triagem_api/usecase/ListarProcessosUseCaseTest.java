package com.attus.triagem_api.usecase;

import com.attus.triagem_api.domain.model.Pagina;
import com.attus.triagem_api.domain.model.Processo;
import com.attus.triagem_api.domain.repository.ProcessoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarProcessosUseCaseTest {

    @Mock
    private ProcessoRepository processoRepository;

    @InjectMocks
    private ListarProcessosUseCase useCase;

    @Test
    @DisplayName("Deve chamar o repositório com os parâmetros de paginação corretos e retornar a página")
    void deveRetornarListaDeProcessosPaginada() {
        int paginaAlvo = 0;
        int tamanhoAlvo = 5;

        List<Processo> processosFake = List.of(new Processo(), new Processo());
        Pagina<Processo> paginaFake = new Pagina<>(processosFake, 2L, paginaAlvo, tamanhoAlvo);

        when(processoRepository.listarPaginado(paginaAlvo, tamanhoAlvo)).thenReturn(paginaFake);

        Pagina<Processo> resultado = useCase.executar(paginaAlvo, tamanhoAlvo);

        assertNotNull(resultado);
        assertEquals(2L, resultado.totalElementos());
        assertEquals(paginaAlvo, resultado.paginaAtual());
        assertEquals(tamanhoAlvo, resultado.tamanhoPagina());
        assertEquals(2, resultado.conteudo().size());

        verify(processoRepository, times(1)).listarPaginado(paginaAlvo, tamanhoAlvo);
    }
}