package com.attus.triagem_api.domain.model;

import java.util.List;

public record Pagina<T>(List<T> conteudo, Long totalElementos, Integer paginaAtual, Integer tamanhoPagina) {}
