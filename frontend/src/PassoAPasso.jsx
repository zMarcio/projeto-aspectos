import { useState } from 'react';

export function PassoAPasso({ prefixo, cabecalhos, linhas, desenhar, titulo }) {
  const ultimo = linhas.length - 1;
  const [atual, selecionar] = useState(ultimo);

  return (
    <>
      {desenhar(atual)}
      <div className="actions">
        <button type="button" id={`${prefixo}-inicio`} aria-label="Primeiro passo" disabled={atual === 0}
          onClick={() => selecionar(0)}>⏮</button>
        <button type="button" id={`${prefixo}-anterior`} aria-label="Passo anterior" disabled={atual === 0}
          onClick={() => selecionar(i => i - 1)}>◀</button>
        <button type="button" id={`${prefixo}-proximo`} aria-label="Próximo passo" disabled={atual === ultimo}
          onClick={() => selecionar(i => i + 1)}>▶</button>
        <button type="button" id={`${prefixo}-fim`} aria-label="Último passo" disabled={atual === ultimo}
          onClick={() => selecionar(ultimo)}>⏭</button>
      </div>
      {titulo && <h2 className="titulo-passos">Passo a passo</h2>}
      <div className={`table-wrap${titulo ? '' : ' campo-modulo'}`}>
        <table id={`${prefixo}-passos`} className="tabela-passos">
          <thead><tr>{cabecalhos.map(texto => <th key={texto} scope="col">{texto}</th>)}</tr></thead>
          <tbody>
            {linhas.map((celulas, indice) => (
              <tr key={indice} className={atual === indice ? 'atual' : undefined}
                aria-current={atual === indice ? 'step' : undefined} onClick={() => selecionar(indice)}>
                {celulas.map((valor, coluna) => <td key={coluna}>{valor}</td>)}
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </>
  );
}
