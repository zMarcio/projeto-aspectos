# Simulador de Linguagens Formais e Modelos Computacionais

Projeto prático (AV3) de **Aspectos Teóricos da Computação**.
Backend em **Java 21 + Spring Boot**, frontend em **HTML + CSS + JavaScript puro**, um módulo por aba.

| # | Módulo | Status | Spec |
|---|--------|--------|------|
| 1 | Linguagens Formais | ✅ | [001](docs/specs/001-linguagens-formais.md) |
| 2 | AFD | ✅ | [002](docs/specs/002-afd.md) |
| 2 | AFN | ✅ | [003](docs/specs/003-afn.md) |
| 3 | Gramática Livre de Contexto | ✅ | [004](docs/specs/004-glc.md) |
| 4 | Autômato com Pilha | ✅ | [005](docs/specs/005-automato-pilha.md) |
| 5 | Máquina de Turing (soma unária) | ✅ | [006](docs/specs/006-maquina-turing.md) |
| 6 | Complexidade | ✅ | [007](docs/specs/007-complexidade.md) |

Trabalhamos com **SDD (Spec Driven Development)**: nenhuma funcionalidade sem spec antes.
Regras e painel de controle em [`docs/specs/000-visao-geral.md`](docs/specs/000-visao-geral.md).

## Como executar

```bash
./gradlew bootRun      # Windows: gradlew.bat bootRun
```
Abra http://localhost:8080. Testes: `./gradlew test`. Precisa de JDK 21 (o Gradle baixa se faltar).

Guia completo: [`docs/COMO-USAR.md`](docs/COMO-USAR.md) ·
Guia para novos módulos: [`docs/COMO-ADICIONAR-MODULO.md`](docs/COMO-ADICIONAR-MODULO.md)

## Exemplos de entrada e saída

**Linguagens Formais** — Σ = {a, b}, w = `abba`
```
Cadeia válida para Σ
Tamanho = 4
```

**AFD** — `q0 –a→ q1`, `q1 –b→ q2`, F = {q2}, entrada `ab`
```
q0 -> q1 -> q2
ACEITA
```

**AFN** — palavras que terminam em `ab` (`q0 a q0`, `q0 b q0`, `q0 a q1`, `q1 b q2`), entrada `babab`
```
[q0] -b-> [q0] -a-> [q0, q1] -b-> [q0, q2] -a-> [q0, q1] -b-> [q0, q2]
ACEITA
```

**GLC** — produções `S → aSb` e `S → λ`, entrada `aaabbb`.
Saída `S ⇒ aSb ⇒ aaSbb ⇒ aaaSbbb ⇒ aaabbb`, com árvore de derivação e aceitação.

**Autômato com Pilha** — linguagem `aⁿbⁿ`, entrada `aabb`.
Saída aceita, com pilha `[A, A, Z]` após ler `aa` e `[Z]` após o passo final `λ`.

**Máquina de Turing** — entrada `000100` (3 + 2).
Saída `00000`, com a fita e o cabeçote disponíveis em todos os passos.

Os JSONs desses exemplos estão em [`src/main/resources/static/exemplos/`](src/main/resources/static/exemplos/)
e também carregam pelos botões de exemplo de cada aba.

## Estrutura

```
src/main/java/com/br/unifor/simulador/
├── SimuladorApplication.java     # sobe o servidor
├── TratadorDeErros.java          # IllegalArgumentException -> HTTP 400 {"erro": ...}
├── Validacao.java                # validações comuns (obrigatório, símbolo, λ)
├── linguagem/                    # Módulo 1 — POST /api/linguagem
├── automato/                     # AFD, AFN e AP — POST /api/afd, /api/afn e /api/ap
├── glc/                          # POST /api/glc
└── turing/                       # POST /api/turing
src/main/resources/static/
├── index.html                    # abas
├── css/estilo.css
├── js/                           # formulários, abas e renderização dos resultados
└── exemplos/*.json
docs/
├── specs/                        # SDD: uma spec por módulo
├── COMO-USAR.md
└── COMO-ADICIONAR-MODULO.md
```
