package com.devinhouse.labsky.enums;

public enum Classificacao {
    VIP("VIP"),
    OURO("OURO"),
    PRATA("PRATA"),
    BRONZE("BRONZE"),
    ASSOCIADO("ASSOCIADO");

    private String classificacao;

    Classificacao(String classificacao) {
        this.classificacao = classificacao;
    }

    public String getClassificacao() {
        return classificacao;
    }
}
