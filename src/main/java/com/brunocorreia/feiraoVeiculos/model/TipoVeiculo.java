package com.brunocorreia.feiraoVeiculos.model;

public enum TipoVeiculo {
    CARRO("Carro", "carros"),
    MOTO("Moto", "motos"),
    CAMINHAO("Caminhão", "caminhoes");

    private final String descricao;
    private final String caminho;

    TipoVeiculo(String descricao, String caminho) {
        this.descricao = descricao;
        this.caminho = caminho;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getCaminho() {
        return caminho;
    }
}
