// Fluxo de formulário das abas GLC, AP e Máquina de Turing.
function ligarSimulacao(prefixo, rota, exemplo, ler, preencher, renderizar) {
  const saida = $(`${prefixo}-saida`);
  const visual = $(`${prefixo}-visual`);
  const faixa = $(`${prefixo}-resultado`);
  const botao = $(`${prefixo}-simular`);
  const restaurar = $(`${prefixo}-exemplo`);
  const rotulos = { ACEITA: 'Aceita', REJEITADA: 'Rejeitada', LIMITE_ATINGIDO: 'Limite atingido' };

  function mostrarErro(erro) {
    mostrarFaixa(faixa, false, `Erro: ${erro.message}`);
    visual.classList.add('hidden');
    saida.classList.remove('hidden');
  }

  function bloquear(bloqueado) {
    botao.disabled = bloqueado;
    restaurar.disabled = bloqueado;
  }

  async function carregar() {
    bloquear(true);
    try {
      const resposta = await fetch(`exemplos/${exemplo}`);
      if (!resposta.ok) throw new Error('Não foi possível carregar o exemplo.');
      preencher(await resposta.json());
      saida.classList.add('hidden');
    } catch (erro) {
      mostrarErro(erro);
    } finally {
      bloquear(false);
    }
  }

  $(`${prefixo}-form`).onsubmit = async evento => {
    evento.preventDefault();
    if (botao.disabled) return;
    bloquear(true);
    try {
      const resultado = await postJson(rota, ler());
      mostrarFaixa(faixa, resultado.status === 'ACEITA', `${rotulos[resultado.status]} — ${resultado.mensagem}`);
      if (resultado.status === 'LIMITE_ATINGIDO') faixa.className = 'resultado inconclusivo';
      visual.classList.remove('hidden');
      saida.classList.remove('hidden');
      renderizar(resultado);
    } catch (erro) {
      mostrarErro(erro);
    } finally {
      bloquear(false);
    }
  };

  restaurar.onclick = carregar;
  carregar();
}
