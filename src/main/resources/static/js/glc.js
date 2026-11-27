ligarSimulacao('glc', '/api/glc', 'glc-enunciado.json', lerGlc, preencherGlc, mostrarGlc);

function lerGlc() {
  const producoes = $('glc-producoes').value.split('\n').flatMap((linha, indice) => {
    if (!linha.trim()) return [];
    const partes = linha.trim().match(/^(\S)\s*(?:->|→)\s*(.*)$/);
    if (!partes) throw new Error(`Produção na linha ${indice + 1}: use o formato S -> aSb.`);
    return [{ esquerda: partes[1], direita: partes[2] }];
  });
  return {
    variaveis: lista($('glc-variaveis').value),
    terminais: lista($('glc-terminais').value),
    simboloInicial: $('glc-inicial').value,
    producoes,
    cadeia: $('glc-cadeia').value,
  };
}

function preencherGlc(exemplo) {
  $('glc-variaveis').value = exemplo.variaveis.join(', ');
  $('glc-terminais').value = exemplo.terminais.join(', ');
  $('glc-inicial').value = exemplo.simboloInicial;
  $('glc-producoes').value = exemplo.producoes.map(p => `${p.esquerda} -> ${p.direita || 'λ'}`).join('\n');
  $('glc-cadeia').value = exemplo.cadeia;
}

function mostrarGlc(resultado) {
  $('glc-visual').classList.toggle('hidden', resultado.status !== 'ACEITA');
  $('glc-derivacao').textContent = resultado.derivacao.join(' ⇒ ');
  const listaRaiz = document.createElement('ul');
  const listasFilhos = [];
  resultado.arvore.forEach(({ simbolo, pai }, indice) => {
    const item = document.createElement('li');
    const rotulo = document.createElement('code');
    rotulo.textContent = simbolo;
    item.append(rotulo);
    const destino = pai === -1 ? listaRaiz : listasFilhos[pai];
    destino.append(item);
    const filhos = document.createElement('ul');
    item.append(filhos);
    listasFilhos[indice] = filhos;
  });
  $('glc-arvore').replaceChildren(listaRaiz);
}
