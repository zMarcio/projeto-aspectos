import { useLayoutEffect, useRef } from 'react';
import { createRoot } from 'react-dom/client';
import { PassoAPasso } from './PassoAPasso.jsx';

const raizes = new Map();
let execucao = 0;

function montarPassos(prefixo, cabecalhos, linhas, desenhar, titulo = false) {
  if (!raizes.has(prefixo)) {
    raizes.set(prefixo, createRoot(document.getElementById(`${prefixo}-quadros`)));
  }
  // Uma nova execução começa no último quadro, mesmo quando tem o mesmo tamanho da anterior.
  raizes.get(prefixo).render(
    <PassoAPasso key={++execucao} prefixo={prefixo} cabecalhos={cabecalhos}
      linhas={linhas} desenhar={desenhar} titulo={titulo} />
  );
}

function Estados({ prefixo, automato, ativos, children }) {
  return (
    <>
      <div className="estados" id={`${prefixo}-desenho`}>
        {automato.estados.map(estado => {
          const classes = ['estado'];
          if (estado === automato.estadoInicial) classes.push('inicial');
          if (automato.estadosFinais.includes(estado)) classes.push('final');
          if (ativos.includes(estado)) classes.push('ativo');
          return <div key={estado} className={classes.join(' ')}>{estado}</div>;
        })}
      </div>
      <div className="info" id={`${prefixo}-info`} aria-live="polite">{children}</div>
    </>
  );
}

export function mostrarAfd(automato, { passos }) {
  const linhas = [[0, automato.estadoInicial, '—', '—', automato.cadeia || 'λ'],
    ...passos.map(p => [p.numero, p.estado, p.simbolo, p.destino, p.restante || 'λ'])];

  montarPassos('afd', ['Passo', 'Estado', 'Lê', 'Vai para', 'Falta ler'], linhas, indice => {
    const passo = passos[indice - 1];
    return (
      <Estados prefixo="afd" automato={automato} ativos={[passo?.destino ?? automato.estadoInicial]}>
        {passo ? (
          <>
            <span><b>Passo:</b> {passo.numero}</span>
            <span><b>δ({passo.estado}, {passo.simbolo}) =</b> {passo.destino}</span>
          </>
        ) : <span><b>Início:</b> {automato.estadoInicial}</span>}
      </Estados>
    );
  }, true);
}

function conjunto(estados) {
  return `{${estados.join(', ')}}`;
}

export function mostrarAfn(automato, { passos, estadosAtivos }) {
  const iniciais = passos[0]?.antes ?? estadosAtivos;
  const linhas = [[0, '—', '—', '—', conjunto(iniciais), automato.cadeia || 'λ'],
    ...passos.map(p => [p.numero, conjunto(p.antes), p.simbolo, conjunto(p.depoisDoMove), conjunto(p.depois), p.restante || 'λ'])];

  montarPassos('afn', ['Passo', 'Estados antes', 'Lê', 'Move', 'Depois do fecho-λ', 'Falta ler'], linhas, indice => {
    const passo = passos[indice - 1];
    return (
      <Estados prefixo="afn" automato={automato} ativos={passo?.depois ?? iniciais}>
        {passo ? (
          <>
            <span><b>Passo:</b> {passo.numero}</span>
            <span><b>Lê:</b> {passo.simbolo}</span>
            <span><b>Ativos:</b> {conjunto(passo.depois)}</span>
          </>
        ) : <span><b>Início (fecho-λ de {automato.estadoInicial}):</b> {conjunto(iniciais)}</span>}
      </Estados>
    );
  }, true);
}

function Celulas({ id, simbolos, destaque, referencia }) {
  return (
    <div id={id} className="celulas" ref={referencia}>
      {simbolos.length ? simbolos.map((simbolo, posicao) => (
        <span key={posicao} className={posicao === destaque ? 'celula ativa' : 'celula'}>{simbolo}</span>
      )) : '∅'}
    </div>
  );
}

export function mostrarPilha({ passos }) {
  const linhas = passos.map(p => [p.numero, p.simbolo || '—', p.estado, p.consumidos, p.pilha.join(' ') || '∅']);

  montarPassos('ap', ['Passo', 'Lê', 'Estado', 'Símbolos consumidos', 'Pilha'], linhas, indice => {
    const passo = passos[indice];
    return (
      <>
        <p id="ap-configuracao" className="info" aria-live="polite">
          {`Passo ${passo.numero} · Estado ${passo.estado} · Lido: ${passo.simbolo || '—'}`}
        </p>
        <h2>Pilha (topo à esquerda)</h2>
        <Celulas id="ap-pilha" simbolos={passo.pilha} destaque={0} />
      </>
    );
  });
}

function Fita({ passo }) {
  const referencia = useRef(null);

  useLayoutEffect(() => {
    const fita = referencia.current;
    const cabecote = fita.children[passo.cabecote];
    fita.scrollLeft = cabecote.offsetLeft - Math.floor(fita.clientWidth / 2);
  }, [passo]);

  return (
    <>
      <p id="mt-configuracao" className="info" aria-live="polite">
        {`Passo ${passo.numero} · Estado ${passo.estado} · Lê: ${passo.simboloLido}`}
      </p>
      <Celulas id="mt-fita" simbolos={Array.from(passo.fita)} destaque={passo.cabecote} referencia={referencia} />
    </>
  );
}

export function mostrarFita({ passos }) {
  const linhas = passos.map(p => [p.numero, p.estado, p.simboloLido, p.cabecote]);
  montarPassos('mt', ['Passo', 'Estado', 'Símbolo sob o cabeçote', 'Posição do cabeçote'], linhas,
    indice => <Fita passo={passos[indice]} />);
}
