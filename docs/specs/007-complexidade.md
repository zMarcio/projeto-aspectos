# Spec 007 — Complexidade computacional

| Campo | Valor |
|---|---|
| Status | ✅ Análise dos seis módulos registrada |
| Módulo | 6 do enunciado |

As estimativas consideram o modelo já carregado e distinguem o processamento da cadeia da construção do rastro. `O` indica um limite superior, `Ω` um limite inferior e `Θ` um limite justo. Essas notações valem para qualquer caso. As estimativas para o caso médio abaixo usam as hipóteses descritas após a tabela.

| Operação | Melhor caso | Caso médio sob as hipóteses abaixo | Pior caso |
|---|---|---|---|
| Linguagens: validar `w ∈ Σ*` | Θ(n+1) | Θ(n+1) | O(1+n(1+u)) |
| Linguagens: obter tamanho | Θ(1) | Θ(1) | Θ(1) |
| Linguagens: concatenar as cadeias | Ω(1) | Θ(n+m) | O(n+m+1) |
| Linguagens: consultar `w ∈ L` | Ω(1) | Θ(n+1) | O((n+1)(\|L\|+1)) |
| Linguagens: gerar Σ* e Σ+ até um tamanho | Θ(1) para tamanho zero | Θ(1+C) para saídas permitidas | Θ(1+C) para saídas permitidas |
| AFD: executar e registrar a cadeia | Θ(1) | Θ(n²) | O(1+n²) |
| AFN: calcular um fecho-λ | Θ(1) | Θ(1) com região alcançável de tamanho constante | O((1+Q+Eλ) log(2+Q)) |
| AFN: executar e registrar a cadeia | Ω(1) | Θ(n²) | O((n+1)(Q+E+1) log(2+Q) + n²) |
| GLC: buscar e reconstruir derivação | Θ(1) | Θ(n²) para a família de aceitação descrita abaixo | O(1+n+A·f) |
| AP: executar e registrar a pilha | Θ(1) | Θ(n²) para a família de aceitação descrita abaixo | O(1+p+Σ(gᵢ+hᵢ)) |
| MT: executar e registrar a fita | Θ(n+1) | Θ(n²) para a família de soma descrita abaixo | O((p+1)(n+p+1)) |

- **Linguagens.** `n` e `m` são os comprimentos das cadeias. A validação sempre percorre a entrada e consulta uma lista linear de até `u` símbolos inválidos distintos. O caso médio ilustrado usa cadeias sobre Σ, concatenação com duas cadeias não vazias e consulta com o hash da cadeia ainda não calculado, sob dispersão usual. Na enumeração aceita pelo limite de 2.000 palavras, `C` é a quantidade total de caracteres gerados. A geração passa duas vezes pelos níveis. Com alfabeto unário, as palavras de tamanho 0 a `m` já somam `Θ(m²)` caracteres.
- **AFD e AFN.** `Q` é a quantidade de estados, `E` a quantidade de transições e `Eλ` a quantidade de transições vazias. As estimativas assumem nomes de estado de tamanho limitado e consulta com custo esperado constante nos mapas. O fecho usa uma pilha e conjuntos ordenados. Nos dois modelos, uma execução até o fim guarda `Θ(n²)` caracteres apenas nos sufixos copiados. O caso médio ilustrado condiciona a execução a atravessar toda a cadeia e, para o AFN, a manter os conjuntos e os destinos visitados por símbolo com tamanho constante.
- **GLC.** `A` é a quantidade de alternativas examinadas e `f` é o comprimento máximo das formas. O caso médio ilustrado usa as palavras aceitas de `S → aSb | λ`, para as quais há `Θ(n)` expansões e cópias de formas de até `Θ(n)` símbolos. O espaço do rastro e da busca é `O(1+A·f)`.
- **AP.** `p` é a quantidade de transições executadas, `gᵢ` o tamanho da substituição do topo e `hᵢ` a altura da pilha após o passo `i`. O caso médio ilustrado usa entradas aceitas de `aⁿbⁿ`. O tempo de decisão para esse exemplo é linear sem copiar a pilha. O histórico soma `Θ(n²)` símbolos na aceitação e ocupa `O(1+p+Σhᵢ)` no caso geral.
- **MT.** `p` é a quantidade de transições executadas. O conteúdo da fita tem `O(n+p+1)` células e é copiado para cada quadro. No caso médio ilustrado, as entradas são válidas para a soma e o separador pode estar em qualquer posição. Toda distribuição sobre essas posições dá `n+2` transições, com rastro de `Θ(n²)`. O espaço máximo do rastro geral é `O((p+1)(n+p+1))`.
