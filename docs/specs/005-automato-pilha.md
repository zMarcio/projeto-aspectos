# Spec 005 — Autômato com Pilha (AP)

| Campo | Valor |
|---|---|
| Status | ✅ Implementado |
| Módulo | 4 do enunciado |
| Endpoint | POST /api/ap |
| Aba | 5. Autômato com Pilha |

## Requisitos e regras

- [x] Usar uma pilha, empilhar e desempilhar.
- [x] Reconhecer `aⁿbⁿ` para `n ≥ 1` com o exemplo fornecido.
- [x] Mostrar símbolo lido, estado, símbolos consumidos e pilha a cada passo.
- Cada transição recebe origem, leitura, topo, destino e substituição do topo. O primeiro símbolo da substituição fica no topo. Use `λ` como leitura vazia ou como substituição para apenas desempilhar.
- As transições são determinísticas. Para um mesmo estado e topo, um movimento `λ` é exclusivo. Sem ele, cada símbolo lido tem no máximo uma transição.
- A aceitação exige cadeia consumida e estado final. Sem uma transição aplicável fora dessa condição, a execução rejeita. O limite é de 1.000 transições ou 1.000 símbolos na pilha.

## Algoritmo e contrato

O simulador consulta as transições por estado e topo e escolhe a leitura disponível. Usa `ArrayDeque` para substituir o topo. O registro inicial tem número 0 e os demais registram a configuração após cada transição.

A requisição do exemplo está em [ap-enunciado.json](../../src/main/resources/static/exemplos/ap-enunciado.json). A resposta contém `status`, `mensagem` e `passos`. Cada passo traz `numero`, `simbolo`, `estado`, `consumidos` e `pilha` em ordem do topo para a base.

## Critérios de aceite

- O exemplo aceita `ab` e `aabb` e rejeita `λ`, `aab`, `abb` e `abab`.
- Para `aabb`, a pilha chega a `[A, A, Z]` e termina em `[Z]` depois do passo `λ`.
- Conflitos de determinismo retornam HTTP 400. Um ciclo que consome o orçamento produz `LIMITE_ATINGIDO`.

## Complexidade

Cada passo custa proporcionalmente ao tamanho da substituição e da pilha registrada. Para o exemplo `aⁿbⁿ`, a execução faz `Θ(n)` transições na aceitação e o rastro completo custa `Θ(n²)`. [Tabela de complexidade](007-complexidade.md)

## Histórico

| Data | O que mudou |
|---|---|
| 2026-11-20 | Rascunho criado. |
| 2026-11-27 | AP determinístico integrado, com navegação pelo rastro da pilha. |
