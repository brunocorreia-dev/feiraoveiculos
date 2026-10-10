package com.brunocorreia.feiraoVeiculos.principal;

import com.brunocorreia.feiraoVeiculos.model.TipoVeiculo;
import com.brunocorreia.feiraoVeiculos.services.BuscaDados;
import com.brunocorreia.feiraoVeiculos.services.ConversorDados;

import java.util.Scanner;

public class Principal {

    private static final String ENDERECO = "https://parallelum.com.br/fipe/api/v1/";

    private final Scanner leitura = new Scanner(System.in);
    private final BuscaDados consumo = new BuscaDados();
    private final ConversorDados conversor = new ConversorDados();

    public static void main(String[] args) {
        new Principal().exibeMenu();
    }

    public void exibeMenu() {
        TipoVeiculo tipo = lerTipoVeiculo();
        String enderecoMarcas = ENDERECO + tipo.getCaminho() + "/marcas";
    }

    // ---------- Leitura do usuário ----------

    private TipoVeiculo lerTipoVeiculo() {
        TipoVeiculo[] tipos = TipoVeiculo.values();
        exibirTitulo("Tipos de veículo");
        for (int i = 0; i < tipos.length; i++) {
            System.out.printf("  %d. %s%n", i + 1, tipos[i].getDescricao());
        }

        while (true) {
            System.out.println("Escolha o tipo de veículo: ");
            String opcao = leitura.nextLine().trim();
            for (int i = 0; i < tipos.length; i++) {
                if (opcao.equals(String.valueOf(i + 1))) {
                    return tipos[i];
                }
            }
            System.out.println("Opção inválida.");
        }
    }

    // ---------- Formatação ----------

    private void exibirTitulo(String titulo) {
        System.out.println("\n=== " + titulo + " ===");
    }
}
