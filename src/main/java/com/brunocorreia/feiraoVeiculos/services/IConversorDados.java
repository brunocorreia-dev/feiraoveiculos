package com.brunocorreia.feiraoVeiculos.services;

public interface IConversorDados {
    <T> T converteDados(String json, Class<T> classe);
}
