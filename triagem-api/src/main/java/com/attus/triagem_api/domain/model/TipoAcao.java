package com.attus.triagem_api.domain.model;

public enum TipoAcao {
    ACAO_CIVIL_PUBLICA("Ação Civil Pública"),
    ACAO_POPULAR("Ação Popular"),
    ANULACAO_DE_DEBITO_FISCAL("Anulação de Débito Fiscal"),
    DESAPROPRIACAO("Desapropriação"),
    EXECUCAO_FISCAL("Execução Fiscal"),
    MANDADO_DE_SEGURANCA("Mandado de Segurança"),
    PROCEDIMENTO_COMUM_CIVEL("Procedimento Comum Cível"),
    RECUPERACAO_JUDICIAL("Recuperação Judicial"),
    RECLAMACAO_TRABALHISTA("Reclamação Trabalhista");

    private final String descricao;

    TipoAcao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}