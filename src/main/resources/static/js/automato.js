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

// ---------- Visualização passo a passo ----------
// "quadros" é uma lista; cada quadro = { ativos: [estados acesos], info: 'html' }.
// A linha i da tabela de passos corresponde ao quadro i.
const navegacao = {};

function mostrarExecucao(p, automato, quadros) {
  navegacao[p] = { automato, quadros, atual: 0 };
  irParaQuadro(p, 0);
}

function irParaQuadro(p, i) {
  const nav = navegacao[p];
  if (!nav) return;
  nav.atual = Math.max(0, Math.min(i, nav.quadros.length - 1));
  const q = nav.quadros[nav.atual];

  // Desenha cada estado como uma "bolinha"; inicial tem seta, final tem borda dupla, ativo fica amarelo
  $(p + '-desenho').innerHTML = nav.automato.estados.map(e => {
    const classes = ['estado'];
    if (e === nav.automato.estadoInicial) classes.push('inicial');
    if (nav.automato.estadosFinais.includes(e)) classes.push('final');
    if (q.ativos.includes(e)) classes.push('ativo');
    return `<div class="${classes.join(' ')}">${esc(e)}</div>`;
  }).join('');
  $(p + '-info').innerHTML = q.info;

  document.querySelectorAll(`#${p}-passos tbody tr`).forEach((tr, idx) => tr.classList.toggle('atual', idx === nav.atual));
  $(p + '-inicio').disabled = $(p + '-anterior').disabled = nav.atual === 0;
  $(p + '-proximo').disabled = $(p + '-fim').disabled = nav.atual === nav.quadros.length - 1;
}

function ligarNavegacao(p) {
  $(p + '-inicio').onclick = () => irParaQuadro(p, 0);
  $(p + '-anterior').onclick = () => irParaQuadro(p, navegacao[p].atual - 1);
  $(p + '-proximo').onclick = () => irParaQuadro(p, navegacao[p].atual + 1);
  $(p + '-fim').onclick = () => irParaQuadro(p, navegacao[p].quadros.length - 1);
}

// Preenche a tabela de passos; clicar numa linha pula para aquele quadro
function preencherTabela(p, linhas) {
  const tbody = document.querySelector(`#${p}-passos tbody`);
  tbody.innerHTML = '';
  linhas.forEach((celulas, i) => {
    const tr = document.createElement('tr');
    tr.innerHTML = celulas.map(c => `<td>${esc(c)}</td>`).join('');
    tr.style.cursor = 'pointer';
    tr.onclick = () => irParaQuadro(p, i);
    tbody.appendChild(tr);
  });
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
