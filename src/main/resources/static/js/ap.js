import { mostrarPilha } from './visualizacoes.js';

ligarSimulacao('ap', '/api/ap', 'ap-enunciado.json', lerAp, preencherAp, mostrarPilha);

function lerAp() {
  const transicoes = $('ap-transicoes').value.split('\n').flatMap((linha, indice) => {
    if (!linha.trim()) return [];
    const colunas = linha.trim().split(/\s+/);
    if (colunas.length !== 5) {
      throw new Error(`Transição na linha ${indice + 1}: informe origem, leitura, topo, destino e substituição.`);
    }
    const [origem, simbolo, topo, destino, empilha] = colunas;
    return [{ origem, simbolo, topo, destino, empilha }];
  });
  return {
    estados: lista($('ap-estados').value),
    alfabeto: lista($('ap-alfabeto').value),
    alfabetoPilha: lista($('ap-alfabeto-pilha').value),
    estadoInicial: $('ap-inicial').value,
    estadosFinais: lista($('ap-finais').value),
    simboloInicialPilha: $('ap-inicial-pilha').value,
    transicoes,
    cadeia: $('ap-cadeia').value,
  };
}

function preencherAp(exemplo) {
  $('ap-estados').value = exemplo.estados.join(', ');
  $('ap-alfabeto').value = exemplo.alfabeto.join(', ');
  $('ap-alfabeto-pilha').value = exemplo.alfabetoPilha.join(', ');
  $('ap-inicial').value = exemplo.estadoInicial;
  $('ap-finais').value = exemplo.estadosFinais.join(', ');
  $('ap-inicial-pilha').value = exemplo.simboloInicialPilha;
  $('ap-transicoes').value = exemplo.transicoes.map(t =>
    [t.origem, t.simbolo, t.topo, t.destino, t.empilha].join(' ')).join('\n');
  $('ap-cadeia').value = exemplo.cadeia;
}
