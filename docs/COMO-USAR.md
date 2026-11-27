# Como usar

## 1. Rodar

Use **JDK 21** e **Node.js com npm**. O Gradle prepara as visualizações React junto dos recursos da aplicação.

```bash
./gradlew bootRun          # Linux / macOS
.\gradlew.bat bootRun     # PowerShell
```

Abra **http://localhost:8080** (não abra o `index.html` direto do disco nem pelo Live Server:
a página precisa do backend na mesma porta).

Testes: `./gradlew test`

## 2. Aba "1. Linguagens Formais"

| Campo | O que digitar |
|-------|---------------|
| Alfabeto Σ | símbolos separados por vírgula: `a, b` |
| Linguagem L | palavras separadas por vírgula (opcional): `ab, ba, abba` |
| Cadeia w | `abba` (deixe vazio ou `λ` para a cadeia vazia) |
| Cadeia w₂ | segunda cadeia, para a concatenação |
| Gerar Σ* e Σ+ até n | tamanho máximo das palavras geradas (no máx. 2000 palavras) |

Clique **Calcular**. Saída para o exemplo do enunciado: `Cadeia válida para Σ`, `Tamanho |w| = 4`,
w ∈ L, concatenação, Σ* e Σ+.

## 3. Abas "2. AFD" e "3. AFN"

| Campo | O que digitar |
|-------|---------------|
| Estados Q | `q0, q1, q2` |
| Alfabeto Σ | `a, b` |
| Estado inicial | `q0` |
| Estados finais F | `q2` (vírgula se forem vários) |
| Transições δ | uma por linha: `origem símbolo destino` — ex.: `q0 a q1` |
| Cadeia de entrada | `ab` |

- **Executar** → faixa verde (ACEITA) ou vermelha (REJEITADA / erro), caminho
  (`q0 -> q1 -> q2`), estados desenhados (→ = inicial, borda dupla = final, amarelo = ativo)
  e tabela do passo a passo. Use ⏮ ◀ ▶ ⏭ ou clique numa linha da tabela para navegar.
- **Abrir JSON…** carrega um autômato de um arquivo `.json`; **Salvar JSON** baixa o que está na tela.
- No **AFN** pode repetir `origem símbolo` com destinos diferentes e usar `λ` como símbolo (transição vazia).
  A tabela mostra o conjunto de estados antes, o resultado do *move* e o conjunto após o fecho-λ.

Formato do JSON (igual para AFD e AFN, e igual ao corpo da API):
```json
{
  "estados": ["q0","q1","q2"],
  "alfabeto": ["a","b"],
  "estadoInicial": "q0",
  "estadosFinais": ["q2"],
  "transicoes": [
    { "origem": "q0", "simbolo": "a", "destino": "q1" },
    { "origem": "q1", "simbolo": "b", "destino": "q2" }
  ],
  "cadeia": "ab"
}
```
Exemplos prontos em `src/main/resources/static/exemplos/`.

## 4. Gramática Livre de Contexto

Use **Carregar exemplo** e execute `aaabbb` com as produções `S -> aSb` e `S -> λ`. A aba mostra a derivação e a árvore. Coloque uma alternativa por linha e use um caractere por variável ou terminal. Uma busca interrompida pelos limites é exibida como inconclusiva.

## 5. Autômato com Pilha

Carregue o exemplo `aⁿbⁿ` e execute `aabb`. Navegue pelo rastro para observar a leitura, o estado e a pilha com o topo à esquerda. Nas transições, uma leitura `λ` preserva a posição da entrada, e uma substituição `λ` remove o topo sem colocar um novo símbolo. Use nomes de estado sem espaços nos campos da aba.

## 6. Máquina de Turing

Use **Restaurar exemplo de soma** e execute `000100` para obter `00000`. Cada `0` é uma unidade, `1` separa as parcelas e `B` indica uma célula em branco nas transições. A tabela é editável. A navegação mostra a posição do cabeçote na fita em cada passo.

## 7. Usando a API direto (curl)

```bash
E=src/main/resources/static/exemplos
curl -s -H 'Content-Type: application/json' -d @$E/linguagem-enunciado.json localhost:8080/api/linguagem
curl -s -H 'Content-Type: application/json' -d @$E/afd-enunciado.json       localhost:8080/api/afd
curl -s -H 'Content-Type: application/json' -d @$E/afn-termina-em-ab.json   localhost:8080/api/afn
curl -s -H 'Content-Type: application/json' -d @$E/glc-enunciado.json      localhost:8080/api/glc
curl -s -H 'Content-Type: application/json' -d @$E/ap-enunciado.json       localhost:8080/api/ap
curl -s -H 'Content-Type: application/json' -d @$E/turing-soma-unaria.json localhost:8080/api/turing
```

Erros de validação voltam como `HTTP 400 {"erro": "mensagem"}`.
