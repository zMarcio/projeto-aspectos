# Spec 007 — Complexidade Computacional (resumo)

| Campo      | Valor |
|------------|-------|
| Status     | 🚧 Parcial — módulos 1, 2 e 3 analisados |
| Módulo     | Módulo 6 do enunciado |

Tabela consolidada para o documento técnico e a apresentação. Detalhes e justificativas
estão na seção 7 de cada spec. Notação: **Ω** = limite inferior (melhor caso), **Θ** = caso médio
(limite justo), **O** = limite superior (pior caso).

| Módulo | Algoritmo | Melhor (Ω) | Médio (Θ) | Pior (O) |
|--------|-----------|-----------|-----------|----------|
| Linguagens | Validar w ∈ Σ* | Ω(n) | Θ(n) | O(n) |
| Linguagens | Tamanho \|w\| | Ω(1) | Θ(1) | O(1) |
| Linguagens | Concatenação | Ω(n+m) | Θ(n+m) | O(n+m) |
| Linguagens | w ∈ L | Ω(n) | Θ(n) | O(n·\|L\|) |
| Linguagens | Gerar Σ* até tamanho m | Ω(m·kᵐ) | Θ(m·kᵐ) | O(m·kᵐ) |
| AFD | Executar cadeia | Ω(1) | Θ(n) | O(n) |
| AFN | Fecho-λ | Ω(\|C\|) | Θ(\|Q\|+\|δλ\|) | O(\|Q\|²·log\|Q\|) |
| AFN | Executar cadeia | Ω(1) | Θ(n·\|Q\|) | O(n·\|Q\|²·log\|Q\|) |
| GLC | _a preencher_ | | | |
| AP | _a preencher_ | | | |
| MT | _a preencher_ | | | |

n = |w|, k = |Σ|, |Q| = nº de estados, |L| = nº de palavras de L.
