package com.brunocorreia.feiraoVeiculos.principal;

import com.brunocorreia.feiraoVeiculos.model.Dados;
import com.brunocorreia.feiraoVeiculos.model.DadosVeiculos;
import com.brunocorreia.feiraoVeiculos.model.Modelos;
import com.brunocorreia.feiraoVeiculos.model.TipoVeiculo;
import com.brunocorreia.feiraoVeiculos.services.BuscaDados;
import com.brunocorreia.feiraoVeiculos.services.ConversorDados;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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

        List<Dados> marcas = buscarLista(enderecoMarcas);
        exibirOpcoes("Marcas de " + tipo.getDescricao(), marcas);
        Dados marca = escolherOpcao("marca", marcas);

        String enderecoModelos = enderecoMarcas + "/" + marca.codigo() + "/modelos";
        List<Dados> modelos = filtrarPorNome(buscarModelos(enderecoModelos));
        exibirOpcoes("Modelos de " + marca.nome(), modelos);
        if (modelos.isEmpty()) {
            System.out.println("Nenhum modelo encontrado.");
            return;
        }
        Dados modelo = escolherOpcao("modelo", modelos);

        String enderecoAnos = enderecoModelos + "/" + modelo.codigo() + "/anos";
        List<DadosVeiculos> veiculos = buscarVeiculos(enderecoAnos, buscarLista(enderecoAnos));

        exibirVeiculos(modelo, veiculos);
    }

    // ---------- Busca de dados ----------

    private List<Dados> buscarLista(String endereco) {
        String json = consumo.buscaDados(endereco);
        return conversor.converteLista(json, Dados.class);
    }

    private List<Dados> buscarModelos(String endereco) {
        String json = consumo.buscaDados(endereco);
        return conversor.converteDados(json, Modelos.class).modelos();
    }

    private List<DadosVeiculos> buscarVeiculos(String enderecoAnos, List<Dados> anos) {
        return anos.stream()
                .map(ano -> conversor.converteDados(
                        consumo.buscaDados(enderecoAnos + "/" + ano.codigo()), DadosVeiculos.class))
                .toList();
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

    private Dados escolherOpcao(String rotulo, List<Dados> opcoes) {
        while (true) {
            System.out.println("Digite o código do(a) " + rotulo + ": ");
            String codigo = leitura.nextLine().trim();
            Optional<Dados> escolhida = opcoes.stream()
                    .filter(d -> d.codigo().equals(codigo))
                    .findFirst();
            if (escolhida.isPresent()) {
                return escolhida.get();
            }
            System.out.println("Código inválido.");
        }
    }

    private List<Dados> filtrarPorNome(List<Dados> opcoes) {
        System.out.println("Digite um trecho do nome do modelo (ou Enter para listar todos): ");
        String trecho = leitura.nextLine().trim().toLowerCase();
        return opcoes.stream()
                .filter(d -> d.nome().toLowerCase().contains(trecho))
                .toList();
    }

    // ---------- Exibição ----------

    private void exibirOpcoes(String titulo, List<Dados> opcoes) {
        exibirTitulo(titulo);
        opcoes.stream()
                .sorted(Comparator.comparing(Dados::nome))
                .forEach(d -> System.out.printf("  %6s - %s%n", d.codigo(), d.nome()));
    }

    private void exibirVeiculos(Dados modelo, List<DadosVeiculos> veiculos) {
        exibirTitulo("Valores FIPE - " + modelo.nome());
        veiculos.forEach(v -> System.out.printf("  %4d | %-10s | %-15s | FIPE %s | %s%n",
                v.ano(), v.combustivel(), v.valor(), v.codigoFipe(), v.mesReferencia()));
    }

    // ---------- Formatação ----------

    private void exibirTitulo(String titulo) {
        System.out.println("\n=== " + titulo + " ===");
    }
}
