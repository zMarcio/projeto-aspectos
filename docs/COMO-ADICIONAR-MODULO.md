# Como adicionar um módulo

Todo módulo segue a mesma receita. Siga na ordem — começando pela spec (SDD).

## Passo 0 — Spec primeiro
1. Abra a spec do módulo ou copie `docs/specs/TEMPLATE.md` para uma nova.
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
- Para Q, Σ, q0 e F, use `DefinicaoAutomato` no pacote `automato`, como no AP.
- Simulações que podem não parar precisam de um limite. Exiba o limite atingido como uma execução inconclusiva.

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
   Use `ligarSimulacao` para formulários e `mostrarQuadros` para navegar pelos passos, como nas abas AP e MT.
3. Inclua o script no fim do `index.html`: `<script src="js/ap.js"></script>`.
4. CSS extra (pilha, fita) vai em `css/estilo.css`.
5. Exemplos em `src/main/resources/static/exemplos/<modulo>-*.json`.

## Passo 4 — Fechar
- Marque os requisitos `[x]` na spec, preencha a complexidade (seção 7 e `007-complexidade.md`),
  status `✅ Implementado`, linha no Histórico.
- Atualize o README se houver novo exemplo de entrada/saída.

---

## Referência de integração

A [spec da Máquina de Turing](specs/006-maquina-turing.md) descreve como o motor do protótipo foi adaptado ao projeto. Os testes HTTP usam o mesmo JSON que a aba carrega, o que ajuda a manter os exemplos, a API e o README coerentes.
