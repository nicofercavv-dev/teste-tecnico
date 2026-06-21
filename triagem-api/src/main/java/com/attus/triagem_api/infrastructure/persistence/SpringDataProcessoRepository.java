package com.attus.triagem_api.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataProcessoRepository extends JpaRepository<ProcessoEntity, Long> {
    Optional<ProcessoEntity> findByNumeroProcesso(String numeroProcesso);
}