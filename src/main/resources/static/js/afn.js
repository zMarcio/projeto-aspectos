// Aba 3 - AFN  ->  POST /api/afn

function desenharAfn(automato, r) {
  // Conjunto inicial (já com fecho-λ): vem do primeiro passo, ou do resultado se a cadeia for vazia
  const iniciais = r.passos.length ? r.passos[0].antes : r.estadosAtivos;
  const quadros = [{ ativos: iniciais, info: `<span><b>Início (fecho-λ de ${esc(automato.estadoInicial)}):</b> ${esc(conj(iniciais))}</span>` }];
  const linhas = [['0', '—', '—', '—', conj(iniciais), automato.cadeia || 'λ']];

  r.passos.forEach(passo => {
    quadros.push({
      ativos: passo.depois,
      info: `<span><b>Passo:</b> ${passo.numero}</span><span><b>Lê:</b> ${esc(passo.simbolo)}</span>
             <span><b>Ativos:</b> ${esc(conj(passo.depois))}</span>`,
    });
    linhas.push([passo.numero, conj(passo.antes), passo.simbolo, conj(passo.depoisDoMove), conj(passo.depois), passo.restante || 'λ']);
  });

  preencherTabela('afn', linhas);
  mostrarExecucao('afn', automato, quadros);
  irParaQuadro('afn', quadros.length - 1);
}

$('afn-simular').onclick = () => executarAutomato('afn', '/api/afn', desenharAfn);
$('afn-exemplo').onclick = () => carregarExemplo('afn', 'afn-termina-em-ab.json');
$('afn-exemplo2').onclick = () => carregarExemplo('afn', 'afn-lambda-a-ou-b.json');
$('afn-cadeia').addEventListener('keydown', e => { if (e.key === 'Enter') $('afn-simular').click(); });
ligarArquivoJson('afn');
ligarNavegacao('afn');
carregarExemplo('afn', 'afn-termina-em-ab.json');
