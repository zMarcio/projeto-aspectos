const camposTuring = [
  ['estadoAtual', 'Estado atual'], ['simboloLido', 'Símbolo lido'], ['novoEstado', 'Novo estado'],
  ['simboloEscrito', 'Símbolo escrito'], ['direcao', 'Direção'],
];

function adicionarTransicaoTuring(valores = {}) {
  const linha = $('mt-transicoes').insertRow();
  for (const [campo, rotulo] of camposTuring) {
    const controle = document.createElement(campo === 'direcao' ? 'select' : 'input');
    if (campo === 'direcao') {
      for (const direcao of ['E', 'D']) controle.add(new Option(direcao, direcao));
      controle.value = valores[campo] || 'D';
    } else {
      controle.value = valores[campo] || '';
      if (campo.startsWith('simbolo')) controle.maxLength = 1;
    }
    controle.name = campo;
    controle.required = true;
    controle.setAttribute('aria-label', rotulo);
    linha.insertCell().append(controle);
  }
  const excluir = document.createElement('button');
  excluir.type = 'button';
  excluir.textContent = 'Excluir';
  excluir.onclick = () => {
    const alvo = linha.nextElementSibling || linha.previousElementSibling;
    linha.remove();
    (alvo?.querySelector('button') || $('mt-adicionar')).focus();
  };
  linha.insertCell().append(excluir);
  return linha;
}

function lerTuring() {
  return {
    entrada: $('mt-entrada').value,
    estadoInicial: $('mt-inicial').value,
    estadoAceitacao: $('mt-aceitacao').value,
    transicoes: Array.from($('mt-transicoes').rows, linha => Object.fromEntries(
      camposTuring.map(([campo]) => [campo, linha.querySelector(`[name="${campo}"]`).value]))),
  };
}

function preencherTuring(exemplo) {
  $('mt-entrada').value = exemplo.entrada;
  $('mt-inicial').value = exemplo.estadoInicial;
  $('mt-aceitacao').value = exemplo.estadoAceitacao;
  $('mt-transicoes').replaceChildren();
  exemplo.transicoes.forEach(adicionarTransicaoTuring);
}

function mostrarTuring(resultado) {
  $('mt-soma').classList.toggle('hidden', resultado.status !== 'ACEITA');
  $('mt-saida-fita').textContent = resultado.saida || 'λ';
  const fita = $('mt-fita');
  const linhas = resultado.passos.map(p => [p.numero, p.estado, p.simboloLido, p.cabecote]);
  mostrarQuadros('mt', linhas, indice => {
    const passo = resultado.passos[indice];
    $('mt-configuracao').textContent = `Passo ${passo.numero} · Estado ${passo.estado} · Lê: ${passo.simboloLido}`;
    const celulas = Array.from(passo.fita, (simbolo, posicao) => {
      const celula = document.createElement('span');
      celula.className = posicao === passo.cabecote ? 'celula ativa' : 'celula';
      celula.textContent = simbolo;
      return celula;
    });
    fita.replaceChildren(...celulas);
    const cabecote = celulas[passo.cabecote];
    fita.scrollLeft = cabecote.offsetLeft - Math.floor(fita.clientWidth / 2);
  });
}

$('mt-adicionar').onclick = () => adicionarTransicaoTuring().querySelector('input').focus();
ligarSimulacao('mt', '/api/turing', 'turing-soma-unaria.json', lerTuring, preencherTuring, mostrarTuring);
