package com.brunocorreia.feiraoVeiculos.services;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.type.CollectionType;

import java.util.List;

public class ConversorDados implements IConversorDados {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public <T> T converteDados(String json, Class<T> classe) {
        return objectMapper.readValue(json, classe);
    }

    @Override
    public <T> List<T> converteLista(String json, Class<T> classe) {
        CollectionType lista = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, classe);
        return objectMapper.readValue(json, lista);
    }
}
