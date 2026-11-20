# Spec 001 — Linguagens Formais

| Campo      | Valor |
|------------|-------|
| Status     | ✅ Implementado |
| Módulo     | Módulo 1 do enunciado |
| Endpoint   | `POST /api/linguagem` |
| Aba        | `1. Linguagens Formais` |

## 1. Objetivo
Permitir definir um alfabeto Σ e uma linguagem L, verificar se uma cadeia w é válida para Σ
e calcular |w|, a concatenação w·w₂, Σ* e Σ+.

## 2. Requisitos (do enunciado)
- [x] Definir um alfabeto Σ
- [x] Definir uma linguagem L (finita, lista de palavras) e dizer se w ∈ L
- [x] Validar se uma cadeia pertence ao alfabeto (w ∈ Σ*)
- [x] Calcular Σ* (até um tamanho n, porque é infinito)
- [x] Calcular Σ+ (até um tamanho n)
- [x] Calcular o tamanho da cadeia
- [x] Calcular a concatenação

## 3. Entradas
| Campo | Tipo | Obrigatório | Exemplo | Observação |
|-------|------|-------------|---------|------------|
| `alfabeto` | lista de texto | sim | `["a","b"]` | cada símbolo com 1 caractere, sem repetir, `λ` proibido |
| `linguagem` | lista de texto | não | `["ab","ba"]` | toda palavra precisa usar só símbolos de Σ; `λ` = palavra vazia |
| `cadeia` | texto | não | `"abba"` | vazio ou `λ` = cadeia vazia |
| `cadeia2` | texto | não | `"ab"` | usada na concatenação |
| `tamanhoMaximo` | número | não (padrão 3) | `3` | Σ*/Σ+ são gerados até esse tamanho |

## 4. Regras e validações
- Alfabeto vazio → `Alfabeto Σ: informe ao menos um valor.`
- Símbolo com mais de 1 caractere → erro. Símbolo repetido → erro. `λ` no alfabeto → erro.
- Palavra de L com símbolo fora de Σ → erro (a linguagem precisa ser sobre Σ).
- Cadeia com símbolo fora de Σ **não é erro**: a resposta diz `cadeiaValida: false` e lista os símbolos inválidos.
- Σ* com mais de **2000** palavras → erro pedindo para diminuir n (proteção do servidor).

## 5. Algoritmo
- **Validar w**: percorre cada caractere de w e verifica se está no `Set` do alfabeto.
- **Tamanho**: `w.length()`.
- **Concatenação**: `w + w2`.
- **w ∈ L**: `Set.contains(w)`.
- **Σ\* até n** (por níveis): começa com `[λ]`; para k = 1..n, cada palavra do nível k−1
  ganha cada símbolo no fim → nível k. Junta todos os níveis. Ordem: por tamanho, depois pela ordem de Σ.
- **Σ+** = Σ* sem λ.

## 6. Contrato da API
Requisição (`exemplos/linguagem-enunciado.json`):
```json
{ "alfabeto": ["a","b"], "linguagem": ["ab","ba","abba"], "cadeia": "abba", "cadeia2": "ab", "tamanhoMaximo": 3 }
```
Resposta:
```json
{
  "alfabeto": "{a, b}", "linguagem": "{ab, ba, abba}",
  "cadeia": "abba", "cadeiaValida": true, "simbolosInvalidos": [],
  "mensagem": "Cadeia válida para Σ", "tamanho": 4, "pertenceALinguagem": true,
  "cadeia2": "ab", "cadeia2Valida": true, "concatenacao": "abbaab", "tamanhoConcatenacao": 6,
  "tamanhoMaximo": 3,
  "sigmaEstrela": ["λ","a","b","aa","ab","ba","bb","aaa", "..."],
  "sigmaMais":    ["a","b","aa","ab","ba","bb","aaa", "..."]
}
```
Erro: `400 {"erro": "Símbolo 'a' repetido no alfabeto."}`

## 7. Complexidade
n = |w|, k = |Σ|, m = tamanho máximo de Σ*.

| Operação | Melhor (Ω) | Médio (Θ) | Pior (O) |
|----------|-----------|-----------|----------|
| Validar w ∈ Σ* | Ω(n) | Θ(n) | O(n) — sempre lê a cadeia toda |
| Tamanho \|w\| | Ω(1) | Θ(1) | O(1) |
| Concatenação | Ω(n+n₂) | Θ(n+n₂) | O(n+n₂) |
| w ∈ L (HashSet) | Ω(n) | Θ(n) | O(n·\|L\|) (colisões) |
| Gerar Σ* até m | Ω(m·kᵐ) | Θ(m·kᵐ) | O(m·kᵐ) — exponencial, por isso o limite de 2000 |

## 8. Critérios de aceite (testes em `LinguagemFormalTest`)
- [x] Σ={a,b}, w=`abba` → "Cadeia válida para Σ", tamanho 4 (exemplo do enunciado)
- [x] w=`abc` → inválida, símbolos inválidos `[c]`
- [x] w=λ → válida, tamanho 0
- [x] `ab`·`ba` = `abba`; `ab`·λ = `ab`
- [x] L={ab,ba}: `ab` ∈ L, `aa` ∉ L
- [x] Σ* até 2 = `λ,a,b,aa,ab,ba,bb`; Σ+ até 2 = o mesmo sem λ
- [x] Σ* até 20 → erro de limite
- [x] Palavra de L fora de Σ → erro; símbolo repetido → erro

## 9. Arquivos
- `src/main/java/com/br/unifor/simulador/linguagem/` — `LinguagemFormal`, `LinguagemRequest`, `LinguagemResultado`, `LinguagemController`
- `src/main/resources/static/js/linguagem.js`
- `src/test/java/com/br/unifor/simulador/linguagem/LinguagemFormalTest.java`

## 10. Fora do escopo / pendências
- L é **finita** (lista de palavras). Linguagens infinitas são tratadas pelos autômatos/gramáticas.
- Operações extras possíveis no futuro: união, interseção, fecho de Kleene de L, reverso, potência wⁿ.

## 11. Histórico
| Data | O que mudou |
|------|-------------|
| 2026-11-20 | Spec criada e implementada (back + aba + testes). |
