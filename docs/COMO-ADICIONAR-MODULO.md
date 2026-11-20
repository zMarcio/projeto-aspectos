# Como encaixar os módulos que faltam (GLC, AP, Máquina de Turing)

Todo módulo segue a mesma receita. Siga na ordem — começando pela spec (SDD).

## Passo 0 — Spec primeiro
1. Abra a spec do módulo (`docs/specs/004-glc.md`, `005-automato-pilha.md`, `006-maquina-turing.md`)
   ou copie `docs/specs/TEMPLATE.md`.
2. Complete entradas, regras, algoritmo, contrato da API e critérios de aceite. Mude o status para `🚧 Em desenvolvimento`.
3. Atualize o painel em `docs/specs/000-visao-geral.md`.

## Passo 1 — Backend (Java)
Crie o pacote `src/main/java/com/br/unifor/simulador/<modulo>/` com:

| Arquivo | Papel | Modelo para copiar |
|---------|-------|--------------------|
| `XxxRequest.java` | `record` com os campos do JSON de entrada | `automato/AutomatoRequest.java` |
| `Xxx.java` | a lógica pura (sem Spring) | `automato/Afd.java` |
| `PassoXxx.java` / `ResultadoXxx.java` | `record`s da saída | `automato/PassoAfd.java`, `ResultadoAfd.java` |
| `XxxController.java` | `@RestController` com `@PostMapping("/api/<modulo>")` | `automato/AutomatoController.java` |

Regras:
- Validação: use `Validacao.obrigatorio(...)`, `Validacao.simbolo(...)`, `Validacao.cadeia(...)` e lance
  `IllegalArgumentException("mensagem")`. **Não** crie `@ExceptionHandler`: o `TratadorDeErros` global já faz isso.
- Precisa de Q, Σ, q0 e F? Reaproveite `automato.DefinicaoAutomato` (coloque a classe no pacote `automato`
  ou torne os campos/métodos `public`).
- Simulações que podem não parar (GLC, MT) precisam de **limite de passos**.

Esqueleto mínimo do controller:
```java
@RestController
public class PilhaController {
    @PostMapping("/api/ap")
    public ResultadoAp simular(@RequestBody ApRequest request){
        AutomatoPilha ap = new AutomatoPilha(...);   // valida e monta
        return ap.executar(Validacao.cadeia(request.cadeia()));
    }
}
```

## Passo 2 — Testes
`src/test/java/com/br/unifor/simulador/<modulo>/XxxTest.java` — um `@Test` por critério de aceite da spec.
Rode `./gradlew test`.

## Passo 3 — Front (nova aba)
1. **`index.html`** — adicione o botão na `<nav class="abas">`:
   ```html
   <button data-aba="aba-ap">5. Autômato com Pilha</button>
   ```
   e a aba (copie a estrutura da aba do AFD, trocando o prefixo dos ids, ex.: `ap-`):
   ```html
   <div class="aba" id="aba-ap">
     <section> ... inputs ... <button class="primary" id="ap-simular">Executar</button></section>
     <section id="ap-saida" class="hidden"><div id="ap-resultado" class="resultado"></div> ... </section>
   </div>
   ```
2. **`js/ap.js`** — leia os inputs, chame `postJson('/api/ap', corpo)`, mostre com `mostrarFaixa(...)`.
   Se for parecido com AFD/AFN, as funções de `js/automato.js` (`lerAutomato`, `preencherAutomato`,
   `ligarArquivoJson`, `mostrarExecucao`, `preencherTabela`) já funcionam com o novo prefixo.
3. Inclua o script no fim do `index.html`: `<script src="js/ap.js"></script>`.
4. CSS extra (pilha, fita) vai em `css/estilo.css`.
5. Exemplos em `src/main/resources/static/exemplos/<modulo>-*.json`.

## Passo 4 — Fechar
- Marque os requisitos `[x]` na spec, preencha a complexidade (seção 7 e `007-complexidade.md`),
  status `✅ Implementado`, linha no Histórico.
- Atualize o README se houver novo exemplo de entrada/saída.

---

## Exemplo real: trazer a Máquina de Turing do protótipo

O protótipo está em `../simulador-maquina-turing-web` (mesmo Spring Boot, mesma ideia).

1. Crie o pacote `com.br.unifor.simulador.turing` e copie `Fita`, `MaquinaTuring`, `Passo`,
   `ResultadoSimulacao`, `SimulacaoRequest`, `Transicao` e `SimuladorController`. Troque a linha
   `package` para `com.br.unifor.simulador.turing`.
2. **Lombok**: este projeto não usa. Troque `Transicao` por um record e ajuste as chamadas
   (`t.getNovoEstado()` → `t.novoEstado()`, etc.):
   ```java
   public record Transicao(String estadoAtual, char simboloLido, String novoEstado, char simboloEscrito, char movimento) {}
   ```
3. No `SimuladorController`: renomeie para `TuringController`, mude `@RequestMapping("/api")` +
   `@PostMapping("/simular")` para `@PostMapping("/api/turing")`, **apague** o método `@ExceptionHandler erro(...)`
   e troque `obrigatorio(...)`/`simbolo(...)` por `Validacao.obrigatorio(...)`/`Validacao.simbolo(...)`.
4. Front: do `index.html` do protótipo, copie
   - o conteúdo de `<main>` (a partir de "Configuração da máquina") para `<div class="aba" id="aba-turing">`;
   - o CSS de `.fita`, `.celula`, `.celula.cabecote` para `css/estilo.css`;
   - o `<script>` para `js/turing.js`, trocando `'/api/simular'` por `'/api/turing'`.
   **Atenção aos ids repetidos** (`entrada`, `resultado`, `info`...): prefixe com `mt-` no HTML e no JS.
5. Botão da aba: `<button data-aba="aba-turing">6. Máquina de Turing</button>`.
6. Teste: `MaquinaTuringTest` com `000100` → fita `00000`, aceita.
