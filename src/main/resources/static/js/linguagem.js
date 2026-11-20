// Aba 1 - Linguagens Formais  ->  POST /api/linguagem

async function calcularLinguagem() {
  const corpo = {
    alfabeto: lista($('lf-alfabeto').value),
    linguagem: lista($('lf-linguagem').value),
    cadeia: $('lf-cadeia').value,
    cadeia2: $('lf-cadeia2').value,
    tamanhoMaximo: Number($('lf-tamanho').value || 0),
  };
  $('lf-saida').classList.remove('hidden');
  try {
    const r = await postJson('/api/linguagem', corpo);
    mostrarFaixa($('lf-resultado'), r.cadeiaValida, r.mensagem);
    $('lf-detalhes').innerHTML = `
      <div class="info">
        <span><b>Σ =</b> ${esc(r.alfabeto)}</span>
        <span><b>L =</b> ${esc(r.linguagem)}</span>
      </div>
      <div class="table-wrap"><table>
        <tr><th>Operação</th><th>Resultado</th></tr>
        <tr><td>w</td><td>${esc(r.cadeia)}</td></tr>
        <tr><td>Tamanho |w|</td><td>${r.tamanho}</td></tr>
        <tr><td>w ∈ Σ* ?</td><td>${r.cadeiaValida ? 'sim' : 'não — inválidos: ' + esc(r.simbolosInvalidos.join(', '))}</td></tr>
        <tr><td>w ∈ L ?</td><td>${r.pertenceALinguagem ? 'sim' : 'não'}</td></tr>
        <tr><td>w₂</td><td>${esc(r.cadeia2)} ${r.cadeia2Valida ? '' : '(inválida para Σ)'}</td></tr>
        <tr><td>Concatenação w·w₂</td><td>${esc(r.concatenacao)} (tamanho ${r.tamanhoConcatenacao})</td></tr>
      </table></div>
      <h2 style="margin-top:18px">Σ* até tamanho ${r.tamanhoMaximo} (${r.sigmaEstrela.length} palavras)</h2>
      <div class="palavras">${esc(conj(r.sigmaEstrela))}</div>
      <h2 style="margin-top:18px">Σ+ até tamanho ${r.tamanhoMaximo} (${r.sigmaMais.length} palavras)</h2>
      <div class="palavras">${esc(conj(r.sigmaMais))}</div>`;
  } catch (e) {
    mostrarFaixa($('lf-resultado'), false, e.message);
    $('lf-detalhes').innerHTML = '';
  }
}

async function exemploLinguagem() {
  const ex = await (await fetch('exemplos/linguagem-enunciado.json')).json();
  $('lf-alfabeto').value = ex.alfabeto.join(', ');
  $('lf-linguagem').value = ex.linguagem.join(', ');
  $('lf-cadeia').value = ex.cadeia;
  $('lf-cadeia2').value = ex.cadeia2;
  $('lf-tamanho').value = ex.tamanhoMaximo;
}

$('lf-calcular').onclick = calcularLinguagem;
$('lf-exemplo').onclick = exemploLinguagem;
$('lf-cadeia').addEventListener('keydown', e => { if (e.key === 'Enter') calcularLinguagem(); });
exemploLinguagem();
