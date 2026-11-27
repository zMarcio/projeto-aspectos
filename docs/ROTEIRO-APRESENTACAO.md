# Apresentação e demonstração da AV3

O AFD e o AFN são apresentados juntos no bloco de autômatos finitos. Assim, a demonstração cobre as seis abas e a análise de complexidade em 10 minutos.

## Preparação

1. Com JDK 21 e Node.js com npm, rode `./gradlew test bootJar` no Linux/macOS ou `.\gradlew.bat test bootJar` no PowerShell.
2. Execute o JAR de `build/libs/` conforme o [README](../README.md) e abra `http://localhost:8080`. Deixe o relatório e a apresentação abertos e o JSON `afd-enunciado.json` disponível para a importação.
3. Antes de apresentar, recarregue a página para ver os exemplos padrão. Se aparecer uma interface anterior às mudanças, use `Ctrl+F5`. O PDF inclui os resultados esperados e pode apoiar a explicação se a demonstração for interrompida.

## Roteiro de 10 minutos

| Tempo | Quem | Ação e resultado esperado |
|---|---|---|
| 0:00–0:40 | Pessoa 1 | Apresentar o objetivo. Mostrar como a interface envia a configuração em JSON ao Spring Boot e recebe o resultado e o rastro. |
| 0:40–1:40 | Pessoa 1 | **Linguagens Formais.** Carregar o exemplo e clicar em **Calcular**. Com Σ = `{a,b}` e `w = abba`, mostrar tamanho 4, `w ∈ L`, concatenação `abbaab`, 15 palavras em Σ* e 14 em Σ+ até comprimento 3. Trocar para `abc` e mostrar o símbolo inválido `c`. |
| 1:40–3:35 | Pessoa 1 | **AFD e AFN.** No AFD, usar **Abrir JSON…** com [afd-enunciado.json](../src/main/resources/static/exemplos/afd-enunciado.json) e executar `ab`. Usar **Início** e **Próximo** para ver `q0`, `q1`, `q2`. No AFN, executar `babab` no exemplo **termina em ab**. Clicar no passo 2 para ver `{q0,q1}` e no final para ver `{q0,q2}`. Se houver tempo, usar o exemplo com λ e deixar a entrada vazia para mostrar o fecho inicial `{q0,q1,q2}`. |
| 3:35–4:50 | Pessoa 1 | **GLC.** Carregar o exemplo e executar `aaabbb` com `S → aSb` e `S → λ`. Mostrar `S ⇒ aSb ⇒ aaSbb ⇒ aaaSbbb ⇒ aaabbb` e a árvore. Trocar para `aab` e mostrar a rejeição. |
| 4:50–6:10 | Pessoa 2 | **Autômato com Pilha.** Carregar `aⁿbⁿ`, executar `aabb` e clicar no passo 2 para mostrar `[A,A,Z]`. No passo final, a leitura λ preserva a posição da entrada e a pilha está em `[Z]`. O resultado é aceitação. |
| 6:10–7:40 | Pessoa 2 | **Máquina de Turing.** Restaurar a soma unária, executar `000100` (3 + 2) e mostrar `00000` em oito transições. Comparar o quadro inicial, a leitura do branco `B` à direita e o passo final. Mostrar a posição do cabeçote. |
| 7:40–9:30 | Pessoa 2 | **Complexidade.** No AFD, o melhor caso é Θ(1) com falha no primeiro símbolo. Uma execução completa faz `n` consultas ao mapa de transições e os sufixos registrados ocupam Θ(n²). Na MT de soma, são `n + 2` transições e Θ(n²) para produzir e guardar todos os quadros. Explicar que as hipóteses do caso médio e os limites gerais estão na [Spec 007](specs/007-complexidade.md). |
| 9:30–10:00 | Pessoa 2 | Encerrar mostrando o repositório, o README e a cobertura de testes. Explicar que `LIMITE_ATINGIDO` indica um resultado inconclusivo. |

## Conferência da publicação

- Verificar pelo endereço do commit que será entregue se estão presentes o README, o Gradle Wrapper, `src/`, `frontend/src/`, `frontend/package.json`, `frontend/package-lock.json` e os materiais em `docs/`.
- Após a publicação, testar a versão publicada em uma cópia limpa e usar o link direto do commit ou da tag na entrega. Os diretórios gerados `build/` e `frontend/node_modules/` ficam fora dela.
