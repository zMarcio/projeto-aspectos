# Spec 005 — Autômato com Pilha (AP)

| Campo      | Valor |
|------------|-------|
| Status     | ⏳ Pendente (não iniciado) |
| Módulo     | Módulo 4 do enunciado |
| Endpoint   | `POST /api/ap` (proposto) |
| Aba        | `5. Autômato com Pilha` (proposta) |

> Rascunho. Reaproveite `DefinicaoAutomato` (pacote `automato`) para Q, Σ, q0 e F.

## 2. Requisitos (do enunciado)
- [ ] Pilha, empilhar, desempilhar
- [ ] Linguagem mínima: L = { aⁿbⁿ | n ≥ 1 }
- [ ] Mostrar a cada passo: leitura (símbolo lido), estado, conteúdo da pilha

## 3. Entradas propostas
Igual ao AFD + alfabeto da pilha e transições `δ(q, a, X) = (p, γ)`:
```json
{
  "estados": ["q0","q1","q2"], "alfabeto": ["a","b"], "alfabetoPilha": ["A","Z"],
  "estadoInicial": "q0", "estadosFinais": ["q2"], "simboloInicialPilha": "Z",
  "transicoes": [
    {"origem":"q0","simbolo":"a","topo":"Z","destino":"q0","empilha":"AZ"},
    {"origem":"q0","simbolo":"a","topo":"A","destino":"q0","empilha":"AA"},
    {"origem":"q0","simbolo":"b","topo":"A","destino":"q1","empilha":"λ"},
    {"origem":"q1","simbolo":"b","topo":"A","destino":"q1","empilha":"λ"},
    {"origem":"q1","simbolo":"λ","topo":"Z","destino":"q2","empilha":"Z"}
  ],
  "cadeia": "aabb"
}
```

## 4. Algoritmo sugerido
Começar determinístico (como o AFD, com `java.util.ArrayDeque` como pilha): a cada passo procura a
transição para (estado, símbolo lido ou λ, topo); desempilha o topo e empilha `empilha` (da direita
para a esquerda). Aceita por estado final ao fim da cadeia. Passo registra `estado`, `lido`, `pilha`.

## 7. Complexidade (a preencher)
AP determinístico: Θ(n) passos.

## 11. Histórico
| Data | O que mudou |
|------|-------------|
| 2026-11-20 | Rascunho criado. |
