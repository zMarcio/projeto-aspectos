# Spec 006 — Máquina de Turing

| Campo | Valor |
|---|---|
| Status | ✅ Implementado |
| Módulo | 5 do enunciado |
| Endpoint | POST /api/turing |
| Aba | 6. Máquina de Turing |

## Requisitos e regras

- [x] Opção B do enunciado, soma em unário. `000100` representa 3 + 2 e produz `00000`.
- [x] Mostrar a fita e a posição do cabeçote a cada passo.
- Na soma unária, cada `0` representa uma unidade, `1` separa as parcelas e uma parcela sem zeros vale zero. O branco `B` é reservado à fita e permitido nas transições.
- Cada transição define estado e símbolo atuais, novo estado, símbolo escrito e movimento `E` ou `D`. Cada par de estado e símbolo tem no máximo uma transição.
- A máquina para em estado de aceitação ou por falta de transição. O limite é de 1.000 transições e a requisição aceita até 1.000 símbolos na entrada.

## Adaptação do protótipo

A fita cresce nas duas direções, com o cabeçote inicialmente na primeira posição da entrada. A máquina utiliza o modelo de fita, configuração e transições do protótipo e mantém o rastro desde o passo 0.

A tabela padrão percorre a primeira parcela, converte o separador em zero e percorre a segunda parcela até o branco que delimita a entrada. Só então volta uma célula, apaga um zero e entra no estado final. A reserva de `B` impede que uma entrada como `000100B0` faça a máquina confundir um trecho interno com o fim da entrada. Uma máquina personalizada segue a forma de parada definida pelos seus estados.

## Contrato e critérios de aceite

A requisição está em [turing-soma-unaria.json](../../src/main/resources/static/exemplos/turing-soma-unaria.json). A resposta inclui `status`, `mensagem`, `estadoFinal`, `fitaFinal`, `saida` sem os brancos das extremidades e `passos` com os quadros da fita.

- `000100` aceita em 8 transições e produz `00000`. O primeiro branco à direita é lido antes da transição de aceitação.
- `100`, `001` e `1` são exemplos com parcelas zero. `0001001`, `000100a` e `00000` são rejeitados pela máquina de soma.
- `000100B0` retorna HTTP 400. Uma execução que ainda pode avançar quando o limite expira retorna `LIMITE_ATINGIDO`.

## Complexidade

A máquina de soma executa `n+2` transições para uma entrada válida de comprimento `n`. Copiar a fita a cada passo eleva tempo e espaço do rastro para `Θ(n²)`. [Tabela de complexidade](007-complexidade.md)

## Histórico

| Data | O que mudou |
|---|---|
| 2026-11-20 | Rascunho criado apontando para o protótipo existente. |
| 2026-11-27 | Motor do protótipo adaptado, com soma unária, rota e visualização da fita. |
