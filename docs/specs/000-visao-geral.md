# Spec 000 — Visão geral e regras do projeto

| Campo      | Valor |
|------------|-------|
| Status     | ✅ Vigente |
| Criada em  | 2026-11-20 |
| Origem     | Enunciado "PROJETO PRÁTICO EM DUPLA" (Aspectos Teóricos da Computação) |

## 1. Como trabalhamos (SDD — Spec Driven Development)

1. **Antes de codar**, cria-se a spec do módulo em `docs/specs/NNN-nome.md` a partir do [`TEMPLATE.md`](TEMPLATE.md), com status `📝 Rascunho`.
2. A dupla revisa a spec (requisitos, contrato da API, regras). Aprovada → status `🚧 Em desenvolvimento`.
3. Implementa-se **exatamente** o que a spec diz. Se algo mudar durante o código, **muda-se a spec primeiro**.
4. Os critérios de aceite da spec viram testes JUnit. Todos passando → status `✅ Implementado`.
5. Toda mudança relevante entra no **Histórico** da spec.

Status possíveis: `📝 Rascunho` · `🚧 Em desenvolvimento` · `✅ Implementado` · `⏳ Pendente (não iniciado)`.

## 2. Painel de controle dos módulos

| # | Módulo (enunciado)               | Spec | Status | Endpoint | Aba |
|---|----------------------------------|------|--------|----------|-----|
| 1 | Linguagens Formais               | [001](001-linguagens-formais.md) | ✅ Implementado | `POST /api/linguagem` | 1. Linguagens Formais |
| 2 | Autômato Finito Determinístico   | [002](002-afd.md) | ✅ Implementado | `POST /api/afd` | 2. AFD |
| 2 | Autômato Finito Não Determinístico | [003](003-afn.md) | ✅ Implementado | `POST /api/afn` | 3. AFN |
| 3 | Gramática Livre de Contexto      | [004](004-glc.md) | ⏳ Pendente | `POST /api/glc` (proposto) | — |
| 4 | Autômato com Pilha               | [005](005-automato-pilha.md) | ⏳ Pendente | `POST /api/ap` (proposto) | — |
| 5 | Máquina de Turing                | [006](006-maquina-turing.md) | ⏳ Pendente (existe protótipo separado) | `POST /api/turing` (proposto) | — |
| 6 | Complexidade Computacional       | [007](007-complexidade.md) | 🚧 Parcial (módulos 1–3 feitos) | — | — |

## 3. Arquitetura (igual ao simulador de Máquina de Turing de referência)

```
Navegador (HTML + CSS + JS puro)      Spring Boot (Java 21)
┌──────────────────────────┐  JSON   ┌───────────────────────────────┐
│ index.html  (abas)       │ ──────▶ │ XxxController  (@PostMapping) │
│ js/<modulo>.js           │ ◀────── │   └─ classe de domínio (lógica)│
└──────────────────────────┘         │ TratadorDeErros → HTTP 400     │
                                     └───────────────────────────────┘
```

- **Um pacote Java por módulo** em `src/main/java/com/br/unifor/simulador/<modulo>/`.
- Cada módulo tem: `XxxRequest` (record do JSON de entrada), a classe de domínio com a lógica
  (sem nada de Spring), `ResultadoXxx`/`PassoXxx` (records da saída) e `XxxController`.
- **Erros de validação**: lance `IllegalArgumentException("mensagem amigável")`.
  O `TratadorDeErros` devolve `400 {"erro": "mensagem"}` e a página mostra a faixa vermelha.
- **Front**: uma `<div class="aba">` por módulo em `index.html` + um arquivo `js/<modulo>.js`.
  Funções comuns em `js/comum.js` (abas, `postJson`, `lista`, `esc`).
- Exemplos de entrada em `src/main/resources/static/exemplos/*.json` (servidos pela página e usados no README).

## 4. Convenções

- Código e mensagens em **português**, nomes simples (`executar`, `adicionarTransicao`, `fechoLambda`).
- Código "burro de propósito": laços simples, `Map`/`Set` do Java, sem frameworks extras, sem Lombok.
- Cadeia vazia: escrita e exibida como **`λ`** (também aceita vazio). Símbolos de alfabeto têm 1 caractere.
- Toda classe de domínio tem teste em `src/test/java/...` (rodar com `./gradlew test`).
