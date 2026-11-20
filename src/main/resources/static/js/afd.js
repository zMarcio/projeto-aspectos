// Aba 2 - AFD  ->  POST /api/afd

function desenharAfd(automato, r) {
  // Quadro 0 = antes de ler; quadro k = depois do passo k
  const quadros = [{ ativos: [automato.estadoInicial], info: `<span><b>Início:</b> ${esc(automato.estadoInicial)}</span>` }];
  const linhas = [['0', automato.estadoInicial, '—', '—', automato.cadeia || 'λ']];

  r.passos.forEach(passo => {
    quadros.push({
      ativos: [passo.destino],
      info: `<span><b>Passo:</b> ${passo.numero}</span><span><b>δ(${esc(passo.estado)}, ${esc(passo.simbolo)}) =</b> ${esc(passo.destino)}</span>`,
    });
    linhas.push([passo.numero, passo.estado, passo.simbolo, passo.destino, passo.restante || 'λ']);
  });

  preencherTabela('afd', linhas);
  mostrarExecucao('afd', automato, quadros);
  irParaQuadro('afd', quadros.length - 1);
}

$('afd-simular').onclick = () => executarAutomato('afd', '/api/afd', desenharAfd);
$('afd-exemplo').onclick = () => carregarExemplo('afd', 'afd-enunciado.json');
$('afd-exemplo2').onclick = () => carregarExemplo('afd', 'afd-numero-par-de-as.json');
$('afd-cadeia').addEventListener('keydown', e => { if (e.key === 'Enter') $('afd-simular').click(); });
ligarArquivoJson('afd');
ligarNavegacao('afd');
carregarExemplo('afd', 'afd-enunciado.json');
