# API FIPE — endpoints consumidos

O projeto usa a API pública e gratuita da **Parallelum**, que expõe os dados da Tabela FIPE em JSON. Ela não exige autenticação.

- Documentação: <https://deividfortuna.github.io/fipe/>
- URL base usada: `https://parallelum.com.br/fipe/api/v1/`

← [Voltar ao README](../README.md)

---

## Hierarquia de recursos

Cada nível da URL corresponde a uma escolha do usuário:

```
/{tipo}/marcas
/{tipo}/marcas/{codMarca}/modelos
/{tipo}/marcas/{codMarca}/modelos/{codModelo}/anos
/{tipo}/marcas/{codMarca}/modelos/{codModelo}/anos/{codAno}
```

`{tipo}` vem do `enum TipoVeiculo`: `carros`, `motos` ou `caminhoes`.

---

## 1. Marcas

```http
GET /carros/marcas
```

```json
[
  { "codigo": "1",  "nome": "Acura" },
  { "codigo": "21", "nome": "Fiat" }
]
```

**Mapeamento:** `List<Dados>` via `converteLista(json, Dados.class)`.

---

## 2. Modelos de uma marca

```http
GET /carros/marcas/21/modelos
```

```json
{
  "modelos": [
    { "codigo": 7540, "nome": "MOBI LIKE 1.0 Fire Flex 5p." },
    { "codigo": 7541, "nome": "MOBI LIKE ON 1.0 Fire Flex 5p." }
  ],
  "anos": [
    { "codigo": "32000-6", "nome": "32000 Híbrido" }
  ]
}
```

**Mapeamento:** `Modelos` via `converteDados(json, Modelos.class)`. Só a lista `modelos` é usada. O campo `anos` é ignorado por `@JsonIgnoreProperties(ignoreUnknown = true)`.

> Aqui o `codigo` dos modelos vem como **número**, e não como texto. O Jackson converte o valor para o `String codigo` do `record Dados` automaticamente.

---

## 3. Anos disponíveis de um modelo

```http
GET /carros/marcas/21/modelos/7540/anos
```

```json
[
  { "codigo": "32000-5", "nome": "32000 Flex" },
  { "codigo": "2024-5",  "nome": "2024 Flex" }
]
```

**Mapeamento:** `List<Dados>`. O `codigo` segue o formato `{ano}-{código do combustível}` (por exemplo, `5` = Flex), e `32000` indica veículo **zero km**.

---

## 4. Valor FIPE de um modelo em um ano

```http
GET /carros/marcas/21/modelos/7540/anos/2024-5
```

```json
{
  "TipoVeiculo": 1,
  "Valor": "R$ 55.696,00",
  "Marca": "Fiat",
  "Modelo": "MOBI LIKE 1.0 Fire Flex 5p.",
  "AnoModelo": 2024,
  "Combustivel": "Flex",
  "CodigoFipe": "001461-3",
  "MesReferencia": "outubro de 2026",
  "SiglaCombustivel": "F"
}
```

**Mapeamento:** `DadosVeiculos` via `converteDados(json, DadosVeiculos.class)`:

| Campo da API | Campo no `record` |
|---|---|
| `Valor` | `valor` |
| `Marca` | `marca` |
| `Modelo` | `modelo` |
| `AnoModelo` | `ano` |
| `Combustivel` | `combustivel` |
| `CodigoFipe` | `codigoFipe` |
| `MesReferencia` | `mesReferencia` |

Os campos `TipoVeiculo` e `SiglaCombustivel` são ignorados.

---

## Observações de uso

- A aplicação faz **uma requisição por ano** do modelo escolhido. Um modelo com 15 anos gera 15 chamadas ao endpoint 4.
- A API pública tem limite de requisições. Em uso intenso, ela pode responder com erro temporariamente.
- Os valores são atualizados mensalmente pela FIPE. O campo `MesReferencia` informa a qual mês o preço se refere.
