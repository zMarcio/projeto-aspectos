// Funções usadas por todas as abas.

// ---------- Troca de abas ----------
document.querySelectorAll('.abas button').forEach(botao => {
  botao.onclick = () => {
    document.querySelectorAll('.abas button').forEach(b => b.classList.remove('ativa'));
    document.querySelectorAll('.aba').forEach(a => a.classList.remove('ativa'));
    botao.classList.add('ativa');
    document.getElementById(botao.dataset.aba).classList.add('ativa');
  };
});

// Atalho para document.getElementById
function $(id) { return document.getElementById(id); }

// "a, b , c" -> ["a","b","c"]
function lista(texto) {
  return texto.split(',').map(s => s.trim()).filter(s => s !== '');
}

// Envia JSON ao backend e devolve a resposta. Se o backend responder erro, lança Error com a mensagem.
async function postJson(url, corpo) {
  const resp = await fetch(url, {
    method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(corpo),
  });
  const texto = await resp.text();
  let dados;
  try {
    dados = JSON.parse(texto);
  } catch {
    throw new Error(`O servidor respondeu ${resp.status} sem JSON. Rode "./gradlew bootRun" `
      + 'e abra a página em http://localhost:8080.');
  }
  if (!resp.ok) throw new Error(dados.erro || `Erro HTTP ${resp.status}.`);
  return dados;
}

// Mostra a faixa verde (ok) ou vermelha (erro)
function mostrarFaixa(elemento, ok, texto) {
  elemento.className = 'resultado ' + (ok ? 'ok' : 'err');
  elemento.textContent = texto;
}

// Escapa texto antes de colocar em innerHTML
function esc(v) {
  return String(v).replace(/[&<>"']/g, c => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' })[c]);
}

// [..] -> "{a, b}"
function conj(itens) { return '{' + itens.join(', ') + '}'; }
