package com.attus.triagem_api.infrastructure.persistence;

import com.attus.triagem_api.domain.model.Pagina;
import com.attus.triagem_api.domain.model.Processo;
import com.attus.triagem_api.domain.repository.ProcessoRepository;
import com.attus.triagem_api.infrastructure.mapper.ProcessoMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ProcessoRepositoryDatabase implements ProcessoRepository {

    private final SpringDataProcessoRepository springDataRepository;
    private final ProcessoMapper mapper;

    public ProcessoRepositoryDatabase(SpringDataProcessoRepository springDataRepository, ProcessoMapper mapper) {
        this.springDataRepository = springDataRepository;
        this.mapper = mapper;
    }

    @Override
    public Processo salvar(Processo processo) {
        ProcessoEntity entity = mapper.toEntity(processo);
        ProcessoEntity savedEntity = springDataRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Pagina<Processo> listarPaginado(Integer pagina, Integer tamanho) {
        Page<ProcessoEntity> pageEntity = springDataRepository.findAll(PageRequest.of(pagina, tamanho));

        List<Processo> listaDominio = pageEntity.getContent().stream()
                .map(mapper::toDomain).toList();
//                .collect(Collectors.toList());
        return new Pagina<>(
                listaDominio,
                pageEntity.getTotalElements(),
                pageEntity.getNumber(),
                pageEntity.getSize()
        );
    }

    @Override
    public Optional<Processo> buscarPorId(Long id) {
        return springDataRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Processo> buscarPorNumero(String numeroProcesso) {
        return springDataRepository.findByNumeroProcesso(numeroProcesso).map(mapper::toDomain);
    }
}
