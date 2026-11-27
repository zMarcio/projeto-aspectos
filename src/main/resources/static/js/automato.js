// Funções comuns das abas AFD e AFN.
// O parâmetro "p" é o prefixo dos ids do HTML: 'afd' ou 'afn'.

// Lê o formulário e monta o JSON no formato que o backend espera (o mesmo do arquivo .json)
function lerAutomato(p) {
  const transicoes = $(p + '-transicoes').value.split('\n')
    .map(linha => linha.trim())
    .filter(linha => linha !== '')
    .map(linha => {
      // aceita "q0 a q1" ou "q0, a, q1"
      const partes = linha.split(/[\s,]+/);
      return { origem: partes[0] || '', simbolo: partes[1] || '', destino: partes[2] || '' };
    });
  return {
    estados: lista($(p + '-estados').value),
    alfabeto: lista($(p + '-alfabeto').value),
    estadoInicial: $(p + '-inicial').value.trim(),
    estadosFinais: lista($(p + '-finais').value),
    transicoes,
    cadeia: $(p + '-cadeia').value,
  };
}

// Faz o caminho inverso: JSON -> formulário
function preencherAutomato(p, json) {
  $(p + '-estados').value = (json.estados || []).join(', ');
  $(p + '-alfabeto').value = (json.alfabeto || []).join(', ');
  $(p + '-inicial').value = json.estadoInicial || '';
  $(p + '-finais').value = (json.estadosFinais || []).join(', ');
  $(p + '-transicoes').value = (json.transicoes || [])
    .map(t => `${t.origem} ${t.simbolo || 'λ'} ${t.destino}`).join('\n');
  $(p + '-cadeia').value = json.cadeia || '';
}

async function carregarExemplo(p, arquivo) {
  const json = await (await fetch('exemplos/' + arquivo)).json();
  preencherAutomato(p, json);
}

// Botões "Abrir JSON…" e "Salvar JSON"
function ligarArquivoJson(p) {
  $(p + '-abrir').onclick = () => $(p + '-arquivo').click();
  $(p + '-arquivo').onchange = async evento => {
    const arquivo = evento.target.files[0];
    if (!arquivo) return;
    try {
      preencherAutomato(p, JSON.parse(await arquivo.text()));
    } catch (e) {
      alert('JSON inválido: ' + e.message);
    }
    evento.target.value = '';
  };
  $(p + '-baixar').onclick = () => {
    const blob = new Blob([JSON.stringify(lerAutomato(p), null, 2)], { type: 'application/json' });
    const a = document.createElement('a');
    a.href = URL.createObjectURL(blob);
    a.download = p + '.json';
    a.click();
    URL.revokeObjectURL(a.href);
  };
}

// Executa: lê o formulário, chama o backend e entrega a resposta para "desenhar"
async function executarAutomato(p, url, desenhar) {
  const automato = lerAutomato(p);
  $(p + '-saida').classList.remove('hidden');
  try {
    const r = await postJson(url, automato);
    mostrarFaixa($(p + '-resultado'), r.aceita, (r.aceita ? '✔ ACEITA — ' : '✘ REJEITADA — ') + r.mensagem);
    $(p + '-visual').classList.remove('hidden');
    $(p + '-caminho').textContent = r.caminho;
    desenhar(automato, r);
  } catch (e) {
    mostrarFaixa($(p + '-resultado'), false, e.message);
    $(p + '-visual').classList.add('hidden');
  }
}
