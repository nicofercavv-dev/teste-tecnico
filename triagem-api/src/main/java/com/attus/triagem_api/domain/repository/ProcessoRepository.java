package com.attus.triagem_api.domain.repository;

import com.attus.triagem_api.domain.model.Pagina;
import com.attus.triagem_api.domain.model.Processo;

import java.util.List;
import java.util.Optional;

public interface ProcessoRepository {
    Processo salvar(Processo processo);
    Pagina<Processo> listarPaginado(Integer pagina, Integer tamanho);
    Optional<Processo> buscarPorId(Long id);
    Optional<Processo> buscarPorNumero(String numeroProcesso);
}