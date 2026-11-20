# Spec 006 — Máquina de Turing

| Campo      | Valor |
|------------|-------|
| Status     | ⏳ Pendente de integração (existe protótipo em `../simulador-maquina-turing-web`) |
| Módulo     | Módulo 5 do enunciado |
| Endpoint   | `POST /api/turing` (proposto; no protótipo é `/api/simular`) |
| Aba        | `6. Máquina de Turing` (proposta) |

## 2. Requisitos (do enunciado)
- [ ] Opção A: reconhecer L = { w#w }   **ou**
- [x] Opção B: somar dois números em unário (`000100` = 3+2 → `00000`) — **feito no protótipo**
- [x] Mostrar a fita (bônus) — **feito no protótipo**

## 3. O que já existe no protótipo
`Fita`, `MaquinaTuring`, `Transicao` (usa Lombok), `Passo`, `ResultadoSimulacao`, `SimulacaoRequest`,
`SimuladorController` (com `@ExceptionHandler` próprio) e um `index.html` completo com fita animada.

## 4. Como integrar
Passo a passo na seção "Exemplo real: trazer a Máquina de Turing" de
[`docs/COMO-ADICIONAR-MODULO.md`](../COMO-ADICIONAR-MODULO.md).

## 7. Complexidade (a preencher)
Soma unária: Θ(n) passos (uma passada para a direita e um passo para a esquerda), n = |fita|.

## 11. Histórico
| Data | O que mudou |
|------|-------------|
| 2026-11-20 | Rascunho criado apontando para o protótipo existente. |
