# Como usar

## 1. Rodar

Precisa de **JDK 21** (se não tiver, o Gradle baixa sozinho na primeira execução).

```bash
./gradlew bootRun          # Linux / macOS
gradlew.bat bootRun        # Windows
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

## 4. Usando a API direto (curl)

```bash
E=src/main/resources/static/exemplos
curl -s -H 'Content-Type: application/json' -d @$E/linguagem-enunciado.json localhost:8080/api/linguagem
curl -s -H 'Content-Type: application/json' -d @$E/afd-enunciado.json       localhost:8080/api/afd
curl -s -H 'Content-Type: application/json' -d @$E/afn-termina-em-ab.json   localhost:8080/api/afn
```

Erros de validação voltam como `HTTP 400 {"erro": "mensagem"}`.
