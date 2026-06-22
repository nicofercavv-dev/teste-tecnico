package com.attus.triagem_api.infrastructure.exception;

public class ProcessoNaoEncontradoException extends RuntimeException {
    public ProcessoNaoEncontradoException(Long id) {
        super("Processo com ID " + id + " não foi encontrado.");
    }
}
