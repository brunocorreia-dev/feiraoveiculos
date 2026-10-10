package com.brunocorreia.feiraoVeiculos.services;

import java.util.List;

public interface IConversorDados {
    <T> T converteDados(String json, Class<T> classe);

    <T> List<T> converteLista(String json, Class<T> classe);
}
