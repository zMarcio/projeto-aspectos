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
| 3 | Gramática Livre de Contexto      | [004](004-glc.md) | ✅ Implementado | `POST /api/glc` | 4. GLC |
| 4 | Autômato com Pilha               | [005](005-automato-pilha.md) | ✅ Implementado | `POST /api/ap` | 5. Autômato com Pilha |
| 5 | Máquina de Turing                | [006](006-maquina-turing.md) | ✅ Implementado (soma unária) | `POST /api/turing` | 6. Máquina de Turing |
| 6 | Complexidade Computacional       | [007](007-complexidade.md) | ✅ Análise registrada | — | — |

## 3. Arquitetura

```
Navegador (HTML, CSS, JS e React)     Spring Boot (Java 21)
┌──────────────────────────┐  JSON   ┌───────────────────────────────┐
│ index.html  (abas)       │ ──────▶ │ XxxController  (@PostMapping) │
│ js/<modulo>.js           │ ◀────── │   └─ classe de domínio (lógica)│
└──────────────────────────┘         │ TratadorDeErros → HTTP 400     │
                                     └───────────────────────────────┘
```

- **Pacotes Java por domínio** em `src/main/java/com/br/unifor/simulador/`. O AP fica em `automato/` junto da definição compartilhada com AFD e AFN.
- Cada módulo tem: `XxxRequest` (record do JSON de entrada), a classe de domínio com a lógica
  (sem nada de Spring), `ResultadoXxx`/`PassoXxx` (records da saída) e `XxxController`.
- **Erros de validação**: lance `IllegalArgumentException("mensagem amigável")`.
  O `TratadorDeErros` devolve `400 {"erro": "mensagem"}` e a página mostra a faixa vermelha.
- **Front**: uma aba por módulo em `index.html`, com formulários JavaScript e funções comuns em `js/comum.js`.
  GLC, AP e MT usam `js/simulacao.js` para os formulários. AFD, AFN, AP e MT usam componentes React em `frontend/src/` para a visualização e a navegação pelos passos.
- Exemplos de entrada em `src/main/resources/static/exemplos/*.json` (servidos pela página e usados no README).

## 4. Convenções

- Código e mensagens em **português**, nomes simples (`executar`, `adicionarTransicao`, `fechoLambda`).
- No backend, use laços simples e as coleções `Map` e `Set` do Java. O projeto usa Spring Boot e records Java.
- Cadeia vazia: escrita e exibida como **`λ`** (também aceita vazio). Símbolos de alfabeto têm 1 caractere.
- Toda classe de domínio tem teste em `src/test/java/...` (rodar com `./gradlew test`).

## 5. React no front-end (bônus)

**Status:** ✅ Implementado

A etapa usa React nas visualizações pedidas no bônus do enunciado e reaproveita as abas, os estilos, os exemplos e as respostas JSON atuais. Os simuladores continuam executando no backend Java.

- [x] Renderizar a pilha e navegar pelos passos no AP com React.
- [x] Aplicar o mesmo padrão aos estados ativos do AFD e do AFN e à fita com o cabeçote da MT, com controles compartilhados e os estilos do projeto.
- [x] Compilar o front no Gradle e servir o pacote pelo Spring Boot. Conferir a navegação das quatro abas no navegador e registrar a tecnologia no README.

Em 27/11/2026, as quatro visualizações foram verificadas no Chrome com o JAR executável.

## 6. Fechamento da entrega

- [x] Preparar o [roteiro de demonstração](../ROTEIRO-APRESENTACAO.md) para os seis blocos do enunciado.
- [x] Validar os exemplos das seis abas pela API e no Chrome com o JAR executável. Os 46 testes JUnit passaram.
- [ ] Ensaiar a demonstração com a dupla.
- [x] Elaborar o [relatório técnico](../RELATORIO-TECNICO.pdf), com UML, casos de uso, implementação e análise assintótica.
- [x] Preparar a [apresentação de 10 minutos](../APRESENTACAO.pptx).
- [ ] Conferir o commit definitivo no GitHub após a publicação da versão local. A branch `feat/glc-ap-mt` existe no remoto e ainda não contém `frontend/package.json`.
