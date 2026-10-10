package com.brunocorreia.feiraoVeiculos.services;

import tools.jackson.databind.ObjectMapper;

public class ConversorDados implements IConversorDados {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public <T> T converteDados(String json, Class<T> classe) {
        return objectMapper.readValue(json, classe);
    }
}
